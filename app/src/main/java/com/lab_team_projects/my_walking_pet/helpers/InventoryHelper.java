package com.lab_team_projects.my_walking_pet.helpers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.home.Animal;
import com.lab_team_projects.my_walking_pet.home.Item;
import com.lab_team_projects.my_walking_pet.home.ItemDetail;
import com.lab_team_projects.my_walking_pet.login.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 사용자가 아이템을 사용하여 동물과 상호작용 할 수 있도록 하는 인벤토리 클래스
 */
public class InventoryHelper {
    private final User user = GameManager.getInstance().getUser();

    private Item.ItemType itemType = Item.ItemType.DRINK;
    private final Context context;

    /**
     * 아이템 정보에 해당하는 csv 파일을 파싱하여 자세한 아이템 정보를 저장하는 리스트
     */
    private final List<ItemDetail> detailsList;    // 파싱한 아이템 정보 리스트
    private final List<Item> items = new ArrayList<>(); // 현재 카테고리 아이템 리스트
    private int currentItemIndex;

    /**
     * 사용자가 아이템을 사용할 때 감지되는 리스너 클래스
     */
    private ItemUsingListener itemUsingListener = null;
    public void setItemUsingListener(ItemUsingListener itemUsingListener) {
        this.itemUsingListener = itemUsingListener;
    }
    /**
     * 사용자가 아이템을 사용할 때 감지되는 리스너 클래스
     */
    public interface ItemUsingListener {
        void onItemUse();
    }

    /*
    * 사용자의 인벤토리에 아이템 저장 및 사용을 도와줌
    * 아이템의 정보는 ItemCodeParseHelper를 통해서
    * csv 파일을 파싱한 후 메모리 상에 적재해놓음
    * */

    /**
     * 인벤토리 생성자, 인벤토리를 처음 생성할 떄 json 문자열을 받아서 클래스 타입에 맞게 사용자 인벤토리에 저장합니다.
     */
    public InventoryHelper(String json, Context context) throws IOException {
        this.context = context;
        /*
        * 사용자의 인벤토리 아이템 json 문자열을 클래스 타입에 맞게 대입
        */
        user.setItemLists(new Gson().fromJson(json, new TypeToken<List<Item>>(){}.getType()));
        /*
        * csv 파일을 파싱하는 클래스
        */
        ItemCodeParseHelper itemCodeParseHelper = new ItemCodeParseHelper(context);
        detailsList = itemCodeParseHelper.getItemDetailsList();
    }

    /**
     * 인벤토리 생성자
     */
    public InventoryHelper (Context context) throws IOException {
        this.context = context;
        ItemCodeParseHelper itemCodeParseHelper = new ItemCodeParseHelper(context);
        detailsList = itemCodeParseHelper.getItemDetailsList();
    }

    public void setItemType(Item.ItemType itemType) {
        this.itemType = itemType;
    }

    /**
     * 상호작용 버튼을 눌렀을 때 아이템 버튼을 누르면 카테고리에 맞게 유저의 인벤토리 리스트에서 아이템을 가져옵니다.
     * @return
     */
    public String setItemName() {
        /*
        * 상호작용 버튼을 눌렀을 때 나오는 물, 밥, 씻기 버튼을 클릭하면
        * 카테고리에 맞게 유저의 인벤토리 리스트에서 아이템을 가져온다
        */
        currentItemIndex = 0;   // 초기값은 리스트의 첫번째로 선택
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

    /**
     * 선택된 아이템에서 다음 버튼을 누르면 아이템 인덱스가 변경되고 아이템 설명 텍스트를 변경합니다.
     */
    public String setNextItem() {
        /*
        * 현재 선택중인 아이템 에서 다음 버튼을 클릭시
        * 현재 아이템 인덱스가 변경되고 텍스트뷰도 변경
        */
        if (currentItemIndex < items.size() - 1) {
            currentItemIndex++;
        } else {
            Toast.makeText(context, "마지막 아이템 입니다.", Toast.LENGTH_SHORT).show();
        }
        Item firstItem = items.get(currentItemIndex);
        return getItemName(firstItem);

    }

    public String setBeforeItem() {
        if (currentItemIndex > 0) {
            currentItemIndex--;
        } else {
            Toast.makeText(context, "첫 번째 아이템 입니다.", Toast.LENGTH_SHORT).show();
        }
        Item firstItem = items.get(currentItemIndex);
        return getItemName(firstItem);
    }

    /**
     * 선택된 아이템을 화면에 문자열 형태로 표현하기 위한 메서드입니다.
     * @param item 아이템 클래스
     * @return 아이템에 해당하는 문자열을 반환
     */
    @NonNull
    private String getItemName(Item item) {
        if (item.getCount() > 0) {
            return String.format(Locale.getDefault(), "%s x %d개", findItem(item.getCode()).getName(), item.getCount());
        } else {
            return String.format(Locale.getDefault(), "  %s  ", findItem(item.getCode()).getName());
        }
    }

    /**
     * 사용자가 인벤토리에서 아이템을 사용하면 실행되는 메서드
     * 아이템이 존재하는지 판단하고 해당 아이템의 자세한 객체를 현재 선택된 동물에 전달하여
     * 아이템 효과대로 동물에 적용합니다.
     * @return 아이템이 없을경우 없다는 문자열을 반환하고 아니면 선택된 아이템의 상태에 해당하는 문자열을 반환합니다.
     */
    public String useCurrentItem() {
        if (!items.isEmpty()) {
            Item item = items.get(currentItemIndex);
            if (item.getCount() > 0) {
                item.setCount(item.getCount() - 1);    // 인벤토리에서 아이템 사용

                ItemDetail itemDetail = findItem(item.getCode());    // 디테일 아이템 가져옴
                Animal animal = user.getAnimalList().get(user.getNowSelectedPet());
                animal.useItem(itemDetail);    // 펫에게 아이템 사용
                itemUsingListener.onItemUse();    // 아이템을 사용했다고 알림

                // 사용한 아이템 체크 해서 미션 달성률 조종
                MissionCheckHelper missionCheckHelper = new MissionCheckHelper(context);
                missionCheckHelper.useItem(itemDetail.getType());

                if (item.getCount() == 0) {
                    user.getItemLists().remove(item);
                    items.remove(currentItemIndex);
                    return "아이템이 없습니다.";
                }
                return getItemName(item);
            }

            if (item.getCount() == -1) {
                ItemDetail itemDetail = findItem(item.getCode());    // 디테일 아이템 가져옴
                Animal animal = user.getAnimalList().get(user.getNowSelectedPet());
                animal.useItem(itemDetail);    // 펫에게 아이템 사용
                itemUsingListener.onItemUse();    // 아이템을 사용했다고 알림

                // 사용한 아이템 체크 해서 미션 달성률 조종
                MissionCheckHelper missionCheckHelper = new MissionCheckHelper(context);
                missionCheckHelper.useItem(itemDetail.getType());

                return getItemName(item);
            }
        }
        return "아이템이 없습니다.";
    }
}
