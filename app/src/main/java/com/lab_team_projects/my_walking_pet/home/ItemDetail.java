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

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets effects.
     *
     * @return the effects
     */
    public List<String> getEffects() {
        return effects;
    }

    /**
     * Sets effects.
     *
     * @param effects the effects
     */
    public void setEffects(List<String> effects) {
        this.effects = effects;
    }

    /**
     * Gets values.
     *
     * @return the values
     */
    public List<Integer> getValues() {
        return values;
    }

    /**
     * Sets values.
     *
     * @param values the values
     */
    public void setValues(List<Integer> values) {
        this.values = values;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets explanation.
     *
     * @return the explanation
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * Sets explanation.
     *
     * @param explanation the explanation
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(int price) {
        this.price = price;
    }
}