package com.lab_team_projects.my_walking_pet.app.onborarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.FragmentOnBoardingSecondBinding;

public class OnBoardingSecondFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentOnBoardingSecondBinding binding = FragmentOnBoardingSecondBinding.inflate(inflater, container, false);
        ViewPager2 viewPager2 = requireActivity().findViewById(R.id.viewPager2);

        binding.btnNext.setOnClickListener(v->{
            viewPager2.setCurrentItem(2);
        });

        return binding.getRoot();
    }
}