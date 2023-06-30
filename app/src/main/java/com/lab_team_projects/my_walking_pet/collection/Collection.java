package com.lab_team_projects.my_walking_pet.collection;

import com.lab_team_projects.my_walking_pet.home.Broods;

/**
 * 동물 도감에 대한 클래스
 */
public class Collection {
    private String broodName;
    private Boolean isHave;
    private int lv;

    public Collection(String broodName, Boolean isHave, int lv) {
        this.broodName = broodName;
        this.isHave = isHave;
        this.lv = lv;
    }

    public String getBroodName() {
        return broodName;
    }

    public Collection setBroodName(String broodName) {
        this.broodName = broodName;
        return this;
    }

    public Boolean getHave() {
        return isHave;
    }

    public Collection setHave(Boolean have) {
        isHave = have;
        return this;
    }

    public int getLv() {
        return lv;
    }

    public Collection setLv(int lv) {
        this.lv = lv;
        return this;
    }
}
