package com.lab_team_projects.my_walking_pet.home;

import com.google.gson.annotations.SerializedName;

/**
 * 상점에서 구매할 수 있는 아이템을 코드와 개수로 정의하는 클래스
 * 서버와 통신할 때 최소한으로 통신하기 위해 만들었습니다.
 */
public class Item {
    @SerializedName("code")    // json 변환시 name에 해당하는 부분, 변수명이랑 name이 같아서 굳이 없어도 되긴한데 일단 넣어 놨음
    private Integer code;
    @SerializedName("count")
    private Integer count;

    /**
     * Instantiates a new Item.
     *
     * @param code  the code
     * @param count the count
     */
    public Item(Integer code, Integer count) {
        this.code = code;
        this.count = count;
    }

    /**
     * Instantiates a new Item.
     */
    public Item() {
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Sets count.
     *
     * @param count the count
     */
    public void setCount(Integer count) {
        this.count = count;
    }


    /**
     * The enum Item type.
     */
    public enum ItemType {
        /**
         * Drink item type.
         */
        DRINK,
        /**
         * Food item type.
         */
        FOOD,
        /**
         * Wash item type.
         */
        WASH
    }

    /**
     * The enum Item effect.
     */
    public enum ItemEffect {
        /**
         * Hunger item effect.
         */
        HUNGER,
        /**
         * Thirst item effect.
         */
        THIRST,
        /**
         * Cleanliness item effect.
         */
        CLEANLINESS,
        /**
         * Mood item effect.
         */
        MOOD,
        /**
         * Durability item effect.
         */
        DURABILITY
    }
}
