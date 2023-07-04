package com.lab_team_projects.my_walking_pet.app;


import android.os.Handler;

import com.lab_team_projects.my_walking_pet.home.Animal;
import com.lab_team_projects.my_walking_pet.login.User;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 앱이 켜져있을 때 시간 주기 및 걸음 주기 마다
 * 현재 선택된 동물의 욕구 수치를 감소시키기 위한 싱글톤 클래스
 *
 * 앱이 실행되면 쓰레드로 일정 주기마다 시간과 걸음 수를 감지한다.
 * 해당 동물의 감소된 마지막 시간과 현재 시간을 판단하여 수치를 감소시키고
 * 걸음 주기로 감소된 마지막 걸음 수와 현재 걸음 수를 판단하여 수치를 소시킨다.
 */
public class AnimalMeasureManager {
    private Map<String, LocalDateTime> animalFigureChangeLastTimeMap;

    private AnimalMeasureManager() {
        // private constructor to prevent instantiation
    }

    /**
     * Bill Pugh Singleton 디자인 패턴
     */
    private static class Holder {
        private static final AnimalMeasureManager INSTANCE = new AnimalMeasureManager();
    }

    /**
     * 싱글톤 객체에 접근하는 유일한 메서드
     * @return 싱글톤 인스턴스를 반환
     */
    public static AnimalMeasureManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 서버에서 마지막 종료 시간을 받아와서 동물들의 욕구 수치를 변화시킨다.
     *
     * ##
     * 현재 구상중인 방법
     *
     * 서버로부터 시간을 받는다
     * 받아온 시간이 현재 시간보다 이전일텐데 1~2분을 대략 a라고함 (수치마다 주기가 다르기 때문에)
     *
     * 받아온 시간에 a를 더하고 a 시간에 해당하는 수치를 감소시킨다.
     * 이걸 받아온 시간이 현재 시간보다 클때까지 반복한다
     *
     * 단, 수치가 0이면 반복문 종료
     *
     */
    public void initServerLastTime(String lastTime) {
        User user = GameManager.getInstance().getUser();

        // 현재 시간 불러옴
        LocalDateTime now = LocalDateTime.now();

        // 시간 문자열 형식화
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        // 서버로부터 받은 마지막 시간을 LocalDateTime 객체로 변환
        LocalDateTime last = LocalDateTime.parse(lastTime, dtf);

        long differenceInMinutes;
        do {
            // now와 last 사이의 시간 차를 구함 (분 단위)
            differenceInMinutes = Duration.between(last, now).toMinutes();

            // 동물 리스트의 모든 동물의 수치가 0이면 중간이 반복문 중지하기 위한 플래그 배열
            Boolean[] allFigureZeroFlags = new Boolean[user.getAnimalList().size()];

            // 유저 동물들의 수치를 감소시킴
            List<Animal> animalList = user.getAnimalList();
            for (int i = 0, animalListSize = animalList.size(); i < animalListSize; i++) {
                Animal animal = animalList.get(i);
                animal.setLiking(animal.getLiking() - 10);
                animal.setHunger(animal.getHunger() - 5);
                animal.setClean(animal.getClean() - 5);

                animalFigureChangeLastTimeMap.put(animal.getId(), now);
                if (animal.getLiking() == 0 && animal.getHunger() == 0 && animal.getClean() == 0) {
                    allFigureZeroFlags[i] = true;
                }
            }

            // 모든 동물의 수치가 0이면 반복문 종료
            boolean isFlagAllTrue = Arrays.stream(allFigureZeroFlags).allMatch(val -> val);
            if (isFlagAllTrue) {
                break;
            }

            last = last.plusMinutes(10);
        }
        while (!(differenceInMinutes >= 10));   // 시간 차가 10분 이하일때까지 반복문 동작


    }

    /**
     * 일정 주기마다 동물 욕구 수치감소 시간을 판단하여 수치를 감소시키고
     * 사용자의 걸음 수를 판단하여 수치를 감소시킨다.
     *
     * 현재 주기 : 10초
     */
    public void startChecker() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {





                handler.postDelayed(this, 10000);
            }
        };
        handler.post(runnable);
    }
}
