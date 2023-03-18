package com.lab_team_projects.my_walking_pet.helpers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.home.Item;
import com.lab_team_projects.my_walking_pet.home.ItemDetail;
import com.lab_team_projects.my_walking_pet.login.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InventoryHelper {
    private User user = GameManager.getInstance().getUser();

    private Item.ItemType itemType = Item.ItemType.DRINK;
    private Context context;

    private final List<ItemDetail> detailsList;    // 파싱한 아이템 정보 리스트
    private List<Item> items = new ArrayList<>();;
    private int currentItemIndex;

    /*
    * 사용자의 인벤토리에 아이템 저장 및 사용을 도와줌
    * 아이템의 정보는 ItemCodeParseHelper를 통해서
    * csv 파일을 파싱한 후 메모리 상에 적재해놓음
    * */

    public InventoryHelper(String json, Context context) throws IOException {
        this.context = context;

        /*
        * 사용자의 인벤토리 아이템 json 문자열을 클래스 타입에 맞게 대입
        * */
        user.setItemLists(new Gson().fromJson(json, new TypeToken<List<Item>>(){}.getType()));

        /*
        * csv 파일을 파싱하는 클래스
        * */
        ItemCodeParseHelper itemCodeParseHelper = new ItemCodeParseHelper(context);
        detailsList = itemCodeParseHelper.getItemDetailsList();
    }

    public InventoryHelper (Context context) throws IOException {
        this.context = context;
        ItemCodeParseHelper itemCodeParseHelper = new ItemCodeParseHelper(context);
        detailsList = itemCodeParseHelper.getItemDetailsList();
    }




    public void setItemType(Item.ItemType itemType) {
        this.itemType = itemType;
    }

    public String setItemName() {
        /*
        * 상호작용 버튼을 눌렀을 때 나오는 물, 밥, 씻기 버튼을 클릭하면
        * 카테고리에 맞게 유저의 인벤토리 리스트에서 아이템을 가져온다
        * */
        currentItemIndex = 0;   // 초기값은 당연히 리스트의 첫번째로 선택
        items.clear();
        for(Item item : user.getItemLists()) {
            ItemDetail itemDetail = findItem(item.getCode());
            if (itemDetail.getType().equals(itemType.name())) {
                items.add(item);
            }
        }

        if(items.isEmpty()) {
            return "아이템이 없습니다.";
        } else {
            Item firstItem = items.get(currentItemIndex);
            return getItemName(firstItem);
        }

    }

    public ItemDetail findItem(int code) {
        for(ItemDetail itemDetail : detailsList) {
            if (itemDetail.getCode() == code) {
                return itemDetail;
            }
        }
        return null;
    }


    public String setNextItem() {
        /*
        * 현재 선택중인 아이템 에서 다음 버튼을 클릭시
        * 현재 아이템 인덱스가 변경되고 텍스트뷰도 변경
        * */
        if(items.isEmpty()) {
            return "아이템이 없습니다.";
        } else {
            if (currentItemIndex < items.size() - 1) {
                currentItemIndex++;
            } else {
                Toast.makeText(context, "마지막 아이템 입니다.", Toast.LENGTH_SHORT).show();
            }
            Item firstItem = items.get(currentItemIndex);
            return getItemName(firstItem);
        }
    }

    public String setPreviewItem() {
        if(items.isEmpty()) {
            return "아이템이 없습니다.";
        } else {
            if (currentItemIndex > 0) {
                currentItemIndex--;
            } else {
                Toast.makeText(context, "첫 번째 아이템 입니다.", Toast.LENGTH_SHORT).show();
            }
            Item firstItem = items.get(currentItemIndex);
            return getItemName(firstItem);
        }
    }

    @NonNull
    private String getItemName(Item item) {
        return String.format(Locale.getDefault(), "%s x %d개", findItem(item.getCode()).getName(), item.getCount());
    }


    public String useCurrentItem() {
        if (!items.isEmpty()) {
            Item item = items.get(currentItemIndex);
            if (item.getCount() > 0) {
                item.setCount(item.getCount() - 1);
                if (item.getCount() == 0) {
                    user.getItemLists().remove(item);
                    items.remove(currentItemIndex);
                    return "아이템이 없습니다.";
                }
                return getItemName(item);
            }
        }
        return "아이템이 없습니다.";
    }
}
