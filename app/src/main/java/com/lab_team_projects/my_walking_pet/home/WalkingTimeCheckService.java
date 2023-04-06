package com.lab_team_projects.my_walking_pet.home;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.helpers.TTSHelper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class WalkingTimeCheckService extends Service implements SensorEventListener {

    private int time;

    private TTSHelper thr;

    private GameManager gm;

    private final Handler ttsHandler = new Handler();

    public WalkingTimeCheckService(){ }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.time = intent.getIntExtra("time",0);
        Log.e("tts", String.valueOf(this.time));

        thr = new TTSHelper(getApplicationContext());

        Map<Integer,String> map = new LinkedHashMap<>();

        map.put(0, "운동 전 준비운동을 하세요!");
        for(int i = 1; i < (this.time / 5) - 1; i++) {
            if(i%2 == 1) {
                map.put(i,"뛰세요");
            } else {
                map.put(i,"걸으세요");
            }
        }
        map.put((this.time / 5) - 1, "마무리 운동 시간입니다.");
        map.put((this.time / 5), "수고하셨습니다.");

        map.forEach(new BiConsumer<Integer, String>() {
            @Override
            public void accept(Integer integer, String s) {
                ttsHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                        Log.e("tts", integer + " : " + s);
                        thr.speak(s);
                    }
                }, integer * 1000 * 60);
            }
        });

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        gm = GameManager.getInstance();

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepCounter!= null) {
            Log.d("__walk", "카운터 있음");
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            //Room DB랑 연동해서 걸음 수 증가 시키기
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}