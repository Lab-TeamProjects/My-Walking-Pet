package com.lab_team_projects.my_walking_pet.helpers;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.home.Item;
import com.lab_team_projects.my_walking_pet.login.User;

import java.io.IOException;
import java.util.List;

public class InventoryHelper {
    private User user = GameManager.getInstance().getUser();
    private TextView tvItemName;
    private ImageButton ibItemPreview;
    private ImageButton ibItemNext;

    private Item.ItemSelect selectType = Item.ItemSelect.DRINK;
    private Context context;
    private ItemCodeParseHelper itemCodeParseHelper;

    public InventoryHelper(String json, Context context) throws IOException {
        List<Item> itemList = new Gson().fromJson(json, new TypeToken<List<Item>>(){}.getType());
        user.setItemLists(itemList);
        this.context = context;

        setInventory();
        itemCodeParseHelper = new ItemCodeParseHelper(context);

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

    public void setSelectType(Item.ItemSelect selectType) {
        this.selectType = selectType;
    }

    public String setItemName() {
        if (selectType == Item.ItemSelect.DRINK) {
        }
        return "";
    }



}
