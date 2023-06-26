package com.lab_team_projects.my_walking_pet.app;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.FragmentSplashBinding;
import com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper;

/**
 * 앱을 처음 실행했을 때 서버와 동기화 하는 동안 화면에 표시할 프래그먼트
 */
public class SplashFragment extends Fragment {


    private FragmentSplashBinding binding;

    /**
     * Instantiates a new Splash fragment.
     */
    public SplashFragment() {
        // Required empty public constructor
    }

    /**
     * 앱을 처음 실행하면 앱 도움말 프래그먼트로 이동하고 아니면 바로 메인 액티비티로 이동합니다.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NavController navController = Navigation.findNavController(binding.getRoot());
                if (isFinished() == 1) {
                    navController.navigate(R.id.titleFragment);
                } else {
                    navController.navigate(R.id.swipeFragment);
                }
                /* 여기 이제 뭐 로그인 되어있는지 이런거 추가하면 될듯 */
            }
        }, 3000);

        return binding.getRoot();
    }

    private int isFinished() {
        UserPreferenceHelper preferenceHelper = new UserPreferenceHelper(requireContext(), "tutorial");
        return preferenceHelper.loadIntValue("onBoarding");
    }
}