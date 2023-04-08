package com.lab_team_projects.my_walking_pet.helpers;

import static com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper.TodayUserActiveKey;

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
            userPre.saveActiveValue(TodayUserActiveKey.food, getCount(Item.ItemType.FOOD.name()) - count);
        } else if (type.equals(Item.ItemType.DRINK.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.drink, getCount(Item.ItemType.DRINK.name()) - count);
        } else if (type.equals(Item.ItemType.WASH.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.wash, getCount(Item.ItemType.WASH.name()) - count);
        }
    }

    public int getCount(String type) {
        if (type.equals(Item.ItemType.FOOD.name())) {
            return userPre.loadActiveValue(TodayUserActiveKey.food);
        } else if (type.equals(Item.ItemType.DRINK.name())) {
            return userPre.loadActiveValue(TodayUserActiveKey.drink);
        } else {
            return userPre.loadActiveValue(TodayUserActiveKey.wash);
        }
    }

    public int getRatio(String type) {
        int food = getCount(Item.ItemType.FOOD.name());
        int drink = getCount(Item.ItemType.DRINK.name());
        int wash = getCount(Item.ItemType.WASH.name());

        Log.d("__walk", food + " " + drink);

        if (type.equals(Item.ItemType.FOOD.name())) {
            return Math.min((int)((food / 3.0) * 100), 100);
        } else if (type.equals(Item.ItemType.DRINK.name())) {
            return Math.min((int)((drink / 3.0) * 100), 100);
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
