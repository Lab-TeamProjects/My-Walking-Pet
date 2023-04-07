package com.lab_team_projects.my_walking_pet.home;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;
import com.lab_team_projects.my_walking_pet.helpers.ExerciseHelper;
import com.lab_team_projects.my_walking_pet.helpers.TTSHelper;
import com.lab_team_projects.my_walking_pet.login.User;
import com.lab_team_projects.my_walking_pet.walk_count.Walk;

import java.io.FileDescriptor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class WalkingTimeCheckService extends Service implements SensorEventListener {

    private MyBinder binder = new MyBinder();
    private ServiceConnection conn;

    private int time;
    private TTSHelper thr;
    private GameManager gm;
    private User user;
    private AppDatabase db;

    private final Handler ttsHandler = new Handler();

    private int lastStep = 0;
    private boolean isFirstRun = false;

    private Walk walk;

    public WalkingTimeCheckService(){ }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.time = intent.getIntExtra("time",0);
        Log.e("tts", String.valueOf(this.time));


        thr = new TTSHelper(getApplicationContext());

        Map<Integer,String> map = new LinkedHashMap<>();

        map.put(1, "운동 전 준비 운동을 하세요");
        for(int i = 2; i < (this.time / 5); i++) {
            if(i%2 == 1) {
                map.put(i,"뛰세요");
            } else {
                map.put(i,"걸으세요");
            }
        }
        map.put((this.time / 5), "마무리 운동 시간 입니다");
        map.put((this.time / 5) + 1, "수고 하셨습니다");

        map.forEach(new BiConsumer<Integer, String>() {
            @Override
            public void accept(Integer integer, String s) {
                ttsHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                        Log.e("tts", integer + " : " + s);
                        thr.speak(s);

                        if (integer == time / 5 + 1) {
                            binder.flag = false;
                            onDestroy();
                        }

                    }
                }, integer * 1000 * 2);
            }
        });

        return START_STICKY;
    }

    @Nullable
    @Override
    public MyBinder onBind(Intent intent) {
        startService(intent);
        return binder;
    }

    public void setConn(ServiceConnection conn) {
        this.conn = conn;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        gm = GameManager.getInstance();
        db = AppDatabase.getInstance(getApplicationContext());
        user = gm.getUser();
        walk = gm.getWalk();

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepCounter!= null) {
            Log.d("__walk", "카운터 있음");
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (!isFirstRun) {
                step(1);
                lastStep = (int) event.values[0];
                isFirstRun = true;
            } else {
                int increaseValue = (int) event.values[0] - lastStep;
                step(increaseValue);
                lastStep = (int) event.values[0];
            }
        }
    }

    private void step(int step) {
        walk.setExerciseWalkCount(walk.getExerciseWalkCount() + step);
        walk.setExerciseCount(walk.getExerciseWalkCount());
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

    public class MyBinder extends Binder {

        public boolean flag = true;

        public WalkingTimeCheckService getService() {
            return WalkingTimeCheckService.this;
        }
    }
}