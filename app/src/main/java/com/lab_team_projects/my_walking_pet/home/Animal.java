package com.lab_team_projects.my_walking_pet.home;

public class Animal {
    private String name;
    private String originalOwnerUid;
    private String currentOwnerUid;
    private Integer isRunAway;
    private Integer level;
    private Integer clean;
    private Integer hunger;
    private float growth;
    private GrowthCallback growthCallback;
    private Integer thirsty;
    private Integer liking;    // 이게 기분인가????
    private String brood;
    private float maxGrowth;
    public enum Feelings {
        HAPPY,
        NORMAL,
        BAD,
        SICK
    }

    public Animal(String name, String brood, String originalOwnerUid, String currentOwnerUid) {
        this.name = name;
        this.brood = brood;
        this.originalOwnerUid = originalOwnerUid;
        this.currentOwnerUid = currentOwnerUid;
        this.isRunAway = 0;
        this.level = 0;
        this.clean = 50;
        this.hunger = 50;
        this.thirsty = 50;
        this.growth = 0;
        this.liking = 0;
        this.maxGrowth = 3000 + (3000 * 0.3f * this.level);
    }

    public interface GrowthCallback {
        void onCall();
    }

    public void setGrowthCallback(GrowthCallback growthCallback) {
        this.growthCallback = growthCallback;
    }

    public GrowthCallback getGrowthCallback() {
        return growthCallback;
    }

    private int levelUp() {
        //growth 수치를 확인해 level을 증가시키는 함수
        return 0;
    }

    public float getStateAverage() {
        //clean, hunger, liking 각각의 수치를 비교해서 현재 상태를 반환하는 함수
        return (clean + hunger + thirsty) / 3.0f;
    }

    public void useItem(ItemDetail itemDetail) {
        //아이템을 사용했을 때, 어떤 아이템인지 판별 후 해당 수치를 변경시키는 함수

        /*
        * 그 아이템의 효과별로 사용
        * */
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

    private int adjustValue(int originalValue, int addedValue) {
        int value = originalValue + addedValue;
        if (value > 100) {
            value = 100;
        } else if (value < 0) {
            value = 0;
        }
        return value;
    }

    public Integer getThirsty() {
        return thirsty;
    }

    public void setThirsty(Integer thirsty) {
        this.thirsty = thirsty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalOwnerUid() {
        return originalOwnerUid;
    }

    public void setOriginalOwnerUid(String originalOwnerUid) {
        this.originalOwnerUid = originalOwnerUid;
    }

    public String getCurrentOwnerUid() {
        return currentOwnerUid;
    }

    public void setCurrentOwnerUid(String currentOwnerUid) {
        this.currentOwnerUid = currentOwnerUid;
    }

    public Integer getIsRunAway() {
        return isRunAway;
    }

    public void setIsRunAway(Integer isRunAway) {
        this.isRunAway = isRunAway;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getClean() {
        return clean;
    }

    public void setClean(Integer clean) {
        this.clean = clean;
    }

    public Integer getHunger() {
        return hunger;
    }

    public void setHunger(Integer hunger) {
        this.hunger = hunger;
    }

    public float getGrowth() {
        return growth;
    }

    public void setGrowth(float growth) {
        this.growth = growth;
    }

    public Integer getLiking() {
        return liking;
    }

    public void setLiking(Integer liking) {
        this.liking = liking;
    }

    public String getBrood() {
        return brood;
    }

    public void setBrood(String brood) {
        this.brood = brood;
    }

    public float getMaxGrowth() {
        return maxGrowth;
    }

    public void setMaxGrowth(float maxGrowth) {
        this.maxGrowth = maxGrowth;
    }
}
