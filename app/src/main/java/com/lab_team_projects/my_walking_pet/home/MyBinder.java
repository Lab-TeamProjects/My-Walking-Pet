package com.lab_team_projects.my_walking_pet.home;

import android.os.Binder;

public class MyBinder extends Binder {

    private String state;

    public interface OnBinderListener {
        void onExercise(boolean flag);
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