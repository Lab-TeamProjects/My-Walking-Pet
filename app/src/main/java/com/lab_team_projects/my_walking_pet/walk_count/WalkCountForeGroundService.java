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

    public static BackgroundTask task;
    public static int value = 0;
    private NotificationManager notificationManager;
    private Walk walk;
    private final GameManager gm = GameManager.getInstance();
    private final User user = gm.getUser();
    private final AppDatabase db = AppDatabase.getInstance(this);

    private int lastStep = 0;
    private boolean isFirstRun = false;

    private boolean isWalking = false;
    private boolean isRunning = false;
    private final float[] lastAccelValues = new float[3];
    private static final float THRESHOLD_WALK = 5.0f; // 걷는 동작 판별 임계값
    private static final float THRESHOLD_RUN = 12.5f; // 뛰는 동작 판별 임계값
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
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        task = new BackgroundTask();

        walk = gm.getWalk();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 가속도 센서, 걸음 감지 센서, 자이로스코프 센서 등록 등록
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // 센서 리스너 등록
        if (accelSensor != null) {
            sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (stepDetectorSensor!= null) {
            Log.d("__walk", "디텍터 있음");
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (stepCounter!= null) {
            Log.d("__walk", "카운터 있음");
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
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

    public void stopTask() {
        task.onCancelled();
        task.cancel(true);
        task = null;
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


    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            /*case Sensor.TYPE_STEP_DETECTOR:
                step(true);
                //Toast.makeText(getApplicationContext(), "걸음 디텍터", Toast.LENGTH_SHORT).show();
                break;*/
            case Sensor.TYPE_STEP_COUNTER:
                if (!isFirstRun) {
                    step(1);
                    lastStep = (int) event.values[0];
                    isFirstRun = true;
                } else {
                    step((int) event.values[0] - lastStep);
                    lastStep = (int) event.values[0];
                }
                Log.d("__walk__", String.valueOf(lastStep));
                break;
            default:
                break;
        }
    }



    private void step(int step) {
        walk.setWalkCount(walk.getWalkCount() + step);
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