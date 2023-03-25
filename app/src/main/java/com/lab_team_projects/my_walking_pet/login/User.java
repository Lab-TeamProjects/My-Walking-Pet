package com.lab_team_projects.my_walking_pet.login;

import com.lab_team_projects.my_walking_pet.collection.Collection;
import com.lab_team_projects.my_walking_pet.home.Animal;
import com.lab_team_projects.my_walking_pet.home.Item;
import com.lab_team_projects.my_walking_pet.mission.Mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class User {
    private String email;
    private String password;
    private String nickName;
    private String uid;
    private double weight;
    private double height;    // 단위 cm
    private int age;
    private int gender;
    private double bmi;
    private double bmr;    // 기초 대사량
    private int money = 1000000;
    private List<Item> itemLists;
    private List<Mission> missionList;
    private List<Animal> animalList;
    private int nowSelectedPet = 0;
    private List<Collection> collectionList;



    public double getBMR() {
        if (this.gender == 0) {
            return  65.0 + (13.7 * weight) + (5.0 * height) - (6.8 * age);
        } else {
            return 655.0 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
        }
    }

    public void setBMI(){
        this.bmi = weight / Math.pow(height, 2);
    }

    public float[] getWalkRunStride() {
        // 반환 미터
        return new float[]{ (float) this.height * 0.45f,(float) this.height * 1.3f };
    }

    public double calculateStride() {
        List<Double> list = Arrays.asList(
            height * 0.45,
            height * 0.37,
            height -100.0
        );

        // cm로 반환함
        return (Collections.max(list) + Collections.min(list)) / 2.0;
    }

    public int getNowSelectedPet() {
        return nowSelectedPet;
    }

    public void setNowSelectedPet(int nowSelectedPet) {
        this.nowSelectedPet = nowSelectedPet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public double getBmr() {
        return bmr;
    }

    public void setBmr(double bmr) {
        this.bmr = bmr;
    }

    public int getMoney() { return money; }

    public void setMoney(int money) { this.money = money; }

    public void addMoney(int money) {
        this.money += money;
    }

    public List<Item> getItemLists() {
        return itemLists;
    }

    public void setItemLists(List<Item> itemLists) {
        this.itemLists = itemLists;
    }

    public List<Mission> getMissionList() {
        return missionList;
    }

    public void setMissionList(List<Mission> missionList) {
        this.missionList = missionList;
    }

    public List<Animal> getAnimalList() {
        return animalList;
    }

    public void setAnimalList(List<Animal> animalList) {
        this.animalList = animalList;
    }

    public List<Collection> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(List<Collection> collectionList) {
        this.collectionList = collectionList;
    }
}
