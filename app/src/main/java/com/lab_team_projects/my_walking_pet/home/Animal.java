package com.lab_team_projects.my_walking_pet.home;

/**
 * 동물 클래스
 * 사용자가 동물을 키울 수 있도록 동물에 대한 객체입니다.
 */
public class Animal {
    /**
     * 동물 이름
     */
    private String name;
    /**
     * 원래 주인 UID
     */
    private String originalOwnerUid;
    /**
     * 현재 주인 UID
     */
    private String currentOwnerUid;
    /**
     * 가출 상태 여부
     */
    private Integer isRunAway;
    /**
     * 현재 레벨
     */
    private Integer level;
    /**
     * 청결도
     */
    private Integer clean;
    /**
     * 허기
     */
    private Integer hunger;
    /**
     * 성장치
     */
    private float growth;
    /**
     * 성장치 콜백 리스너
     */
    private GrowthCallback growthCallback;
    /**
     * 갈증
     */
    private Integer thirsty;
    /**
     * 호감도
     */
    private Integer liking;    // 이게 기분인가????
    /**
     * 동물 종족
     */
    private String brood;
    /**
     * 최대 성장치
     */
    private float maxGrowth;

    /**
     * The enum Feelings.
     */
    public enum Feelings {
        /**
         * Happy feelings.
         */
        HAPPY,
        /**
         * Normal feelings.
         */
        NORMAL,
        /**
         * Bad feelings.
         */
        BAD,
        /**
         * Sick feelings.
         */
        SICK
    }

    /**
     * Instantiates a new Animal.
     *
     * @param name             the name
     * @param brood            the brood
     * @param originalOwnerUid the original owner uid
     * @param currentOwnerUid  the current owner uid
     */
    public Animal(String name, String brood, String originalOwnerUid, String currentOwnerUid) {
        this.name = name;
        this.brood = brood;
        this.originalOwnerUid = originalOwnerUid;
        this.currentOwnerUid = currentOwnerUid;
        this.isRunAway = 0;
        this.level = 2;
        this.clean = 50;
        this.hunger = 50;
        this.thirsty = 50;
        this.growth = 0;
        this.liking = 0;
        this.maxGrowth = 3000 + (3000 * 0.3f * this.level);
    }

    /**
     * 사용자가 걷게되면 리스너가 동작하고 onCall() 메서드를 호출합니다.
     * 아래는 리스너 관련 메서드입니다.
     */
    public interface GrowthCallback {
        /**
         * On call.
         */
        void onCall();
    }

    /**
     * Sets growth callback.
     *
     * @param growthCallback the growth callback
     */
    public void setGrowthCallback(GrowthCallback growthCallback) {
        this.growthCallback = growthCallback;
    }

    /**
     * Gets growth callback.
     *
     * @return the growth callback
     */
    public GrowthCallback getGrowthCallback() {
        return growthCallback;
    }

    /**
     * 사용자가 걸을 때 성장치가 얼만큼 증가할지 판단하는 메서드입니다.
     *
     * @return 현재 배고픔, 목마름, 청결의 수치의 평균을 반환합니다.
     */
    public float getStateAverage() {
        //clean, hunger, liking 각각의 수치를 비교해서 현재 상태를 반환하는 함수
        return (clean + hunger + thirsty) / 3.0f;
    }

    /**
     * 인벤토리에서 아이템을 사용하려고 할 때 동물에게 적용하는 메서드입니다.
     *
     * @param itemDetail 사용하는 아이템의 자세한 정보를 갖고 있는 객체
     */
    public void useItem(ItemDetail itemDetail) {
        for (int i = 0; i < itemDetail.getEffects().size(); i++) {
            String effect = itemDetail.getEffects().get(i);
            int value = itemDetail.getValues().get(i);

            if (effect.equals(Item.ItemEffect.HUNGER.name())) {
                hunger = adjustValue(hunger, value);
            } else if (effect.equals(Item.ItemEffect.THIRST.name())) {
                thirsty = adjustValue(thirsty, value);
            } else if (effect.equals(Item.ItemEffect.MOOD.name())) {
                liking = adjustValue(liking, value);
            } else if (effect.equals(Item.ItemEffect.CLEANLINESS.name())) {
                clean = adjustValue(clean, value);
            }
        }
    }

    /**
     * 아이템을 사용할 때 이전 값에 새로운 값을 더합니다.
     *
     * @param originalValue 원래 동물이 갖고 있던 수치
     * @param addedValue    사용하는 아이템의 수치
     * @return 더한 값을 반환합니다.
     */

    private int adjustValue(int originalValue, int addedValue) {
        int value = originalValue + addedValue;
        if (value > 100) {
            value = 100;
        } else if (value < 0) {
            value = 0;
        }
        return value;
    }

    /**
     * 성장치 조건을 확인하여 동물의 레벨을 증가시킵니다.
     * @return 레벨업 여부를 반환
     */
    private int levelUp() {
        //growth 수치를 확인해 level을 증가시키는 함수
        return 0;
    }

    /**
     * Gets thirsty.
     *
     * @return the thirsty
     */
    public Integer getThirsty() {
        return thirsty;
    }

    /**
     * Sets thirsty.
     *
     * @param thirsty the thirsty
     */
    public void setThirsty(Integer thirsty) {
        this.thirsty = thirsty;
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
     * Gets original owner uid.
     *
     * @return the original owner uid
     */
    public String getOriginalOwnerUid() {
        return originalOwnerUid;
    }

    /**
     * Sets original owner uid.
     *
     * @param originalOwnerUid the original owner uid
     */
    public void setOriginalOwnerUid(String originalOwnerUid) {
        this.originalOwnerUid = originalOwnerUid;
    }

    /**
     * Gets current owner uid.
     *
     * @return the current owner uid
     */
    public String getCurrentOwnerUid() {
        return currentOwnerUid;
    }

    /**
     * Sets current owner uid.
     *
     * @param currentOwnerUid the current owner uid
     */
    public void setCurrentOwnerUid(String currentOwnerUid) {
        this.currentOwnerUid = currentOwnerUid;
    }

    /**
     * Gets is run away.
     *
     * @return the is run away
     */
    public Integer getIsRunAway() {
        return isRunAway;
    }

    /**
     * Sets is run away.
     *
     * @param isRunAway the is run away
     */
    public void setIsRunAway(Integer isRunAway) {
        this.isRunAway = isRunAway;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Sets level.
     *
     * @param level the level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * Gets clean.
     *
     * @return the clean
     */
    public Integer getClean() {
        return clean;
    }

    /**
     * Sets clean.
     *
     * @param clean the clean
     */
    public void setClean(Integer clean) {
        this.clean = clean;
    }

    /**
     * Gets hunger.
     *
     * @return the hunger
     */
    public Integer getHunger() {
        return hunger;
    }

    /**
     * Sets hunger.
     *
     * @param hunger the hunger
     */
    public void setHunger(Integer hunger) {
        this.hunger = hunger;
    }

    /**
     * Gets growth.
     *
     * @return the growth
     */
    public float getGrowth() {
        return growth;
    }

    /**
     * Sets growth.
     *
     * @param growth the growth
     */
    public void setGrowth(float growth) {
        this.growth = growth;
    }

    /**
     * Gets liking.
     *
     * @return the liking
     */
    public Integer getLiking() {
        return liking;
    }

    /**
     * Sets liking.
     *
     * @param liking the liking
     */
    public void setLiking(Integer liking) {
        this.liking = liking;
    }

    /**
     * Gets brood.
     *
     * @return the brood
     */
    public String getBrood() {
        return brood;
    }

    /**
     * Sets brood.
     *
     * @param brood the brood
     */
    public void setBrood(String brood) {
        this.brood = brood;
    }

    /**
     * Gets max growth.
     *
     * @return the max growth
     */
    public float getMaxGrowth() {
        return maxGrowth;
    }

    /**
     * Sets max growth.
     *
     * @param maxGrowth the max growth
     */
    public void setMaxGrowth(float maxGrowth) {
        this.maxGrowth = maxGrowth;
    }
}
