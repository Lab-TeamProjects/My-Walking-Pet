package com.lab_team_projects.my_walking_pet.home;

import android.os.Binder;

/**
 * 포그라운드 서비스와 메인 스레드간의 통신을 하면서 데이터를 주고 받을 수 있도록 하기 위한 바인더 클래스
 * 바인더 클래스를 상속받고 있습니다.
 */
public class MyBinder extends Binder {

    private String state;

    /**
     * The interface On binder listener.
     */
    public interface OnBinderListener {
        /**
         * On finish.
         *
         * @param flag the flag
         */
        void onFinish(boolean flag);
    }

    /**
     * The interface On binder state listener.
     */
    public interface OnBinderStateListener {
        /**
         * On change.
         *
         * @param state the state
         */
        void onChange(String state);
    }

    private OnBinderListener listener;
    private OnBinderStateListener stateListener;

    /**
     * Sets listener.
     *
     * @param listener the listener
     */
    public void setListener(OnBinderListener listener) {
        this.listener = listener;
    }

    /**
     * Gets listener.
     *
     * @return the listener
     */
    public OnBinderListener getListener() {
        return listener;
    }

    /**
     * Gets state listener.
     *
     * @return the state listener
     */
    public OnBinderStateListener getStateListener() {
        return stateListener;
    }

    /**
     * Sets state listener.
     *
     * @param stateListener the state listener
     */
    public void setStateListener(OnBinderStateListener stateListener) {
        this.stateListener = stateListener;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(String state) {
        this.state = state;
    }
}