package com.lab_team_projects.my_walking_pet;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.lab_team_projects.my_walking_pet.databinding.ActivityMainBinding;
import com.lab_team_projects.my_walking_pet.game.Walk;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;
//    public WalkSensor walkSensor = new WalkSensor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        /*walkSensor.sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        walkSensor.sensorStepCounter = walkSensor.sensorManager.getDefaultSensor((Sensor.TYPE_STEP_COUNTER));
        if(walkSensor.sensorStepCounter == null) {
            Toast.makeText(this, "No Step Counter Sensor", Toast.LENGTH_SHORT).show();
        }*/
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        walkSensor.sensorManager.registerListener((SensorEventListener) this, walkSensor.sensorStepCounter,walkSensor.sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //다른 앱을 띄워놓아도 센서가 계속 동작
        walkSensor.sensorManager.registerListener((SensorEventListener) this, walkSensor.sensorStepCounter,walkSensor.sensorManager.SENSOR_DELAY_NORMAL);
    }


    public class WalkSensor implements SensorEventListener {
        //걸음 센서 클래스
        public SensorManager sensorManager;
        public Sensor sensorStepCounter;
        public Walk walk = new Walk();

        @Override
        public void onSensorChanged(SensorEvent event) {
            //센서값이 변화할 때, count 수 증가시키는 함수
            switch (event.sensor.getType()) {
                case Sensor.TYPE_STEP_COUNTER:
                    walk.setCount(walk.getCount() + 1);
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }*/


}

