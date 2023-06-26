package com.lab_team_projects.my_walking_pet.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.lab_team_projects.my_walking_pet.login.User;
import com.lab_team_projects.my_walking_pet.walk_count.Walk;

/**
 * 앱의 매니저 역할을 하는 클래스
 * 싱글톤 객체로 구현되어 있으며
 * 유저 정보와 일일 걸음 정보에 해당하는 객체를 멤버 변수로 갖고 있습니다.
 */
public class GameManager {
    private static GameManager instance;
    private final User user = new User();
    private Walk walk;


    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * 서버와 동기화 하여 유저 정보를 앱에 설정합니다.
     *
     * @param context the context
     */
    public void loadUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        /*
        * 아직 프리페어런스 저장되는 코드가 없음
        * 그래서 임의의 사용자 정보를 대입
        * ****서버에서 가져오는 코드로 바꿔야 함*****
        */
        user.setWeight(pref.getFloat("weight", 70.0f));
        user.setHeight(pref.getFloat("height", 177.0f));
        user.setAge(pref.getInt("age", 24));
        user.setGender(pref.getInt("gender", 0));
        user.setBMI();
        user.setMoney(pref.getInt("money", 10000));
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets walk.
     *
     * @return the walk
     */
    public Walk getWalk() {
        return walk;
    }

    /**
     * Sets walk.
     *
     * @param walk the walk
     */
    public void setWalk(Walk walk) {
        this.walk = walk;
    }
}
