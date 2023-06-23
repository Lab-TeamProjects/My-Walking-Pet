package com.lab_team_projects.my_walking_pet.walk_count;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.lab_team_projects.my_walking_pet.login.User;

import java.util.Locale;

/**
 * 걸음 정보 클래스
 * 사용자의 걸음 수를 저장하고 관련 메소드가 정의됨
 */
@Entity
public class Walk {
    @PrimaryKey(autoGenerate = true)
    private int id;    // db id
    /**
     * 걸음 클래스가 만들어진 날짜
     */
    private String date;    // 현재 날짜
    /**
     * 사용자가 지정한 걸음 일일 목표 (기본값: 1000)
     */
    private int goal = 1000;    // 목표

    /**
     * 총 걸음 수 (걸은 걸음 + 뛴 걸음)
     */
    private int count;    // 걸음 수
    /**
     * 사용자가 걸은 걸음 카운트
     */
    private int walkCount;
    /**
     * 사용자가 뛴 걸음 카운트
     */
    private int runCount;
    /**
     * 일일 칼로리 소모량
     */
    private double kcal;
    /**
     * 일일 걷거나 뛴 거리
     */
    private double distance;    // 총 거리
    /**
     * 일일 걷거나 뛴 시간 (단위: 초)
     */
    private int sec;    // 걸은 초


    /**
     * 운동 패턴 설계 창에서 걷거나 뛴 걸음 수
     */
    private int exerciseCount;
    /**
     * 운동 패턴 설계 창에서 뛴 걸음 수
     */
    private int exerciseRunCount;
    /**
     * 운동 패턴 설계 창에서 걸은 걸음 수
     */
    private int exerciseWalkCount;
    /**
     * 운동 패턴 설계 창에서 소모된 칼로리
     */
    private double exerciseKcal;
    /**
     * 운동 패턴 설계 창에서 걸은 거리
     */
    private double exerciseDistance;
    /**
     * 운동 패턴 설계 창에서 걸은 시간
     */
    private int exerciseWalkSec;


    /**
     * 유저 정보를 받아서 걸은 걸음 수, 뛴 걸음 수, 보폭을 이용하여 칼로리 소모량을 계산하여 설정합니다.
     *
     * @param user 유저 정보 클래스
     */
    public void calculateKcal(User user) {
        this.kcal = (3.8 * (3.5 * user.getWeight() * ((user.calculateStride() / 100 * this.walkCount / 1.6) / 60)) / 1000 * 5) +
                (10 * (3.5 * user.getWeight() * ((user.calculateRunStride() / 100 * this.runCount / 2.7) / 60)) / 1000 * 5);

    }

    /**
     * 운동 패턴 설계 전용 칼로리 소모량을 계산하고 설정합니다.
     *
     * @param user 유저 정보 클래스
     */
    public void exCalculateKcal(User user) {
        this.exerciseKcal = (3.8 * (3.5 * user.getWeight() * ((user.calculateStride() / 100 * this.exerciseWalkCount / 1.6) / 60)) / 1000 * 5) +
                (10 * (3.5 * user.getWeight() * ((user.calculateRunStride() / 100 * this.exerciseRunCount / 2.7) / 60)) / 1000 * 5);
    }

    /**
     * 보폭과 걸음 수를 이용하여 운동 시간을 계산합니다.
     *
     * @param user 유저 정보로 부터 보폭을 받습니다.
     * @return 계산된 운동 시간을 반환합니다.
     */
    public int calculateSec(User user) {
        return (int) (user.calculateStride() / 100 * this.walkCount / 1.6) +
                (int) (user.calculateRunStride() / 100 * this.runCount / 2.7);
    }

    /**
     * 보폭과 걸음 수를 이용하여 운동 패턴 설계의 운동 시간을 계산합니다.
     *
     * @param user 유저 정보로 부터 보폭을 받습니다.
     * @return 계산된 운동 시간을 반환합니다.
     */
    public int exerciseCalculateSec(User user) {
        return (int) (user.calculateStride() / 100 * this.exerciseWalkCount / 1.6) +
                (int) (user.calculateRunStride() / 100 * this.exerciseRunCount / 2.7);
    }

    /**
     * 거리를 계산합니다.
     *
     * @param user 유저 정보로 부터 보폭을 받습니다.
     * @return 계산된 거리를 반환합니다.
     */
    public double calculateDistance(User user) {
        // km로 반환함
        return (this.count * (user.calculateStride() * 0.01) * 0.001);
    }

    /**
     * 운동 시간을 시간, 분, 초로 변환합니다.
     *
     * @return 변환된 시간을 HH:mm:ss 문자열 포맷으로 반환합니다.
     */
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
