package com.lab_team_projects.my_walking_pet.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.lab_team_projects.my_walking_pet.login.User;
import com.lab_team_projects.my_walking_pet.walk_count.Walk;

public class GameManager {
    private static GameManager instance;
    private final User user = new User();
    private Walk walk;


    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void loadUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        /*
        * 아직 프리페어런스 저장되는 코드가 없음
        * 그래서 임의의 사용자 정보를 대입
        * */
        user.setWeight(pref.getFloat("weight", 70.0f));
        user.setHeight(pref.getFloat("height", 177.0f));
        user.setAge(pref.getInt("age", 24));
        user.setGender(pref.getInt("gender", 0));
        user.setBMI();
        user.setMoney(pref.getInt("money", 10000));
    }

    public User getUser() {
        return user;
    }

    public Walk getWalk() {
        return walk;
    }

    public void setWalk(Walk walk) {
        this.walk = walk;
    }
}
