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

public class SplashFragment extends Fragment {


    private FragmentSplashBinding binding;
    public SplashFragment() {
        // Required empty public constructor
    }

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