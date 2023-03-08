package com.lab_team_projects.my_walking_pet.home;

public class Item {
    private Integer code;
    private Integer count;

    enum Type {
        FOOD, SNACK, CLEAN
    }

    enum Effect {
        HUNGER, THIRST, CLEANLINESS, MOOD, DURABILITY
    }
}
