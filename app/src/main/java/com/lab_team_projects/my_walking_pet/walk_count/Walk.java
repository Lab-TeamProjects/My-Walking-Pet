package com.lab_team_projects.my_walking_pet.walk_count;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.lab_team_projects.my_walking_pet.login.User;

import java.util.Locale;

@Entity
public class Walk {
    @PrimaryKey(autoGenerate = true)
    private int id;    // db id
    private String date;    // 현재 날짜
    private int goal = 1000;    // 목표

    private int count;    // 걸음 수
    private int walkCount;
    private int runCount;
    private double kcal;
    private double distance;    // 총 거리
    private int sec;    // 걸은 초

    private int exerciseCount;
    private int exerciseRunCount;
    private int exerciseWalkCount;
    private double exerciseKcal;
    private double exerciseDistance;
    private int exerciseWalkSec;

    public void calculateKcal(User user) {
        this.kcal = (3.8 * (3.5 * user.getWeight() * ((user.calculateStride() / 100 * this.walkCount / 1.6) / 60)) / 1000 * 5) +
                (10 * (3.5 * user.getWeight() * ((user.calculateRunStride() / 100 * this.runCount / 2.7) / 60)) / 1000 * 5);

    }

    public void exCalculateKcal(User user) {
        this.exerciseKcal = (3.8 * (3.5 * user.getWeight() * ((user.calculateStride() / 100 * this.exerciseWalkCount / 1.6) / 60)) / 1000 * 5) +
                (10 * (3.5 * user.getWeight() * ((user.calculateRunStride() / 100 * this.exerciseRunCount / 2.7) / 60)) / 1000 * 5);
    }

    public int calculateSec(User user) {
        return (int) (user.calculateStride() / 100 * this.walkCount / 1.6) +
                (int) (user.calculateRunStride() / 100 * this.runCount / 2.7);
    }

    public int exerciseCalculateSec(User user) {
        return (int) (user.calculateStride() / 100 * this.exerciseWalkCount / 1.6) +
                (int) (user.calculateRunStride() / 100 * this.exerciseRunCount / 2.7);
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

    public int getExerciseRunCount() {
        return exerciseRunCount;
    }

    public void setExerciseRunCount(int exerciseRunCount) {
        this.exerciseRunCount = exerciseRunCount;
    }

    public int getExerciseWalkCount() {
        return exerciseWalkCount;
    }

    public void setExerciseWalkCount(int exerciseWalkCount) {
        this.exerciseWalkCount = exerciseWalkCount;
    }

    public double getExerciseKcal() {
        return exerciseKcal;
    }

    public void setExerciseKcal(double exerciseKcal) {
        this.exerciseKcal = exerciseKcal;
    }

    public double getExerciseDistance() {
        return exerciseDistance;
    }

    public void setExerciseDistance(double exerciseDistance) {
        this.exerciseDistance = exerciseDistance;
    }

    public int getExerciseWalkSec() {
        return exerciseWalkSec;
    }

    public void setExerciseWalkSec(int exerciseWalkSec) {
        this.exerciseWalkSec = exerciseWalkSec;
    }
}
