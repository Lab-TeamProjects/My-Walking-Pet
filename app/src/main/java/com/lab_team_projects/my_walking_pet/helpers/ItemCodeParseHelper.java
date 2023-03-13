package com.lab_team_projects.my_walking_pet.helpers;

import android.content.Context;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.home.ItemDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ItemCodeParseHelper {
    private List<ItemDetails> itemDetailsList = new ArrayList<>();

    public List<ItemDetails> getItemDetailsList() {
        return itemDetailsList;
    }

    public ItemCodeParseHelper(Context context) throws IOException {

        /*
        * csv 파일을 불러와서
        * 형식에 맞게 파싱함
        * 아이템 ID,
        * 아이템 이름,
        * 효과:값&효과:값&효과:값(만약 단일 효과면 그냥 효과:값 으로 표기,
        * 카테고리,
        * 아이템 설명
        * */

        InputStream is = context.getResources().openRawResource(R.raw.item);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String line = "";

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");
            ItemDetails itemDetails = new ItemDetails();
            itemDetails.setCode(Integer.parseInt(tokens[0]));
            itemDetails.setName(tokens[1]);

            String[] effects = tokens[2].split("&");
            List<String> effectsList = new ArrayList<>();
            List<Integer> valueList = new ArrayList<>();

            for (String effect : effects) {
                String[] effectTokens = effect.split(":");
                effectsList.add(effectTokens[0]);
                valueList.add(Integer.valueOf(effectTokens[1]));
            }

            itemDetails.setEffects(effectsList);
            itemDetails.setValues(valueList);

            itemDetails.setType(tokens[3]);
            itemDetails.setExplanation(tokens[4]);

            itemDetailsList.add(itemDetails);
        }

        reader.close();
        is.close();
    }
}
