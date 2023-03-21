package com.lab_team_projects.my_walking_pet.app;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.ActivityMainBinding;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;
import com.lab_team_projects.my_walking_pet.helpers.InventoryHelper;
import com.lab_team_projects.my_walking_pet.home.Animal;
import com.lab_team_projects.my_walking_pet.home.Broods;
import com.lab_team_projects.my_walking_pet.login.User;
import com.lab_team_projects.my_walking_pet.walk_count.Walk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserPetList(); // 임시
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
         * 게임매니저를 통해서 유저 정보를 세팅함
         * 이부분은 다른곳으로 옮겨도 됨
         * 자동로그인 하는 부분이라던가
         * */

        GameManager gameManager = GameManager.getInstance();
        gameManager.loadUser(this);


        /*
         * 현재 데이터베이스를 확인해서 전날 db가 있는지 확인해야함
         * 혹은 오늘 db가 있는지 없는지 확인해아함
         * db가 없으면 새로운 Walk를 만들고 db에 저장
         * */

        /*
         * 현재 시간을 받아와야함
         * 아래 date format을 저렇게 한 이유는 mysql date 포맷이랑 같게 하려고 했음
         * */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String time = sdf.format(date);

        AppDatabase db = AppDatabase.getInstance(this);
        List<Walk> walkList = db.walkDao().getAll();
        Walk walk;
        // 리스트가 완전히 비었음
        if (walkList.isEmpty()) {
            // 새로 오늘 walk를 만들어야함
            walk = new Walk();
            walk.setDate(time);
            gameManager.setWalk(walk);
            db.walkDao().insert(walk);

        } else {
            // 저장된 walk 리스트에서 마지막 walk를 가져옴
            walk = walkList.get(walkList.size() - 1);
            try {
                Date walkDate = sdf.parse(walk.getDate());
                LocalDate walkLocalDate = walkDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                // 리스트에 저장된 마지막 walk가 오늘 날짜와 다름
                if (localDate.isAfter(walkLocalDate)) {
                    // 오늘 walk를 만들어야함
                    walk = new Walk();
                    walk.setDate(time);
                    db.walkDao().insert(walk);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        gameManager.setWalk(walk);

        setUserInventory();    // 임시



        // 앱바 설정
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.walkCountFragment,
                R.id.shopFragment, R.id.missionFragment,
                R.id.collectionFragment,
                R.id.settingFragment
        ).build();

        // 네비게이션 컨트롤러
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        // 홈화면에서는 앱바를 표시하면 안되기 때문에 홈화면일 때 앱바 숨김
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.homeFragment) {
                binding.appBarLayout.setVisibility(View.GONE);
            }
        });

        binding.ibHome.setOnClickListener(v->{
            onBackPressed();
        });





    }

    private void setUserPetList() {
        User user = GameManager.getInstance().getUser();
        Animal pet1 = new Animal("착한아이", Broods.DOG, user, user);
        Animal pet2 = new Animal("멋진아이", Broods.CAT, user, user);
        Animal pet3 = new Animal("천재아이", Broods.MONKEY, user, user);
        List<Animal> list = new ArrayList<>();
        list.add(pet1);
        list.add(pet2);
        list.add(pet3);
        GameManager.getInstance().getUser().setAnimalList(list);
    }

    private void setUserInventory() {
        /* 임시 */
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        JSONObject jsonObject4 = new JSONObject();
        try {
            jsonObject.put("code", 1001).put("count", 3);
            jsonObject2.put("code", 1006).put("count", 10);
            jsonObject3.put("code", 1002).put("count", 5);
            jsonObject4.put("code", 1004).put("count", -1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        jsonArray.put(jsonObject2);
        jsonArray.put(jsonObject3);
        jsonArray.put(jsonObject4);

        String json = jsonArray.toString();

        /*
         * 인벤토리 조작
         * */
        try {
            InventoryHelper inventoryHelper = new InventoryHelper(json, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAppBarLoad(){
        binding.appBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameManager gameManager = GameManager.getInstance();
        Walk walk = gameManager.getWalk();
        AppDatabase db = AppDatabase.getInstance(this);
        db.walkDao().update(walk);
    }
}

