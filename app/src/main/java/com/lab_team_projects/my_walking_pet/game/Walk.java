package com.lab_team_projects.my_walking_pet.game;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Date;

public class Walk {
    private int count = 0;
    private String date;
    private int goal;
    private int min;
    private int sec;
    private double kcal;
    private double km;


    public String getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }

    public int getGoal() {
        return goal;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }



}
