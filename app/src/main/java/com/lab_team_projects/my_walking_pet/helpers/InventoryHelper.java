package com.lab_team_projects.my_walking_pet.helpers;

import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.home.Item;
import com.lab_team_projects.my_walking_pet.login.User;

import java.util.List;

public class InventoryHelper {
    private User user = GameManager.getInstance().getUser();
    private ExtendedFloatingActionButton fabWater;
    private ExtendedFloatingActionButton fabFood;
    private ExtendedFloatingActionButton fabWash;
    private TextView tvItemName;
    private ImageButton ibItemPreview;
    private ImageButton ibItemNext;

    private Item.ItemSelect selectType = Item.ItemSelect.WATER;

    public InventoryHelper(String json) {
        List<Item> itemList = new Gson().fromJson(json, new TypeToken<List<Item>>(){}.getType());
        user.setItemLists(itemList);

        setInventory();


    }

    private void setInventory() {

    }

    public void setBindingButton(ExtendedFloatingActionButton fabWater,
                                 ExtendedFloatingActionButton fabFood,
                                 ExtendedFloatingActionButton fabWash,
                                 TextView tvItemName,
                                 ImageButton ibItemPreview,
                                 ImageButton ibItemNext) {
        this.fabWater = fabWater;
        this.fabFood = fabFood;
        this.fabWash = fabWash;
        this.tvItemName = tvItemName;
        this.ibItemPreview = ibItemPreview;
        this.ibItemNext = ibItemNext;
    }
}
