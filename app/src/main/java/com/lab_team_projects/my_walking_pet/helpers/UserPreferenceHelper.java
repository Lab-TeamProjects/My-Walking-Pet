package com.lab_team_projects.my_walking_pet.helpers;

import static com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper.UserPreferenceKey.*;
import static com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper.UserPreferenceKey.age;

import android.content.Context;
import android.content.SharedPreferences;

import com.lab_team_projects.my_walking_pet.login.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserPreferenceHelper {

    public enum UserPreferenceKey {
        weight, height, age, gender
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserPreferenceHelper(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserInfo(HashMap<String, Float> hashMap) {
        /*
        * 유저의 정보를 담고있는 Hash Map을 받아서
        * shared preferences를 이용해 기기에 저장한다
        * */
        for(HashMap.Entry<String, Float> entry : hashMap.entrySet()) {
            editor.putFloat(entry.getKey(), entry.getValue());
        }
    }

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
    }
}
