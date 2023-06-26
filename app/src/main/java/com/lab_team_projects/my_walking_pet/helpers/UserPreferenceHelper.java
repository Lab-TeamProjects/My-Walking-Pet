package com.lab_team_projects.my_walking_pet.helpers;

import static com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper.UserPreferenceKey.age;
import static com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper.UserPreferenceKey.gender;
import static com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper.UserPreferenceKey.height;
import static com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper.UserPreferenceKey.money;
import static com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper.UserPreferenceKey.weight;

import android.content.Context;
import android.content.SharedPreferences;

import com.lab_team_projects.my_walking_pet.login.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 간단한 데이터를 앱 내부에 저장하기 위한 클래스
 */
public class UserPreferenceHelper {
    /*
    * 해당 열거형의 이름은 임시로 저렇게 만들었음
    * */

    /**
     * The enum User token key.
     */
    public enum UserTokenKey {
        /**
         * Login user token key.
         */
        login,
        /**
         * Refresh user token key.
         */
        refresh
    }

    /**
     * The enum User preference key.
     */
    public enum UserPreferenceKey {
        /**
         * Weight user preference key.
         */
        weight,
        /**
         * Height user preference key.
         */
        height,
        /**
         * Age user preference key.
         */
        age,
        /**
         * Gender user preference key.
         */
        gender,
        /**
         * Money user preference key.
         */
        money
    }

    /**
     * The enum Today user active key.
     */
    public enum TodayUserActiveKey {
        /**
         * Walk today user active key.
         */
        walk,
        /**
         * Food today user active key.
         */
        food,
        /**
         * Drink today user active key.
         */
        drink,
        /**
         * Wash today user active key.
         */
        wash
    }

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    /**
     * Instantiates a new User preference helper.
     *
     * @param context the context
     * @param name    the name
     */
    public UserPreferenceHelper(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Save user info.
     *
     * @param hashMap the hash map
     */
    public void saveUserInfo(HashMap<String, Float> hashMap) {
        /*
        * 유저의 정보를 담고있는 Hash Map을 받아서
        * shared preferences를 이용해 기기에 저장한다
        * */
        for(HashMap.Entry<String, Float> entry : hashMap.entrySet()) {
            editor.putFloat(entry.getKey(), entry.getValue()).commit();
        }
    }

    /**
     * Save user token.
     *
     * @param key   the key
     * @param token the token
     */
    /*
    * 유저 토큰을 키로 저장
    * */
    public void saveUserToken(UserTokenKey key, String token) {
        editor.putString(key.name(), token).commit();
    }

    /**
     * Load user token string.
     *
     * @param key the key
     * @return the string
     */
    public String loadUserToken(UserTokenKey key) {
        return sharedPreferences.getString(key.name(), "");

    }


    /**
     * Save active value.
     *
     * @param key   the key
     * @param value the value
     */
    /*
    * 유저가 오늘 동작했던거 저장
    * */
    public void saveActiveValue(TodayUserActiveKey key, int value) {
        editor.putInt(key.name(), value).commit();
    }

    /**
     * Load active value int.
     *
     * @param key the key
     * @return the int
     */
    public int loadActiveValue(TodayUserActiveKey key) {
        return sharedPreferences.getInt(key.name(), 0);
    }


    /**
     * Save int value.
     *
     * @param key   the key
     * @param value the value
     */
    public void saveIntValue(String key, int value) {
        editor.putInt(key, value).apply();
    }

    /**
     * Load int value int.
     *
     * @param key the key
     * @return the int
     */
    public int loadIntValue(String key) {
        return sharedPreferences.getInt(key, 0);
    }


    /**
     * Load user info.
     *
     * @param user the user
     */
    public void loadUserInfo(User user) {
        List<Double> values = new ArrayList<>();

        /*
        * shared preferences에 저장되어있는 값을 values 리스트에 저장한다
        * */
        UserPreferenceKey[] keys = UserPreferenceKey.values();
        for (UserPreferenceKey key : keys) {
            values.add((double) sharedPreferences.getFloat(key.name(), 0.0f));
        }

        /*
        * enum 순서대로 리스트에 저장했기 때문에 ordinal 메소드를 사용해서
        * user 객체에 변수를 설정한다
        * */
        user.setWeight(values.get(weight.ordinal()));
        user.setHeight(values.get(height.ordinal()));
        user.setAge(values.get(age.ordinal()).intValue());
        user.setGender(values.get(gender.ordinal()).intValue());
        user.setBMI();
        user.setMoney(values.get(money.ordinal()).intValue());
    }
}
