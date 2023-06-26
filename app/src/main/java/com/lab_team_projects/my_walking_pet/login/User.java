package com.lab_team_projects.my_walking_pet.login;

import com.lab_team_projects.my_walking_pet.collection.Collection;
import com.lab_team_projects.my_walking_pet.home.Animal;
import com.lab_team_projects.my_walking_pet.home.Item;
import com.lab_team_projects.my_walking_pet.mission.Mission;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The type User.
 */
public class User {
    private String email;
    private String password;
    private String nickName;
    private String uid;
    private String accessToken;

    private double weight;
    private double height;    // 단위 cm
    private String birthday;
    private int age;
    private int gender;
    private double bmi;
    private double bmr;    // 기초 대사량
    private int money;
    private List<Item> itemLists;
    private List<Mission> missionList;
    private List<Animal> animalList;
    private int nowSelectedPet = 0;
    private List<Collection> collectionList;

    /**
     * Sets access token.
     *
     * @param accessToken the access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Gets access token.
     *
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Gets bmr.
     *
     * @return the bmr
     */
    public double getBMR() {
        if (this.gender == 0) {
            return  65.0 + (13.7 * weight) + (5.0 * height) - (6.8 * age);
        } else {
            return 655.0 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
        }
    }

    /**
     * Set bmi.
     */
    public void setBMI(){
        this.bmi = weight / Math.pow(height, 2);
    }

    /**
     * Get walk run stride float [ ].
     *
     * @return the float [ ]
     */
    public float[] getWalkRunStride() {
        // 반환 미터
        return new float[]{ (float) this.height * 0.45f,(float) this.height * 1.3f };
    }

    /**
     * Calculate stride double.
     *
     * @return the double
     */
    public double calculateStride() {
        List<Double> list = Arrays.asList(
            height * 0.45,
            height * 0.37,
            height -100.0
        );

        // cm로 반환함
        return (Collections.max(list) + Collections.min(list)) / 2.0;
    }

    /**
     * Calculate run stride double.
     *
     * @return the double
     */
    public double calculateRunStride() {
        return height * 0.6;
    }

    /**
     * Gets birthday.
     *
     * @return the birthday
     */
    public String getBirthday() { return birthday; }

    /**
     * Sets birthday.
     *
     * @param birthday the birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * Gets now selected pet.
     *
     * @return the now selected pet
     */
    public int getNowSelectedPet() {
        return nowSelectedPet;
    }

    /**
     * Sets now selected pet.
     *
     * @param nowSelectedPet the now selected pet
     */
    public void setNowSelectedPet(int nowSelectedPet) {
        this.nowSelectedPet = nowSelectedPet;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets nick name.
     *
     * @return the nick name
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Sets nick name.
     *
     * @param nickName the nick name
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets weight.
     *
     * @param weight the weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Gets age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets age.
     *
     * @param age the age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets gender.
     *
     * @return the gender
     */
    public int getGender() {
        return gender;
    }

    /**
     * Sets gender.
     *
     * @param gender the gender
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     * Gets bmi.
     *
     * @return the bmi
     */
    public double getBmi() {
        return bmi;
    }

    /**
     * Sets bmi.
     *
     * @param bmi the bmi
     */
    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    /**
     * Gets bmr.
     *
     * @return the bmr
     */
    public double getBmr() {
        return bmr;
    }

    /**
     * Sets bmr.
     *
     * @param bmr the bmr
     */
    public void setBmr(double bmr) {
        this.bmr = bmr;
    }

    /**
     * Gets money.
     *
     * @return the money
     */
    public int getMoney() { return money; }

    /**
     * Sets money.
     *
     * @param money the money
     */
    public void setMoney(int money) { this.money = money; }

    /**
     * Add money.
     *
     * @param money the money
     */
    public void addMoney(int money) {
        this.money += money;
    }

    /**
     * Gets item lists.
     *
     * @return the item lists
     */
    public List<Item> getItemLists() {
        return itemLists;
    }

    /**
     * Sets item lists.
     *
     * @param itemLists the item lists
     */
    public void setItemLists(List<Item> itemLists) {
        this.itemLists = itemLists;
    }

    /**
     * Gets mission list.
     *
     * @return the mission list
     */
    public List<Mission> getMissionList() {
        return missionList;
    }

    /**
     * Sets mission list.
     *
     * @param missionList the mission list
     */
    public void setMissionList(List<Mission> missionList) {
        this.missionList = missionList;
    }

    /**
     * Gets animal list.
     *
     * @return the animal list
     */
    public List<Animal> getAnimalList() {
        return animalList;
    }

    /**
     * Sets animal list.
     *
     * @param animalList the animal list
     */
    public void setAnimalList(List<Animal> animalList) {
        this.animalList = animalList;
    }

    /**
     * Gets collection list.
     *
     * @return the collection list
     */
    public List<Collection> getCollectionList() {
        return collectionList;
    }

    /**
     * Sets collection list.
     *
     * @param collectionList the collection list
     */
    public void setCollectionList(List<Collection> collectionList) {
        this.collectionList = collectionList;
    }
}
