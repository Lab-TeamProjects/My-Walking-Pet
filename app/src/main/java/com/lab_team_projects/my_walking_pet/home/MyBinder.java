package com.lab_team_projects.my_walking_pet.home;

import android.os.Binder;

public class MyBinder extends Binder {
    public interface OnBinderListener {
        void onExercise(boolean flag);
    }

    private OnBinderListener listener;

    public void setListener(OnBinderListener listener) {
        this.listener = listener;
    }

    public OnBinderListener getListener() {
        return listener;
    }
}