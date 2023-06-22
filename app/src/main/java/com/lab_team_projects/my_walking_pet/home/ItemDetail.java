package com.lab_team_projects.my_walking_pet.home;

import java.util.List;

/**
 * 실제 앱에서 사용되는 아이템의 자세한 정보를 갖고있는 클래스
 * 아이템 코드와 효과 등등이 정의됩니다.
 */
public class ItemDetail {
    private int code;
    private String name;
    private List<String> effects;
    private List<Integer> values;
    private String type;
    private String explanation;
    private int price;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEffects() {
        return effects;
    }

    public void setEffects(List<String> effects) {
        this.effects = effects;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}