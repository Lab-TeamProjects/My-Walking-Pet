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

/**
 * 걸음 통계를 화면에 보여주기 위한 프래그먼트 클래스
 */
public class WalkCountFragment extends Fragment {

    /**
     * xml에 직접 접근하기 위한 바인딩 클래스
     */
    private FragmentWalkCountBinding binding;
    /**
     * 메인 액티비티에서 프래그먼트간 이동시 상단 화면 잘림을 방지하기 위해 메서드 사용을 위한 메인 액티비티 클래스
     */
    private MainActivity mMainActivity;
    /**
     * 일일 걸음 통계 화면을 보여주기 위한 프래그먼트 클래스
     */
    private final DayFragment dayFragment = new DayFragment();
    /**
     * 주간 걸음 통계 화면을 보여주기 위한 프래그먼트 클래스
     */
    private final WeekFragment weekFragment = new WeekFragment();
    /**
     * 월간 걸음 통계 화면을 보여주기 위한 프래그먼트 클래스
     */
    private final MonthFragment monthFragment = new MonthFragment();
    /**
     * 걸음 데이터를 날짜별로 보기 편하게 접근하기 위한 캘린더 화면 프래그먼트 클래스
     */
    private final CalendarFragment yearFragment = new CalendarFragment();


    public WalkCountFragment() {
        // Required empty public constructor
    }

    /**
     * 메인 액티비티를 설정하기 위한 메서드
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    /**
     * 걸음 통계 버튼을 누르면 걸음 통계 프래그먼트 뷰가 생성되면 실행되는 메서드
     * 프래그먼트를 탭을 이용하여 프래그먼트 이동을 구현하였습니다.
     * 뷰페이저2 코드가 있지만
     */
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
        binding.viewPager2.setUserInputEnabled(false);

        new TabLayoutMediator(binding.tabs, binding.viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("1일");
            } else if (position == 1) {
                tab.setText("7일");
            } else if (position == 2) {
                tab.setText("1달");
            } else if (position == 3) {
                tab.setText("달력");
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