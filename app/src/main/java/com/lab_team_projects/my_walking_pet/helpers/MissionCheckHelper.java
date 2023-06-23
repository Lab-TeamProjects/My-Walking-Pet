package com.lab_team_projects.my_walking_pet.helpers;

import static com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper.TodayUserActiveKey;

import android.content.Context;
import android.util.Log;

import com.lab_team_projects.my_walking_pet.home.Item;

/**
 * 사용자가 미션에 해당하는 조건을 만족했을 경우 미션 프래그먼트에서 확인할 수 있도록하는 미션 체크 클래스
 */
public class MissionCheckHelper {
    private final UserPreferenceHelper userPre;

    public MissionCheckHelper(Context context) {
        this.userPre = new UserPreferenceHelper(context, "user_active");
    }

    /**
     * 사용자가 해당 아잍메을 사용했다고 알립니다.
     * @param type 아이템 타입
     */
    public void useItem(String type) {
        if (type.equals(Item.ItemType.FOOD.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.food, userPre.loadActiveValue(TodayUserActiveKey.food) + 1);
        } else if (type.equals(Item.ItemType.DRINK.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.drink, userPre.loadActiveValue(TodayUserActiveKey.drink) + 1);
        } else if (type.equals(Item.ItemType.WASH.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.wash, userPre.loadActiveValue(TodayUserActiveKey.wash) + 1);
        }
    }

    /**
     * 사용자가 사용한 아이템 개수를 알립니다.
     * @param type
     * @param count
     */
    public void completeMission(String type, int count) {
        if (type.equals(Item.ItemType.FOOD.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.food, getCount(Item.ItemType.FOOD.name()) - count);
        } else if (type.equals(Item.ItemType.DRINK.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.drink, getCount(Item.ItemType.DRINK.name()) - count);
        } else if (type.equals(Item.ItemType.WASH.name())) {
            userPre.saveActiveValue(TodayUserActiveKey.wash, getCount(Item.ItemType.WASH.name()) - count);
        }
    }

    /**
     * 개수를 반환받습니다.
     * @param type
     * @return
     */
    public int getCount(String type) {
        if (type.equals(Item.ItemType.FOOD.name())) {
            return userPre.loadActiveValue(TodayUserActiveKey.food);
        } else if (type.equals(Item.ItemType.DRINK.name())) {
            return userPre.loadActiveValue(TodayUserActiveKey.drink);
        } else {
            return userPre.loadActiveValue(TodayUserActiveKey.wash);
        }
    }

    /**
     * 미션 아이템에 해당하는 비율을 계산하여 반환받습니다.
     * @param type
     * @return 미션 달성 비율
     */
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
