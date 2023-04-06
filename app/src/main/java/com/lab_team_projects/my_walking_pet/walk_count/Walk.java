package com.lab_team_projects.my_walking_pet.walk_count;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.lab_team_projects.my_walking_pet.login.User;

import java.util.Locale;

@Entity
public class Walk {
    @PrimaryKey(autoGenerate = true)
    private int id;    // db id
    private int count;    // 걸음 수
    private String date;    // 현재 날짜
    private int goal = 1000;    // 목표
    private int sec;    // 걸은 초
    private double kcal;
    private double distance;    // 총 거리
    private int walkCount;
    private int runCount;
    private int exerciseCount;

    public double calculateKcal(User user) {
        // 칼로리 계산
        // 몸무게 kg * 거리 km * 단위 면적당 칼로리 소모량 (걷기는 약 30 ~ 40)
        return user.getWeight() * calculateDistance(user) * 0.57;
    }

    public void calculateSec(User user) {
        this.sec = (int) ((user.getWalkRunStride()[0] * walkCount) / (14 / 7 * user.getWalkRunStride()[0])
                        + (user.getWalkRunStride()[1] * runCount) / (15 / 8 * user.getWalkRunStride()[1]));
    }

    public double calculateDistance(User user) {
        // km로 반환함
        return (this.count * (user.calculateStride() * 0.01) * 0.001);
    }

    public String calculateHours() {
        int hours = this.sec / 3600;
        int minutes = (this.sec % 3600) / 60;
        int seconds = this.sec % 60;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    public int getWalkCount() {
        return walkCount;
    }

    public void setWalkCount(int walkCount) {
        this.walkCount = walkCount;
    }

    public int getRunCount() {
        return runCount;
    }

    public void setRunCount(int runCount) {
        this.runCount = runCount;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(int exerciseCount) {
        this.exerciseCount = exerciseCount;
    }
}
