package com.lab_team_projects.my_walking_pet.home;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.databinding.FragmentHomeBinding;
import com.lab_team_projects.my_walking_pet.help.HelpActivity;
import com.lab_team_projects.my_walking_pet.helpers.InventoryHelper;
import com.lab_team_projects.my_walking_pet.helpers.OnSwipeTouchHelper;
import com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper;
import com.lab_team_projects.my_walking_pet.login.User;
import com.lab_team_projects.my_walking_pet.walk_count.WalkViewModel;

import java.io.IOException;

public class HomeFragment extends Fragment {

    WalkingTimeCheckService.MyBinder svc;

    private FragmentHomeBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;
    private boolean isInteractionBtnClick = false;
    long lastClickTime = 0;
    int canDragTime = 3000;    // 드래그 쿨타임 현재 3초
    private InventoryHelper inventoryHelper;


    private boolean isExercising = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);



        bindingListener();
        try {
            initInventory();
        } catch (IOException e) {
            // 임시
            e.printStackTrace();
        }

        initPetGrower();

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
                        Log.d("__walk", "asdasd");
                        // 드래그 하면 동작할 것
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // 드래그 종료시 동작
                    break;
            }


            return true;
        });


        WalkViewModel walkViewModel = new ViewModelProvider(this).get(WalkViewModel.class);
        walkViewModel.getWalkLiveData().observe(getViewLifecycleOwner(), walk -> {
            binding.tvWalkCount.setText(String.valueOf(walk.getCount()));
        });

        sharedPreferences = requireContext().getSharedPreferences(UserPreferenceHelper.UserTokenKey.login.name(), Context.MODE_PRIVATE);

        sharedPreferenceChangeListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        if (key.equals(UserPreferenceHelper.UserPreferenceKey.money.name())) {
                            int money = sharedPreferences.getInt(key, GameManager.getInstance().getUser().getMoney());
                            GameManager.getInstance().getUser().setMoney(money);
                            binding.tvMoney.setText(String.valueOf(money));
                        }
                    }
                };

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        return binding.getRoot();
    }




    @SuppressLint("ClickableViewAccessibility")
    private void initInventory() throws IOException {

        inventoryHelper = new InventoryHelper(requireContext());
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

    private void initPetGrower() {
        User user = GameManager.getInstance().getUser();
        setPetRate(user.getAnimalList().get(user.getNowSelectedPet()));

        // 펫 이름 클릭시 정보 다이얼로그
        binding.tvPetName.setOnClickListener(v->{
            CustomPetInfoDialog dialog = new CustomPetInfoDialog(requireContext());
            dialog.show();
        });

        //  펫 아이템 사용시 수치 다시 그리기
        inventoryHelper.setItemUsingListener(() -> {
            setPetRate(user.getAnimalList().get(user.getNowSelectedPet()));
        });

        /*
         * 펫 변경
         * */

        binding.btnPrevPet.setOnClickListener(v->{
            Animal animal;
            if (0 < user.getNowSelectedPet()) {
                user.setNowSelectedPet(user.getNowSelectedPet() - 1);
                animal = user.getAnimalList().get(user.getNowSelectedPet());
                setPetRate(animal);
            }
        });

        binding.btnNextPet.setOnClickListener(v->{
            Animal animal;
            if (user.getNowSelectedPet() < user.getAnimalList().size() - 1) {
                user.setNowSelectedPet(user.getNowSelectedPet() + 1);
                animal = user.getAnimalList().get(user.getNowSelectedPet());
                setPetRate(animal);
            }
        });
    }

    private void setPetRate(Animal currentPet) {
        binding.tvPetName.setText(currentPet.getName());
        binding.pbHunger.setProgress(currentPet.getHunger());
        binding.pbThirst.setProgress(currentPet.getThirsty());
        binding.pbCleanliness.setProgress(currentPet.getClean());
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
        binding.ibWalkCount.setOnClickListener(v -> navigateToFragment(v, R.id.walkCountFragment));

        binding.fabInteraction.setOnClickListener(v -> {
            isInteractionBtnClick = !isInteractionBtnClick;
            clickInteractionBtn(isInteractionBtnClick);
        });

        binding.ibHelp.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), HelpActivity.class));
            Toast.makeText(requireContext(), "도움말 버튼", Toast.LENGTH_SHORT).show();
        });

        binding.ibAR.setOnClickListener(v -> Toast.makeText(requireContext(), "AR 이동 버튼", Toast.LENGTH_SHORT).show());

        binding.ibExercise.setOnClickListener(v -> {
            CustomExerciseDialog dialog;
            if (svc != null) {
                dialog = new CustomExerciseDialog(requireContext(), svc.flag, this);
                dialog.setOnExerciseListener(new CustomExerciseDialog.OnExerciseListener() {
                    @Override
                    public void exercise(boolean flag) {
                        isExercising = flag;
                    }
                });
            }
            else {
                dialog = new CustomExerciseDialog(requireContext(), false, this);
                dialog.setOnExerciseListener(new CustomExerciseDialog.OnExerciseListener() {
                    @Override
                    public void exercise(boolean flag) {
                        isExercising = flag;
                    }
                });
            }

            dialog.show();
        });
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
    public void onResume() {
        super.onResume();
        GameManager gameManager = GameManager.getInstance();
        binding.tvMoney.setText(String.valueOf(gameManager.getUser().getMoney()));
        //Log.d("__walk", String.valueOf(binding.tvMoney.getId()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 바인딩은 생명주기 이슈 때문에 프래그먼트가 종료되면 널을 넣어줘야 함
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        binding = null;
    }

    public void serviceCreateAndBind(int selected) {
        Intent intent = new Intent(getContext(), WalkingTimeCheckService.class);
        intent.putExtra("time", selected);
        getContext().bindService(intent,mServiceConnection,Context.BIND_AUTO_CREATE);



        //+
        // getContext().unbindService(b);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            svc = (WalkingTimeCheckService.MyBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("asdf","asdfasdfas");
        }
    };
}