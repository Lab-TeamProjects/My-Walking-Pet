package com.lab_team_projects.my_walking_pet.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.adapters.FragmentPagerAdapter;
import com.lab_team_projects.my_walking_pet.app.onborarding.OnBoardingFirstFragment;
import com.lab_team_projects.my_walking_pet.app.onborarding.OnBoardingSecondFragment;
import com.lab_team_projects.my_walking_pet.app.onborarding.OnBoardingThirdFragment;
import com.lab_team_projects.my_walking_pet.databinding.FragmentSwipeBinding;

import java.util.Arrays;
import java.util.List;

/**
 * 앱 도움말 프래그먼트를 스와이프할 수 있도록 하는 프래그먼트입니다.
 */
public class SwipeFragment extends Fragment {

    /**
     * 뷰페이저2를 바인딩하여 도움말 프래그먼트들을 스와이프할 수 있또록 구현합니다.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSwipeBinding binding = FragmentSwipeBinding.inflate(inflater, container, false);
        List<Fragment> fragmentList = Arrays.asList(
                new OnBoardingFirstFragment(),
                new OnBoardingSecondFragment(),
                new OnBoardingThirdFragment()
        );
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(requireActivity(), fragmentList);
        binding.viewPager2.setAdapter(adapter);
        return binding.getRoot();
    }
}