package com.lab_team_projects.my_walking_pet.walk_count;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lab_team_projects.my_walking_pet.adapters.FragmentPagerAdapter;
import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.databinding.FragmentWalkCountBinding;

import java.util.Arrays;
import java.util.List;

public class WalkCountFragment extends Fragment {

    private FragmentWalkCountBinding binding;
    private MainActivity mMainActivity;
    private final DayFragment dayFragment = new DayFragment();
    private final WeekFragment weekFragment = new WeekFragment();
    private final MonthFragment monthFragment = new MonthFragment();
    private final YearFragment yearFragment = new YearFragment();


    public WalkCountFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWalkCountBinding.inflate(inflater, container, false);

        mMainActivity.onAppBarLoad();
        /*
         * 탭 별로 프래그먼트 전환
         * 뷰페이저2, 어댑터 연동
         * */

        List<Fragment> fragments = Arrays.asList(dayFragment, weekFragment, monthFragment, yearFragment);
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(requireActivity(), fragments);
        binding.viewPager2.setAdapter(pagerAdapter);

        new TabLayoutMediator(binding.tabs, binding.viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("1일");
            } else if (position == 1) {
                tab.setText("7일");
            } else if (position == 2) {
                tab.setText("1달");
            } else if (position == 3) {
                tab.setText("1년");
            }
        }).attach();

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}