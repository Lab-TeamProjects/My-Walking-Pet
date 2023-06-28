package com.lab_team_projects.my_walking_pet.helpers;

import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.home.Animal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 동물 쓰다듬기 쿨타임 시간을 체크하는 클래스
 */
public class AnimalSwipeTimeCheckHelper {
    private Map<String, Long> lastSwipeMap;
    private static final long COOL_TIME = 5 * 1000 * 60;

    public AnimalSwipeTimeCheckHelper() {
        this.lastSwipeMap = new HashMap<>();
    }

    /**
     * 사용자가 화면을 바꿀 때 마다 사용자 스와이프 맵을 재설정합니다.
     */
    public void loadAnimal() {
        List<Animal> animalList = GameManager.getInstance().getUser().getAnimalList();

        for (Animal animal : animalList) {
            if (!lastSwipeMap.containsKey(animal.getName())) {
                lastSwipeMap.put(animal.getName(), 0L);
            }
        }
    }

    public boolean isCoolTimeOver(Animal animal) {
        long currentTime = System.currentTimeMillis();
        long lastSwipeTime = lastSwipeMap.get(animal.getName());

        if ((currentTime - lastSwipeTime >= COOL_TIME)) {
            lastSwipeMap.put(animal.getName(), currentTime);
            return true;
        } else {
            System.out.println("쿨타임 중입니다. " + (COOL_TIME - (currentTime - lastSwipeTime)) + " 밀리초 남았습니다.");
            return false;
        }
    }
}
