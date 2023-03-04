package com.lab_team_projects.my_walking_pet.app;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.ActivityMainBinding;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;
import com.lab_team_projects.my_walking_pet.login.User;
import com.lab_team_projects.my_walking_pet.walk_count.Walk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;
//    public WalkSensor walkSensor = new WalkSensor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // 리스트가 완전히 비었음
        if (walkList.isEmpty()) {
            // 새로 오늘 walk를 만들어야함
            Walk walk = new Walk();
            gameManager.setWalk(walk);
            db.walkDao().insert(walk);

        } else {
            // 저장된 walk 리스트에서 마지막 walk를 가져옴
            Walk walk = walkList.get(walkList.size() - 1);
            try {
                Date walkDate = sdf.parse(walk.getDate());
                LocalDate walkLocalDate = walkDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                // 리스트에 저장된 마지막 walk가 오늘 날짜와 다름
                if (localDate.isAfter(walkLocalDate)) {
                    // 오늘 walk를 만들어야함
                    walk = new Walk();
                    gameManager.setWalk(walk);
                    db.walkDao().insert(walk);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }



        /*
        * 뷰바인딩
        * 레이아웃에 대한 클래스를 만들 필요 없이
        * 바인딩 선언 하나로 전부 접근 가능
        * */

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 앱바 설정
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.walkCountFragment,
                R.id.shopFragment, R.id.missionFragment,
                R.id.collectionFragment,
                R.id.settingFragment
        ).build();

        // 네비게이션 컨트롤러
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        // 프래그먼트 이동했을 때 앱바 표시 조건문
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.homeFragment) {
                binding.appBarLayout.setVisibility(View.GONE);
            } else {
                binding.appBarLayout.setVisibility(View.VISIBLE);
            }
        });

        binding.ibHome.setOnClickListener(v->{
            onBackPressed();
        });


    }

}

