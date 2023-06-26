package com.lab_team_projects.my_walking_pet.home;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.databinding.FragmentHomeBinding;
import com.lab_team_projects.my_walking_pet.helpers.InventoryHelper;
import com.lab_team_projects.my_walking_pet.helpers.OnSwipeTouchHelper;
import com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper;
import com.lab_team_projects.my_walking_pet.login.User;
import com.lab_team_projects.my_walking_pet.walk_count.WalkViewModel;

import java.io.IOException;

/**
 * 홈 화면에 해당하는 프래그먼트 클래스
 * 프래그먼트를 상속 받음
 */
public class HomeFragment extends Fragment {

    /**
     * 포그라운드 서비스와 통신하기 위한 바인더 클래스
     */
    private MyBinder svc;

    private FragmentHomeBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;

    /**
     * 게임 매니저
     */
    private final GameManager gm = GameManager.getInstance();
    /**
     * 현재 접속된 유저
     */
    private final User user = gm.getUser();
    /**
     * 현재 선택된 동물
     */
    private final Animal nowPet = user.getAnimalList().get(user.getNowSelectedPet());

    private boolean isInteractionBtnClick = false;
    /**
     * The Last click time.
     */
    long lastClickTime = 0;
    /**
     * The Can drag time.
     */
    int canDragTime = 3000;    // 드래그 쿨타임 현재 3초
    private InventoryHelper inventoryHelper;

    private boolean isExercising = false;

    /**
     * Instantiates a new Home fragment.
     */
    public HomeFragment() { }

    /**
     * 홈 프래그먼트 화면이 생성되면 실행되는 메서드
     * 동물을 쓰다듬는 상호작용, 사용자 인벤토리, 동물 성장치 불러오기 등등 정의되어있습니다.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        nowPet.setGrowthCallback(new Animal.GrowthCallback() {
            @Override
            public void onCall() {
                binding.customBarChartView.setContentBarRatio(nowPet.getGrowth(),nowPet.getMaxGrowth());
            }
        });

        bindingListener();
        try {
            initInventory();
        } catch (IOException e) {
            Log.e("Homefragment_onCreateView", "IOException", e);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GameManager gm = GameManager.getInstance();
        User user = gm.getUser();
        Animal nowPet = user.getAnimalList().get(user.getNowSelectedPet());

        binding.tvMoney.setText(String.valueOf(gm.getUser().getMoney()));
        binding.customBarChartView.setContentBarRatio(nowPet.getGrowth(),nowPet.getMaxGrowth());
    }

    /**
     * 사용자 인벤토리의 리스너를 정의합니다.
     * @throws IOException
     */

    @SuppressLint("ClickableViewAccessibility")
    private void initInventory() throws IOException {
        inventoryHelper = new InventoryHelper(requireContext());
        binding.fabWater.setOnClickListener(v -> setFabOnClickListener(Item.ItemType.DRINK, inventoryHelper));
        binding.fabFood.setOnClickListener(v -> setFabOnClickListener(Item.ItemType.FOOD, inventoryHelper));
        binding.fabWash.setOnClickListener(v -> setFabOnClickListener(Item.ItemType.WASH, inventoryHelper));
        binding.ibItemNext.setOnClickListener(v -> binding.tvItemName.setText(inventoryHelper.setNextItem()));
        binding.ibItemPreview.setOnClickListener(v -> binding.tvItemName.setText(inventoryHelper.setBeforeItem()));
        binding.tvItemName.setOnTouchListener(new OnSwipeTouchHelper(requireContext()) {
            @Override
            public void onSwipeTop() {
                binding.tvItemName.setText(inventoryHelper.useCurrentItem());
            }
        });
    }

    /**
     * 현재 선택된 동물의 성장치, 수치를 홈화면에 그릴 수 있도록 수치를 가져옵니다.
     */
    private void initPetGrower() {
        User user = GameManager.getInstance().getUser();
        setPetRate(user.getAnimalList().get(user.getNowSelectedPet()));

        // 펫 이름 클릭시 정보 다이얼로그
        binding.tvPetName.setOnClickListener(v->{
            CustomPetInfoDialog dialog = new CustomPetInfoDialog(requireContext());
            dialog.show();
        });

        // 펫 아이템 사용시 수치 다시 그리기
        inventoryHelper.setItemUsingListener(() -> {
            setPetRate(user.getAnimalList().get(user.getNowSelectedPet()));
        });

        // 펫 변경 왼쪽
        binding.btnPrevPet.setOnClickListener(v->{
            Animal animal;
            if (0 < user.getNowSelectedPet()) {
                user.setNowSelectedPet(user.getNowSelectedPet() - 1);
                animal = user.getAnimalList().get(user.getNowSelectedPet());
                setPetRate(animal);
            }
        });

        // 펫 변경 오른쪽
        binding.btnNextPet.setOnClickListener(v->{
            Animal animal;
            if (user.getNowSelectedPet() < user.getAnimalList().size() - 1) {
                user.setNowSelectedPet(user.getNowSelectedPet() + 1);
                animal = user.getAnimalList().get(user.getNowSelectedPet());
                setPetRate(animal);
            }
        });
    }

    /**
     * 성장치들의 프로그래스바를 설정합니다.
     * @param currentPet
     */
    // 성장 수치들(목마름,청결....) 프로그래스바 세팅
    private void setPetRate(Animal currentPet) {
        binding.tvPetName.setText(currentPet.getName());
        binding.pbHunger.setProgress(currentPet.getHunger());
        binding.pbThirst.setProgress(currentPet.getThirsty());
        binding.pbCleanliness.setProgress(currentPet.getClean());
    }

    /**
     * 상호작용 버튼을 동작할 수 있도록 설정합니다.
     * @param itemType
     * @param inventoryHelper
     */
    // 플로팅 액션버튼 설정
    private void setFabOnClickListener(Item.ItemType itemType, InventoryHelper inventoryHelper) {
        binding.tvItemName.setVisibility(View.VISIBLE);
        binding.ibItemPreview.setVisibility(View.VISIBLE);
        binding.ibItemNext.setVisibility(View.VISIBLE);
        inventoryHelper.setItemType(itemType);
        binding.tvItemName.setText(inventoryHelper.setItemName());
    }

    /**
     * 홈 화면에서 보이는 각종 버튼들의 리스너를 설정합니다.
     */
    // 각종 리스너 바인딩
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

        binding.ibHelp.setOnClickListener(v -> showHelpOverlay()); // 도움말을 표시하는 메소드 호출
        //binding.helpOverlay.setOnClickListener(v -> hideHelpOverlay());
        binding.ibAR.setOnClickListener(v -> Toast.makeText(requireContext(), "AR 이동 버튼", Toast.LENGTH_SHORT).show()); // Toast -> "카메라 실행"으로 변경 필요

        binding.ibExercise.setOnClickListener(v -> {
            CustomExerciseDialog dialog = new CustomExerciseDialog(requireContext(), isExercising, isExercising ? svc : null);
            dialog.setOnExerciseListener(new CustomExerciseDialog.OnExerciseListener() {
                @Override
                public void onExercise(boolean flag, int selected) {
                    isExercising = flag;
                    serviceCreateAndBind(selected);
                }
            });
            dialog.show();
        });
    }

    /**
     * 액티비티 위에서 프래그먼트간의 이동할 떄 실행됩니다.
     * @param view
     * @param fragmentId
     */
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

        binding.tvMoney.setText(String.valueOf(gm.getUser().getMoney()));
        binding.customBarChartView.setContentBarRatio(nowPet.getGrowth(),nowPet.getMaxGrowth());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 바인딩은 생명주기 이슈 때문에 프래그먼트가 종료되면 널을 넣어줘야 함
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        binding = null;
    }

    /**
     * 포그라운드 서비스에서 동작하는 바인더 클래스를 정의합니다.
     *
     * @param selected the selected
     */
    public void serviceCreateAndBind(int selected) {
        svc = null;
        Intent intent = new Intent(getContext(), WalkingTimeCheckService.class);
        intent.putExtra("time", selected);

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                svc = (MyBinder) service;
                svc.setListener(new MyBinder.OnBinderListener() {
                    @Override
                    public void onFinish(boolean flag) {
                        isExercising = flag;
                        requireContext().stopService(intent);
                        requireContext().unbindService(mServiceConnection);
                        mServiceConnection = null;
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e("운동 시작 서비스","비정상적 종료");
            }
        };

        requireContext().bindService(intent,mServiceConnection,Context.BIND_AUTO_CREATE);
        requireContext().startService(intent);
    }

    private void showHelpOverlay() {
        // binding.helpOverlay.setVisibility(View.VISIBLE); // 반투명한 화면을 보이도록 설정

        // 각 객체의 설명을 나타내는 코드 작성
        // 객체의 설명을 나타내는 TextView를 추가하거나 다른 방식을 사용하여 구현합니다.
    }

    private void hideHelpOverlay() {
        //binding.helpOverlay.setVisibility(View.GONE); // 반투명한 화면을 숨기도록 설정

        // 객체의 설명을 숨기는 코드 작성
    }

    private ServiceConnection mServiceConnection;
}