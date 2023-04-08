package com.lab_team_projects.my_walking_pet.app.onborarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.FragmentOnBoardingThirdBinding;
import com.lab_team_projects.my_walking_pet.helpers.UserPreferenceHelper;

public class OnBoardingThirdFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentOnBoardingThirdBinding binding = FragmentOnBoardingThirdBinding.inflate(inflater, container, false);
        /*
        * 마지막 버튼을 누르면 튜토리얼은 다본거라서 다봤다고 설정함
        */
        binding.btnNext.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(binding.getRoot());
            navController.navigate(R.id.titleFragment);
            onBoardingFinished();
        });

        return binding.getRoot();
    }

    private void onBoardingFinished() {
        UserPreferenceHelper preferenceHelper = new UserPreferenceHelper(requireContext(), "tutorial");
        preferenceHelper.saveIntValue("onBoarding", 1);
    }
}