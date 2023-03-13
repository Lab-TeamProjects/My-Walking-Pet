package com.lab_team_projects.my_walking_pet.home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.FragmentHomeBinding;
import com.lab_team_projects.my_walking_pet.help.HelpActivity;
import com.lab_team_projects.my_walking_pet.helpers.InventoryHelper;
import com.lab_team_projects.my_walking_pet.helpers.OnSwipeTouchHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private boolean isInteractionBtnClick = false;
    long lastClickTime = 0;
    int canDragTime = 3000;    // 드래그 쿨타임 현재 3초

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.pbHunger.setProgress(70);
        binding.pbThirst.setProgress(40);
        binding.pbCleanliness.setProgress(50);

        bindingListener();
        try {
            initInventory();
        } catch (IOException e) {
            // 임시
            e.printStackTrace();
        }

        binding.pet.setOnTouchListener((v, event) -> {
            v.performClick();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 드래그 처음 시작시 동작
                    break;
                case MotionEvent.ACTION_MOVE:
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime > canDragTime) {
                        lastClickTime = currentTime;
                        // 드래그 하면 동작할 것
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // 드래그 종료시 동작
                    break;
            }


            return true;
        });
        return binding.getRoot();
    }




    @SuppressLint("ClickableViewAccessibility")
    private void initInventory() throws IOException {
        /* 임시 */
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        try {
            jsonObject.put("code", 1001).put("count", 3);
            jsonObject2.put("code", 1006).put("count", 10);
            jsonObject3.put("code", 1002).put("count", 5);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        jsonArray.put(jsonObject2);
        jsonArray.put(jsonObject3);

        String json = jsonArray.toString();

        /*
        * 인벤토리 조작
        * */
        InventoryHelper inventoryHelper = new InventoryHelper(json, requireContext());
        binding.fabWater.setOnClickListener(v -> setFabOnClickListener(Item.ItemType.DRINK, inventoryHelper));
        binding.fabFood.setOnClickListener(v -> setFabOnClickListener(Item.ItemType.FOOD, inventoryHelper));
        binding.fabWash.setOnClickListener(v -> setFabOnClickListener(Item.ItemType.WASH, inventoryHelper));
        binding.ibItemNext.setOnClickListener(v -> binding.tvItemName.setText(inventoryHelper.setNextItem()));
        binding.ibItemPreview.setOnClickListener(v -> binding.tvItemName.setText(inventoryHelper.setPreviewItem()));
        binding.tvItemName.setOnTouchListener(new OnSwipeTouchHelper(requireContext()) {
            @Override
            public void onSwipeTop() {
                binding.tvItemName.setText(inventoryHelper.useCurrentItem());
            }
        });

    }

    private void setFabOnClickListener(Item.ItemType itemType, InventoryHelper inventoryHelper) {
        binding.tvItemName.setVisibility(View.VISIBLE);
        binding.ibItemPreview.setVisibility(View.VISIBLE);
        binding.ibItemNext.setVisibility(View.VISIBLE);
        inventoryHelper.setItemType(itemType);
        binding.tvItemName.setText(inventoryHelper.setItemName());
    }


    private void bindingListener() {
        binding.ibShop.setOnClickListener(v -> navigateToFragment(v, R.id.shopFragment));
        binding.ibSetting.setOnClickListener(v -> navigateToFragment(v, R.id.settingFragment));
        binding.ibMission.setOnClickListener(v -> navigateToFragment(v, R.id.missionFragment));
        binding.ibCollection.setOnClickListener(v -> navigateToFragment(v, R.id.collectionFragment));
        binding.tvWalkCount.setOnClickListener(v -> navigateToFragment(v, R.id.walkCountFragment));

        binding.fabInteraction.setOnClickListener(v -> {
            isInteractionBtnClick = !isInteractionBtnClick;
            clickInteractionBtn(isInteractionBtnClick);
        });

        binding.ibHelp.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), HelpActivity.class));
            Toast.makeText(requireContext(), "도움말 버튼", Toast.LENGTH_SHORT).show();
        });

        binding.ibAR.setOnClickListener(v -> Toast.makeText(requireContext(), "AR 이동 버튼", Toast.LENGTH_SHORT).show());
    }

    private void navigateToFragment(View view, @IdRes int fragmentId) {
        Navigation.findNavController(view).navigate(fragmentId, null);
    }

    private void clickInteractionBtn(boolean isClick){
        if(isClick) {
            binding.fabWater.setVisibility(View.VISIBLE);
            binding.fabFood.setVisibility(View.VISIBLE);
            binding.fabWash.setVisibility(View.VISIBLE);
        } else {
            binding.fabWater.setVisibility(View.INVISIBLE);
            binding.fabFood.setVisibility(View.INVISIBLE);
            binding.fabWash.setVisibility(View.INVISIBLE);
            binding.tvItemName.setVisibility(View.INVISIBLE);
            binding.ibItemPreview.setVisibility(View.INVISIBLE);
            binding.ibItemNext.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 바인딩은 생명주기 이슈 때문에 프래그먼트가 종료되면 널을 넣어줘야 함
        binding = null;
    }
}