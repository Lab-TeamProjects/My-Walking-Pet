package com.lab_team_projects.my_walking_pet.walk_count;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.lab_team_projects.my_walking_pet.login.User;

@Entity
public class Walk {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int count;
    private String date;
    private int goal;
    private int min;
    private int sec;
    private double kcal;
    private double hours;
    // 단위 km
    private double distance;


    public double calculateKcal(User user) {
        // 칼로리 계산
        // 몸무게 kg * 거리 km * 단위 면적당 칼로리 소모량 (걷기는 약 30 ~ 40)
        return user.getWeight() * calculateDistance(user) * 0.57;
    }


    public double calculateDistance(User user) {
        // km로 반환함
        return (this.count * (user.calculateStride() * 0.01) * 0.001);

    }


    public String calculateHours() {
        return "임시:임시:임시";
    }


    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

}
