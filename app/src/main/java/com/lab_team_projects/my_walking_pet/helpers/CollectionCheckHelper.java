package com.lab_team_projects.my_walking_pet.helpers;

import android.util.Log;

import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.collection.Collection;
import com.lab_team_projects.my_walking_pet.home.Animal;
import com.lab_team_projects.my_walking_pet.home.Broods;
import com.lab_team_projects.my_walking_pet.login.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 사용자가 키운 동물을 도감에 등록하기 위한 도감 등록 체크 헬퍼 클래스
 */
public class CollectionCheckHelper {

    /**
     * 종족별 매핑이 되어있는 도감 아이템으로 완전 비어있는 도감 리스트를 반환합니다.
     * @return 만들어진 도감 리스트를 반환
     */
    public List<Collection> getInitCollectionList() {
        List<Collection> newList = new ArrayList<>();
        Broods[] broods = Broods.values();

        for (Broods brood : broods) {
            if (brood.equals(Broods.Test)) {
                continue;
            }

            for (int i = 1; i<=3; i++) {
                Collection newCollection = new Collection(brood.name(), false, i);
                newList.add(newCollection);
            }
        }

        while (newList.size() != 12) {
            newList.add(new Collection("Test", false, 1));
        }

        return newList;
    }


    /**
     * 유저가 가진 동물이 진화, 구입 등등으로 동물 리스트에 변화가 생기는 코드에서 호출하여
     * 유저의 도감 리스트에도 변화를 주는 메서드
     * @param animal 변화가 이뤄진 동물
     */
    public void addCollectionToUser(Animal animal) {
        User user = GameManager.getInstance().getUser();

        // 유저의 도감 리스트에서 매개변수로 들어온 동물의 종족과 레벨이 같은 도감 클래스를 찾는다.
        Optional<Collection> newCollection = user.getCollectionList().stream()
                .filter(c -> c.getBroodName().equals(animal.getBrood()) && c.getLv() == animal.getLevel())
                .findFirst();

        if (newCollection.isPresent()) {
            // optional 클래스로부터 찾은 객체 원본을 받는다.
            Collection element = newCollection.get();

            // 찾은 도감의 인덱스 번호를 받는다.
            int index = user.getCollectionList().indexOf(element);

            // 유저에 도감을 등록한다.
            element.setHave(true);
            user.getCollectionList().set(index, element);


        } else {
            // 도감 원본을 찾을 수 없음 에러처리
        }

    }
}
