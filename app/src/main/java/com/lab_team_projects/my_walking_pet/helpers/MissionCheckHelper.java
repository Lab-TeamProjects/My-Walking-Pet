package com.lab_team_projects.my_walking_pet.helpers;

import static com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper.*;

import android.content.Context;
import android.util.Log;

import com.lab_team_projects.my_walking_pet.home.Item;

public class MissionCheckHelper {
    private UserPreferenceHelper userPre;

    public MissionCheckHelper(Context context) {
        this.userPre = new UserPreferenceHelper(context, "user_active");

    }

    public void useItem(String type) {
        if (type.equals(Item.ItemType.FOOD.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.food, userPre.loadActiveValue(TodayUserActiveKey.food) + 1);
        } else if (type.equals(Item.ItemType.DRINK.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.drink, userPre.loadActiveValue(TodayUserActiveKey.drink) + 1);
        } else if (type.equals(Item.ItemType.WASH.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.wash, userPre.loadActiveValue(TodayUserActiveKey.wash) + 1);
        }
    }

    public void completeMission(String type, int count) {
        if (type.equals(Item.ItemType.FOOD.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.food, userPre.loadActiveValue(TodayUserActiveKey.food) - count);
        } else if (type.equals(Item.ItemType.DRINK.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.drink, userPre.loadActiveValue(TodayUserActiveKey.drink) - count);
        } else if (type.equals(Item.ItemType.WASH.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.wash, userPre.loadActiveValue(TodayUserActiveKey.wash) - count);
        }
    }

    public int getRatio(String type) {
        int food = userPre.loadActiveValue(TodayUserActiveKey.food);
        int wash = userPre.loadActiveValue(TodayUserActiveKey.wash);
        int drink = userPre.loadActiveValue(TodayUserActiveKey.drink);


        if (type.equals(Item.ItemType.FOOD.name())) {
            return Math.min((food / 3) * 100, 100);
        } else if (type.equals(Item.ItemType.DRINK.name())) {
            return Math.min((drink / 3) * 100, 100);
        } else {
            return 0;
        }
    }

    public void resetToday() {
        userPre.saveActiveValue(TodayUserActiveKey.food, 0);
        userPre.saveActiveValue(TodayUserActiveKey.drink, 0);
        userPre.saveActiveValue(TodayUserActiveKey.wash, 0);
    }
}
