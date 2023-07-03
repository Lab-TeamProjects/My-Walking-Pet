package com.lab_team_projects.my_walking_pet.helpers;


import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.home.Broods;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 동물 종족과 레벨에 해당하는 이미지 등등을 매핑해놓은 헬퍼 클래스
 */
public class AnimalMappingHelper {
    private final Map<Broods, int[]> broodsImgMap = Map.of(
            Broods.CAT, new int[]{R.drawable.img_egg_black, R.drawable.img_pet_cat_1, R.drawable.img_pet_cat_2, R.drawable.img_pet_cat_3}
            , Broods.DOG, new int[]{R.drawable.img_egg_blue, R.drawable.img_pet_cat_1, R.drawable.img_pet_cat_2, R.drawable.img_pet_cat_3}
            , Broods.MONKEY, new int[]{R.drawable.img_egg_white, R.drawable.img_pet_cat_1, R.drawable.img_pet_cat_2, R.drawable.img_pet_cat_3}
            , Broods.HAMSTER, new int[]{R.drawable.img_egg_yellow, R.drawable.img_pet_cat_1, R.drawable.img_pet_cat_2, R.drawable.img_pet_cat_3}
            , Broods.Test, new int[]{R.drawable.img_egg_yellow, R.drawable.img_pet_cat_1, R.drawable.img_pet_cat_2, R.drawable.img_pet_cat_3});

    private final Map<Broods, String> broodsNameMap =  Map.of(
            Broods.CAT, "고양이",
            Broods.DOG, "강아지",
            Broods.MONKEY, "원숭이",
            Broods.HAMSTER, "햄스터",
            Broods.Test, "테스트");


    /**
     * 종족명과 레벨을 넣으면 알맞은 그림을 반환합니다.
     * @param broodName 종족 이름 (str)
     * @param lv 레벨 (int)
     * @return 매핑된 이미지 id (int)
     */
    public int getAnimalImg(String broodName, int lv) {
        int[] values = getImgValue(Broods.valueOf(broodName));
        return values[lv];
    }


    /**
     * 종족과 이미지가 매핑되어있는 Map 객체를 반환합니다.
     * @return broodsImgMap
     */
    public Map<Broods, int[]> getBroodsImgMap() {
        return broodsImgMap;
    }

    /**
     * key에 해당하는 종족을 받아서 value를 반환합니다.
     * @param broods key
     * @return int[] value
     */
    public int[] getImgValue(Broods broods) {
        return broodsImgMap.get(broods);
    }

    /**
     * 알을 제외한 나머지 매핑된 value를 반환합니다.
     * @param broods 종족 key
     * @return int[] 단, egg를 제외한 나머지
     */
    public int[] getImgValueNoneEgg(Broods broods) {
        int[] originalValue = getImgValue(broods);

        return Arrays.copyOfRange(originalValue, 1, originalValue.length);
    }

    /**
     * 종족과 한글 이름으로 매핑된 Map 클래스를 반환
     * @return Map 클래스
     */
    public Map<Broods, String> getBroodsNameMap() {
        return broodsNameMap;
    }

    /**
     * enum 클래스 종족에 대한 매핑된 이름을 String으로 반환합니다.
     * @param broods 종족 enum
     * @return 이름 반환
     */
    public String getBroodsName(Broods broods) {
        return getBroodsNameMap().get(broods);
    }

}
