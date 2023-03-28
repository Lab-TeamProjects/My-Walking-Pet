package com.lab_team_projects.my_walking_pet.home;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class WalkingTimeCheckService extends Service {
    private final IBinder binder = new MyServiceBinder();

    private final Handler firstStretchingHandler = new Handler();
    private final Handler secondStretchingHandler = new Handler();

    private final int lastStretching;

    public WalkingTimeCheckService(int exerciseTime) {
        this.lastStretching = exerciseTime - 60 * 1000 * 5;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        int firstStretching = 60 * 1000 * 5;

        firstStretchingHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("first","stretching");
            }
        }, firstStretching);

        secondStretchingHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("second","stretching");
            }
        }, lastStretching);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyServiceBinder extends Binder implements ServiceInterface {
        @Override
        public void startTask() {
            // 서비스에서 실행할 코드 작성
        }

        @Override
        public void stopTask() {
            // 서비스에서 실행할 코드 작성
        }
    }

    public interface OnDataReceivedListener {
        void onDataReceived(String data);
    }

}