package com.lab_team_projects.my_walking_pet.game;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Date;

public class Walk {
    private Integer count = 0;
    private Date date;
    private Integer goal;

    public Date getDate() {
        return date;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }
}
