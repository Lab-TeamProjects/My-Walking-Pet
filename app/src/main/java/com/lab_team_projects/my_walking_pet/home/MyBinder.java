package com.lab_team_projects.my_walking_pet.home;

import android.os.Binder;

/**
 * 포그라운드 서비스와 메인 스레드간의 통신을 하면서 데이터를 주고 받을 수 있도록 하기 위한 바인더 클래스
 * 바인더 클래스를 상속받고 있습니다.
 */
public class MyBinder extends Binder {

    private String state;

    public interface OnBinderListener {
        void onFinish(boolean flag);
    }

    public interface OnBinderStateListener {
        void onChange(String state);
    }

    private OnBinderListener listener;
    private OnBinderStateListener stateListener;

    public void setListener(OnBinderListener listener) {
        this.listener = listener;
    }

    public OnBinderListener getListener() {
        return listener;
    }

    public OnBinderStateListener getStateListener() {
        return stateListener;
    }

    public void setStateListener(OnBinderStateListener stateListener) {
        this.stateListener = stateListener;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}