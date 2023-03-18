package com.lab_team_projects.my_walking_pet.home;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("code")    // json 변환시 name에 해당하는 부분, 변수명이랑 name이 같아서 굳이 없어도 되긴한데 일단 넣어 놨음
    private Integer code;
    @SerializedName("count")
    private Integer count;

    public Item(Integer code, Integer count) {
        this.code = code;
        this.count = count;
    }

    public Item() {
    }

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


    public enum ItemType {
        DRINK, FOOD, WASH
    }

    public enum ItemEffect {
        HUNGER, THIRST, CLEANLINESS, MOOD, DURABILITY
    }
}
