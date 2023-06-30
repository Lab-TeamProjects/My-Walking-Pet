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
}
