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

    /**
     * Gets walk count.
     *
     * @return the walk count
     */
    public int getWalkCount() {
        return walkCount;
    }

    /**
     * Sets walk count.
     *
     * @param walkCount the walk count
     */
    public void setWalkCount(int walkCount) {
        this.walkCount = walkCount;
    }

    /**
     * Gets run count.
     *
     * @return the run count
     */
    public int getRunCount() {
        return runCount;
    }

    /**
     * Sets run count.
     *
     * @param runCount the run count
     */
    public void setRunCount(int runCount) {
        this.runCount = runCount;
    }

    /**
     * Gets distance.
     *
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Sets distance.
     *
     * @param distance the distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets count.
     *
     * @param count the count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets goal.
     *
     * @return the goal
     */
    public int getGoal() {
        return goal;
    }

    /**
     * Sets goal.
     *
     * @param goal the goal
     */
    public void setGoal(int goal) {
        this.goal = goal;
    }

    /**
     * Gets sec.
     *
     * @return the sec
     */
    public int getSec() {
        return sec;
    }

    /**
     * Sets sec.
     *
     * @param sec the sec
     */
    public void setSec(int sec) {
        this.sec = sec;
    }

    /**
     * Gets kcal.
     *
     * @return the kcal
     */
    public double getKcal() {
        return kcal;
    }

    /**
     * Sets kcal.
     *
     * @param kcal the kcal
     */
    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    /**
     * Gets exercise count.
     *
     * @return the exercise count
     */
    public int getExerciseCount() {
        return exerciseCount;
    }

    /**
     * Sets exercise count.
     *
     * @param exerciseCount the exercise count
     */
    public void setExerciseCount(int exerciseCount) {
        this.exerciseCount = exerciseCount;
    }

    /**
     * Gets exercise run count.
     *
     * @return the exercise run count
     */
    public int getExerciseRunCount() {
        return exerciseRunCount;
    }

    /**
     * Sets exercise run count.
     *
     * @param exerciseRunCount the exercise run count
     */
    public void setExerciseRunCount(int exerciseRunCount) {
        this.exerciseRunCount = exerciseRunCount;
    }

    /**
     * Gets exercise walk count.
     *
     * @return the exercise walk count
     */
    public int getExerciseWalkCount() {
        return exerciseWalkCount;
    }

    /**
     * Sets exercise walk count.
     *
     * @param exerciseWalkCount the exercise walk count
     */
    public void setExerciseWalkCount(int exerciseWalkCount) {
        this.exerciseWalkCount = exerciseWalkCount;
    }

    /**
     * Gets exercise kcal.
     *
     * @return the exercise kcal
     */
    public double getExerciseKcal() {
        return exerciseKcal;
    }

    /**
     * Sets exercise kcal.
     *
     * @param exerciseKcal the exercise kcal
     */
    public void setExerciseKcal(double exerciseKcal) {
        this.exerciseKcal = exerciseKcal;
    }

    /**
     * Gets exercise distance.
     *
     * @return the exercise distance
     */
    public double getExerciseDistance() {
        return exerciseDistance;
    }

    /**
     * Sets exercise distance.
     *
     * @param exerciseDistance the exercise distance
     */
    public void setExerciseDistance(double exerciseDistance) {
        this.exerciseDistance = exerciseDistance;
    }

    /**
     * Gets exercise walk sec.
     *
     * @return the exercise walk sec
     */
    public int getExerciseWalkSec() {
        return exerciseWalkSec;
    }

    /**
     * Sets exercise walk sec.
     *
     * @param exerciseWalkSec the exercise walk sec
     */
    public void setExerciseWalkSec(int exerciseWalkSec) {
        this.exerciseWalkSec = exerciseWalkSec;
    }
}
