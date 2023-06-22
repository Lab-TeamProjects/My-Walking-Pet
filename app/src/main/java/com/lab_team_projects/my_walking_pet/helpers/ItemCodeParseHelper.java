package com.lab_team_projects.my_walking_pet.helpers;

import android.content.Context;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.home.ItemDetail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ItemCodeParseHelper {
    private final List<ItemDetail> itemDetailList = new ArrayList<>();

    public List<ItemDetail> getItemDetailsList() {
        return itemDetailList;
    }

    public ItemCodeParseHelper(Context context) throws IOException {

        /*
        * csv 파일을 불러와서
        * 형식에 맞게 파싱함
        * 아이템 ID,
        * 아이템 이름,
        * 효과:값&효과:값&효과:값(만약 단일 효과면 그냥 효과:값 으로 표기,
        * 카테고리,
        * 아이템 설명,
        * 아이템 가격
        * */

        InputStream is = context.getResources().openRawResource(R.raw.item);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String line = "";

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");
            ItemDetail itemDetail = new ItemDetail();
            itemDetail.setCode(Integer.parseInt(tokens[0]));
            itemDetail.setName(tokens[1]);

            String[] effects = tokens[2].split("&");
            List<String> effectsList = new ArrayList<>();
            List<Integer> valueList = new ArrayList<>();

            for (String effect : effects) {
                String[] effectTokens = effect.split(":");
                effectsList.add(effectTokens[0]);
                valueList.add(Integer.valueOf(effectTokens[1]));
            }

            itemDetail.setEffects(effectsList);
            itemDetail.setValues(valueList);
            itemDetail.setType(tokens[3]);
            itemDetail.setExplanation(tokens[4]);
            itemDetail.setPrice(Integer.parseInt(tokens[5]));


            itemDetailList.add(itemDetail);
        }

        reader.close();
        is.close();
    }
}
