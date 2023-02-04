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
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.game.Walk;
import com.lab_team_projects.my_walking_pet.setting.NoticeSettingActivity;

public class WalkCountForeGroundService extends Service {

    public BackgroundTask task;
    public int value = 0;

    public WalkCountForeGroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        task = new BackgroundTask();
        task.execute();

        initializeNotification();
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

    class BackgroundTask extends AsyncTask<Integer, String, Integer> implements SensorEventListener {

        private Walk walk = new Walk();
        private SensorManager sensorManager;
        private Sensor stepCountSensor;

        @Override
        protected Integer doInBackground(Integer... values) {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if(stepCountSensor == null) {
                Toast.makeText(getApplicationContext(), "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
            }
            else {
                while (isCancelled() == false) {
                    try {
                        println(value + "번째 실행중");
                        Thread.sleep(1000);
                        value++;
                    } catch (Exception ex) { }
                }
                return value;
            }
            return null;
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

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                walk.setCount(walk.getCount() + 1);
                println("걸음 수 : " + walk.getCount());
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ForegroundService", "onDestroy");
        task.cancel(true);
    }

    public void println(String msg) {
        Log.d("Foreground", msg);
    }
}