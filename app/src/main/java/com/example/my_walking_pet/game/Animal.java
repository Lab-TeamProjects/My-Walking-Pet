package com.example.my_walking_pet.game;

public class Animal {
    private String name;
    private User originalOwner;
    private User currentOwner;
    private Boolean isRunAway;
    private Integer level;
    private Integer clean;
    private Integer hunger;
    private Integer growth;
    private Integer liking;
    private Broods brood;
    private enum Feelings {
        HAPPY,
        NORMAL,
        BAD,
        SICK
    }

    private int levelUp() {
        //growth 수치를 확인해 level을 증가시키는 함수
        return 0;
    }

    private int stateAverage() {
        //clean, hunger, liking 각각의 수치를 비교해서 현재 상태를 반환하는 함수
        return 0;
    }

    private void useItem(int itemCode) {
        //아이템을 사용했을 때, 어떤 아이템인지 판별 후 해당 수치를 변경시키는 함수
    }
}
