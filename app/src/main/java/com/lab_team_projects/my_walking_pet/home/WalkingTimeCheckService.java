package com.lab_team_projects.my_walking_pet.home;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;
import com.lab_team_projects.my_walking_pet.helpers.TTSHelper;
import com.lab_team_projects.my_walking_pet.login.User;
import com.lab_team_projects.my_walking_pet.walk_count.Walk;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 사용자의 운동 패턴을 설계하기 위한 서비스 클래스
 */
public class WalkingTimeCheckService extends Service implements SensorEventListener {

    /**
     * 바인더 클래스
     */
    private final MyBinder binder = new MyBinder();
    /**
     * 사용자가 설정한 시간
     */
    private int time;
    /**
     * TTS 헬퍼 클래스
     */
    private TTSHelper thr;
    private GameManager gm;
    private User user;
    private AppDatabase db;

    private final Handler ttsHandler = new Handler();

    private int lastStep = 0;
    private boolean isFirstRun = false;
    private Walk walk;

    /**
     * Instantiates a new Walking time check service.
     */
    public WalkingTimeCheckService(){}

    /**
     * 시작 버튼을 누르면 실행되는 메서드
     * 운동 패턴을 정의합니다.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.time = intent.getIntExtra("time",0);
        Log.e("tts", String.valueOf(this.time));
        thr = new TTSHelper(getApplicationContext());
        Map<Integer,String> map = new LinkedHashMap<>();
        map.put(1, "준비 운동을 하세요");
        for(int i = 2; i < (this.time / 5); i++) {
            if(i%2 == 1) {
                map.put(i,"뛰세요");
            } else {
                map.put(i,"걸으세요");
            }
        }
        map.put((this.time / 5), "몸풀기 시간 입니다");
        map.put((this.time / 5) + 1, "수고 하셨습니다");

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            Integer integer = entry.getKey();
            String s = entry.getValue();
            ttsHandler.postDelayed(() -> {
                Log.e("tts", integer + " : " + s);
                binder.setState(s);
                if (binder.getStateListener() != null) {
                    binder.getStateListener().onChange(s, this.time, (this.time-(integer*5)) + 5);
                }
                thr.speak(s);

                if (integer == time / 5 + 1) {
                    binder.getListener().onFinish(false);
                }
            }, integer * 1000 * 2);
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public MyBinder onBind(Intent intent) {
        return binder;
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
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (!isFirstRun) {
                lastStep = (int) event.values[0];
                isFirstRun = true;
            } else {
                int increaseValue = (int) event.values[0] - lastStep;
                step(increaseValue, false);
                lastStep = (int) event.values[0];
            }
        }
    }

    /**
     * 운동시 걸음 수를 저장하는 메서드입니다.
     */
    private void step(int step, boolean isRunning) {
        if (isRunning) {
            walk.setExerciseRunCount(walk.getExerciseRunCount() + step);
            walk.setExerciseDistance(walk.getExerciseDistance() + (step * (user.calculateRunStride() * 0.01) * 0.001));
        } else {
            walk.setExerciseWalkCount(walk.getExerciseWalkCount() + step);
            walk.setExerciseDistance(walk.getExerciseDistance() + (step * (user.calculateStride() * 0.01) * 0.001));
        }
        walk.setExerciseCount(walk.getExerciseWalkCount() + walk.getExerciseRunCount());
        walk.setExerciseWalkSec(walk.exerciseCalculateSec(user));
        if (gm != null) {
            walk.exCalculateKcal(user);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}