package com.lab_team_projects.my_walking_pet.helpers;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.home.Item;
import com.lab_team_projects.my_walking_pet.home.ItemDetails;
import com.lab_team_projects.my_walking_pet.login.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InventoryHelper {
    private User user = GameManager.getInstance().getUser();
    private TextView tvItemName;
    private ImageButton ibItemPreview;
    private ImageButton ibItemNext;

    private Item.ItemType itemType = Item.ItemType.DRINK;
    private Context context;
    private ItemCodeParseHelper itemCodeParseHelper;

    private List<ItemDetails> detailsList;
    private List<Item> items = new ArrayList<>();;
    private int currentItemIndex;

    public InventoryHelper(String json, Context context) throws IOException {
        user.setItemLists(new Gson().fromJson(json, new TypeToken<List<Item>>(){}.getType()));
        this.context = context;

        setInventory();
        itemCodeParseHelper = new ItemCodeParseHelper(context);
        detailsList = itemCodeParseHelper.getItemDetailsList();
    }

    private void setInventory() {

    }

    public void setBindingButton(TextView tvItemName,
                                 ImageButton ibItemPreview,
                                 ImageButton ibItemNext) {
        this.tvItemName = tvItemName;
        this.ibItemPreview = ibItemPreview;
        this.ibItemNext = ibItemNext;
    }

    public void setItemType(Item.ItemType itemType) {
        this.itemType = itemType;
    }

    public String setItemName() {
        currentItemIndex = 0;   // 물, 밥, 씻기 중에 선택
        items.clear();
        for(Item item : user.getItemLists()) {
            ItemDetails itemDetails = findItem(item.getCode());
            if (itemDetails.getType().equals(itemType.name())) {
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

    public ItemDetails findItem(int code) {
        for(ItemDetails itemDetails : detailsList) {
            if (itemDetails.getCode() == code) {
                return itemDetails;
            }
        }
        return null;
    }


    public String setNextItem() {
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
