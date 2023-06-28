package com.lab_team_projects.my_walking_pet.home;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.databinding.FragmentHomeBinding;
import com.lab_team_projects.my_walking_pet.helpers.AnimalSwipeTimeCheckHelper;
import com.lab_team_projects.my_walking_pet.helpers.InventoryHelper;
import com.lab_team_projects.my_walking_pet.helpers.OnSwipeTouchHelper;
import com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper;
import com.lab_team_projects.my_walking_pet.login.User;
import com.lab_team_projects.my_walking_pet.walk_count.WalkViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    private boolean isInteractionBtnClick = false;
    /**
     * The Last click time.
     */
    long lastClickTime = 0;
    /**
     * The Can drag time.
     */
    int canDragTime = 4000;    // 드래그 쿨타임 현재 3초
    private InventoryHelper inventoryHelper;

    private boolean isExercising = false;

    private AnimalSwipeTimeCheckHelper animalSwipeTimeCheckHelper = new AnimalSwipeTimeCheckHelper();

    private String[] liking = {"매우좋음", "좋음", "보통", "나쁨"};


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

        verifyStoragePermission(requireActivity());
        Animal nowPet = user.getAnimalList().get(user.getNowSelectedPet());
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

                        Animal animal = user.getAnimalList().get(user.getNowSelectedPet());

                        // 드래그 하면 동작할 것
                        boolean result = animalSwipeTimeCheckHelper.isCoolTimeOver(
                                animal);

                        if (result) {
                            animal.setLiking(animal.getLiking() + 25);
                        }

                        setMessageCloud(R.drawable.ic_messge_happy);

                        setAnimalLiking(animal);

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

    private void setAnimalLiking(Animal animal) {
        if (animal.getLiking() >= 75) {
            binding.tvLiking.setText("기분 매우 좋음");
        } else if (animal.getLiking() >= 50) {
            binding.tvLiking.setText("기분 좋음");
        } else if (animal.getLiking() >= 25) {
            binding.tvLiking.setText("기분 보틍");
        } else {
            binding.tvLiking.setText("기분 나쁨");
        }
    }

    private void setMessageCloud(int image) {
        // 애니메이션 시작 전에 View를 보이게 함
        Glide.with(requireContext()).load(image).into(binding.ivMessage);
        binding.ivMessage.setVisibility(View.VISIBLE);
        // 서서히 나타나는 애니메이션
        binding.ivMessage.animate().alpha(1f).setDuration(500).start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 2.5초 후에 서서히 사라지는 애니메이션
                binding.ivMessage.animate().alpha(0f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // 애니메이션이 끝난 후에 View를 안보이게 함
                        binding.ivMessage.setVisibility(View.INVISIBLE);
                    }
                }).start();
            }
        }, 3000);
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
                // 말풍선 표시, 이미지 추가되면 수정할것
                setMessageCloud(R.drawable.ic_messge);

                binding.tvItemName.setText(inventoryHelper.useCurrentItem());
            }
        });


    }

    /**
     * 현재 선택된 동물의 성장치, 수치를 홈화면에 그릴 수 있도록 수치를 가져옵니다.
     */
    private void initPetGrower() {
        // 초기 설정
        setAnimalShow();

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
            if (0 < user.getNowSelectedPet()) {
                user.setNowSelectedPet(user.getNowSelectedPet() - 1);
                setAnimalShow();
            }
        });

        // 펫 변경 오른쪽
        binding.btnNextPet.setOnClickListener(v->{
            if (user.getNowSelectedPet() < user.getAnimalList().size() - 1) {
                user.setNowSelectedPet(user.getNowSelectedPet() + 1);
                setAnimalShow();
            }
        });
    }

    private void setAnimalShow() {
        Animal animal = user.getAnimalList().get(user.getNowSelectedPet());
        setPetRate(animal);

        /*
        animalImages : 동물 레벨에 따른 이미지
        0 : 고양이
        1 : 개
        2: 원숭이
        3: 햄스터

         */
        int[][] animalImages = {{R.drawable.img_egg_white, R.drawable.img_pet_cat_1, R.drawable.img_pet_cat_2, R.drawable.img_pet_cat_3}
                , {}
                , {}
                , {R.drawable.img_egg_yellow, R.drawable.img_pet_hamster_2, R.drawable.img_pet_hamster_3, R.drawable.img_pet_hamster_4}};

        // Broods enum 클래스에서 해당 동물이 몇번째인지
        int broodsIndex = Broods.valueOf(animal.getBrood()).ordinal();
        int animalLv = animal.getLevel();
        Glide.with(requireContext()).load(animalImages[broodsIndex][animalLv]).fitCenter().into(binding.pet);
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
        binding.ibAR.setOnClickListener(v -> {
            takeScreenshot();
        });

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

    private void takeScreenshot() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

            // 외부 저장소 경로를 "/learn" 디렉토리로 설정
            String dirPath = Environment.getExternalStorageDirectory().toString() + "/learn";
            File fileDir = new File(dirPath);
            if (!fileDir.exists()) {
                boolean mkdir = fileDir.mkdir();
            }

            String path = dirPath + "/" + "sc" + timeStamp + "a.jpg";

            // 스크린샷을 찍기 위해 뷰를 생성
            View v1 = requireActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(path);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            outputStream.flush();
            outputStream.close();

            Toast.makeText(requireContext(), "스크린샷을 저장했습니다.", Toast.LENGTH_SHORT).show();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
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
        animalSwipeTimeCheckHelper.loadAnimal();
        binding.tvMoney.setText(String.valueOf(gm.getUser().getMoney()));
        Animal nowPet = user.getAnimalList().get(user.getNowSelectedPet());
        setAnimalLiking(nowPet);
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