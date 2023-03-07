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
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;
import com.lab_team_projects.my_walking_pet.login.User;
import com.lab_team_projects.my_walking_pet.setting.NoticeSettingActivity;

public class WalkCountForeGroundService extends Service implements SensorEventListener {

    public static BackgroundTask task = new BackgroundTask();
    public static int value = 0;
    private Walk walk;
    private final GameManager gm = GameManager.getInstance();
    private final User user = gm.getUser();
    private final AppDatabase db = AppDatabase.getInstance(this);

    private float[] lastAccelValues = new float[3];
    private static final float THRESHOLD_WALK = 12.5f; // 걷는 동작 판별 임계값
    private static final float THRESHOLD_RUN = 25.5f; // 뛰는 동작 판별 임계값
    private boolean isWalking = false;
    private boolean isRunning = false;

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

        // 가속도 센서 등록 등록
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // 센서 리스너 등록
        if (accelSensor != null) {
            sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        task.execute();
        initializeNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void initializeNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("설정을 보려면 누르세요");
        style.setBigContentTitle(null);
        style.setSummaryText("서비스 동작중");
        builder.setContentText(null);
        builder.setContentTitle(null);
        builder.setOngoing(true);
        builder.setStyle(style);
        builder.setWhen(0);
        builder.setShowWhen(false);

        Intent notificationIntent = new Intent(this, NoticeSettingActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1","포그라운드 서비스", NotificationManager.IMPORTANCE_NONE));
        }
        Notification notification = builder.build();
        startForeground(1, notification);
    }

    private static final float ALPHA = 0.8f; // 필터 계수, 높을 수록 입력갑에 둔감해짐

    private float[] lowPass(float[] input, float[] output) {
        if (output == null) return input;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] accelValues = event.values.clone();
            accelValues = lowPass(accelValues, lastAccelValues);
            float x = accelValues[0];
            float y = accelValues[1];
            float z = accelValues[2];
            float accelMagnitude = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = accelMagnitude - lastAccelValues[2];
            lastAccelValues[2] = accelMagnitude;

            if (delta > THRESHOLD_WALK) {
                // 걸음 수를 증가시키는 기준값을 넘었을 때
                if (!isWalking) {
                    // 이전에 이동 상태가 아니었다면, 이동 상태로 전환됨
                    isWalking = true;
                }
                // 걷는 걸음 수 증가 처리
                step(true);
            } else {
                // 걸음 수를 증가시키는 기준 값을 넘지 못했을 때
                if (isWalking) {
                    // 이전에 이동 상태였다면, 멈춤 상태로 전환됨
                    isWalking = false;
                }
            }

            if (delta > THRESHOLD_RUN && !isRunning) {
                // 이전에 뛰는 상태가 아니었다면, 뛰는 상태로 전환됨
                isRunning = true;
                // 뛰는 걸음 수 증가 처리
                step(false);
            } else if (delta < THRESHOLD_RUN && isRunning) {
                // 뛰는 상태였다면, 멈춤 상태로 전환됨
                isRunning = false;
            }
        }
    }

    private void step(boolean isWalking) {
        if (isWalking) {
            walk.setWalkCount(walk.getWalkCount() + 1);
        } else {
            walk.setRunCount(walk.getRunCount() + 2);
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