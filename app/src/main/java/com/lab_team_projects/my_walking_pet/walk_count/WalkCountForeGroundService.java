package com.lab_team_projects.my_walking_pet.walk_count;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;
import com.lab_team_projects.my_walking_pet.login.User;
import com.lab_team_projects.my_walking_pet.setting.NoticeSettingActivity;

import java.util.Locale;

public class WalkCountForeGroundService extends Service implements SensorEventListener {

    public static BackgroundTask task = new BackgroundTask();
    public static int value = 0;
    private NotificationManager notificationManager;
    private Walk walk;
    private final GameManager gm = GameManager.getInstance();
    private final User user = gm.getUser();
    private final AppDatabase db = AppDatabase.getInstance(this);

    private boolean isWalking = false;
    private boolean isRunning = false;
    private final float[] lastAccelValues = new float[3];
    private static final float THRESHOLD_WALK = 5.0f; // 걷는 동작 판별 임계값
    private static final float THRESHOLD_RUN = 17.5f; // 뛰는 동작 판별 임계값
    private static final float NS2S = 1.0f / 1000000000.0f;
    private long lastTimestamp = 0;
    private float angle = 0;
    private long walkStartTime = 0;
    private long runStartTime = 0;
    private long runElapsedTime;
    private long walkElapsedTime;

    public WalkCountForeGroundService() {
        // 빈 생성자
    }

    public boolean isRunning(){
        return task.getStatus() == AsyncTask.Status.RUNNING;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        walk = gm.getWalk();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 가속도 센서, 걸음 감지 센서, 자이로스코프 센서 등록 등록
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // 센서 리스너 등록
        if (accelSensor != null) {
            sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (stepDetectorSensor!= null) {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (gyroSensor!= null) {
            sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }


        task.execute();
        initializeNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public Notification makeNotification(){
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .bigText(String.format(Locale.getDefault(), "\uD83D\uDC36 펫 다음 성장까지 %d걸음\n\uD83D\uDC5F 목표 걸음까지 %d걸음 남았습니다!", walk.getCount(), walk.getGoal()))
                .setBigContentTitle(String.format(Locale.getDefault(), "%d 걸음", walk.getCount()));


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(bigTextStyle)
                .setContentText(String.format(Locale.getDefault(), "\uD83D\uDC36 펫 다음 성장까지 %d걸음 남았습니다!", walk.getCount()))
                .setContentTitle(String.format(Locale.getDefault(), "%d 걸음", walk.getCount()))
                .setOngoing(true)
                .setNumber(0)    // 앱 뱃지 뜨는 것이 별로라서 없애고 싶은데 안없어짐
                .setWhen(0)
                .setShowWhen(false);

        Intent notificationIntent = new Intent(this, NoticeSettingActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1","포그라운드 서비스", NotificationManager.IMPORTANCE_NONE));
        }

        return builder.build();
    }

    public void initializeNotification() {
        startForeground(1, makeNotification());
    }

    private static final float ALPHA = 0.65f; // 필터 계수, 계수가 높을 수록 필터되는 값이 줄어듬

    private float[] lowPass(float[] input, float[] output) {
        if (output == null) return input;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    public float[] highPassFilter(float[] input) {
        final float ALPHA = 0.65f;
        float[] lastValues = input.clone();

        for (int i = 0; i < input.length; i++) {
            lastValues[i] = ALPHA * (lastValues[i] + input[i] - lastValues[i]);
        }

        return lastValues;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_STEP_DETECTOR:
                step(true);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                float[] accelValues = event.values.clone();
                float[] filteredValues = highPassFilter(accelValues);
                filteredValues = lowPass(accelValues, filteredValues);

                float x = filteredValues[0];
                float y = filteredValues[1];
                float z = filteredValues[2];
                float accelMagnitude = (float) Math.sqrt(x * x + y * y + z * z);
                float delta = accelMagnitude - lastAccelValues[2];
                lastAccelValues[2] = accelMagnitude;

                /*// 걷기 타이머 시작
                if (delta > THRESHOLD_WALK && !isWalking) {
                    Log.d("WALK", "걷기시작");
                    isWalking = true;
                    walkStartTime = SystemClock.elapsedRealtime();
                }

                // 뛰기 타이머 시작
                if (delta > THRESHOLD_RUN && !isRunning) {
                    Log.d("WALK", "뛰기시작");
                    isRunning = true;
                    runStartTime = SystemClock.elapsedRealtime();
                }

                // 걷기 타이머 종료 및 뛰기 타이머 시작
                if (delta < THRESHOLD_RUN && isRunning) {
                    isRunning = false;
                    runElapsedTime = (SystemClock.elapsedRealtime() - runStartTime);
                    Log.d("WALK", "뛰기정지");
                }

                // 뛰기 타이머 종료 및 걷기 타이머 계산
                if (delta < THRESHOLD_WALK && isWalking) {
                    Log.d("WALK", "운동정지");
                    isWalking = false;
                    walkElapsedTime = (SystemClock.elapsedRealtime() - walkStartTime);
                    walk.setSec((int) (walk.getSec() + (walkElapsedTime + runElapsedTime) / 1000));
                    Log.d("WALK", String.format(Locale.getDefault(), "이번 운동으로 %d 초 만큼 운동했습니다.", walk.getSec()));
                    walkElapsedTime = 0;
                    runElapsedTime = 0;
                    updateWalk();
                }*/

                // 걷기 임계값 이상이면 걷는중으로 인지
                if (delta > THRESHOLD_WALK) {
                    long timestamp = event.timestamp;
                    if (lastTimestamp != 0) {
                        // 자이로스코프 이벤트 시간 차이 계산
                        float dt = (timestamp - lastTimestamp) * NS2S;
                        // 현재 기기의 방향을 측정하고 보정
                        angle += event.values[2] * dt;
                        if (angle >= Math.PI / 2) {
                            angle -= Math.PI / 2;
                            // 현재 걷고 있는지, 뛰고 있는지에 맞춰서 걸음 수 증가
                            step(!(delta > THRESHOLD_RUN));
                        }
                    }
                    lastTimestamp = timestamp;
                }
                break;
            case Sensor.TYPE_GYROSCOPE:
                float[] gyroValues = event.values.clone();
                // 자이로스코프 값을 적분하여 보정
                float dt = (event.timestamp - lastTimestamp) * NS2S;
                angle += gyroValues[2] * dt;
                lastTimestamp = event.timestamp;
                break;

        }
    }

    private void step(boolean isWalking) {
        if (isWalking) {
            walk.setWalkCount(walk.getWalkCount() + 1);
        } else {
            walk.setRunCount(walk.getRunCount() + 1);
        }
        walk.setCount(walk.getWalkCount() + walk.getRunCount());
        walk.calculateSec(user);
        updateWalk();
    }

    private void updateWalk() {
        if (gm != null) {
            walk.setKcal(walk.calculateKcal(user));
        }
        db.walkDao().update(walk);
        
        // 센서 리로딩
        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, makeNotification());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ForegroundService", "onDestroy");
        task.cancel(true);
    }

    public static void println(String msg) {
        Log.d("Foreground", msg);
    }


    // 쓰레드
    static class BackgroundTask extends AsyncTask<Integer, String, Integer> {

        @Override
        protected Integer doInBackground(Integer... values) {

            while (isCancelled() == false) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) { }
            }
            return value;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            println("onProgressUpdate()업데이트");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            println("onPostExecute()");
        }

        @Override
        protected void onCancelled() {
            println("onCancelled()");
        }

    }
}