package com.lab_team_projects.my_walking_pet.home;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("code")
    private Integer code;
    @SerializedName("count")
    private Integer count;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public enum ItemSelect {
        WATER, FOOD, WASH
    }

    public enum ItemType {
        FOOD, SNACK, CLEAN
    }

    public enum ItemEffect {
        HUNGER, THIRST, CLEANLINESS, MOOD, DURABILITY
    }
}
