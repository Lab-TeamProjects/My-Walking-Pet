package com.lab_team_projects.my_walking_pet.walk_count;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.databinding.FragmentYearBinding;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * 걸음 정보의 이전 내역을 날짜별로 보기 쉽게 하기 위한 캘린더 프래그먼트
 */
public class CalendarFragment extends Fragment {

    /**
     * 프래그먼트 xml에 접근하기 위한 바인딩 클래스
     */
    private FragmentYearBinding binding;
    /**
     * 날짜 기본 포맷 yyyy-MM-dd 클래스
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CalendarFragment() {
        // Required empty public constructor
    }


    /**
     * 캘린더 프래그먼트 뷰가 만들어지면 실행되는 메소드
     */
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYearBinding.inflate(inflater, container, false);

        List<Walk> walkList = AppDatabase.getInstance(requireContext()).walkDao().getAll();

        Walk selectWalk = walkList.get(walkList.size() - 1);
        binding.tvWalkGoal.setText("걸음 목표: " + selectWalk.getGoal());
        binding.tvWalkCount.setText("걸음 수: " + selectWalk.getCount());
        binding.tvWalkTime.setText("걸은 시간: " + selectWalk.calculateHours());
        binding.tvWalkKcal.setText("칼로리 소모량: " + selectWalk.getKcal());
        binding.tvWalkKM.setText("걸은 거리: " + selectWalk.getDistance());


        binding.calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Walk selectWalk = null;
                for (Walk walk : walkList) {
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(Objects.requireNonNull(dateFormat.parse(walk.getDate())));
                        int walkYear = cal.get(Calendar.YEAR);
                        int walkMonth = cal.get(Calendar.MONTH) + 1;
                        int walkDay = cal.get(Calendar.DAY_OF_MONTH);
                        if (walkYear == year && walkMonth == month + 1 && walkDay == dayOfMonth) {
                            selectWalk = walk;
                            System.out.println(year + " " + (month + 1) + " " + dayOfMonth);
                            break;

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (selectWalk != null) {
                    binding.tvWalkGoal.setText("걸음 목표: " + selectWalk.getGoal());
                    binding.tvWalkCount.setText("걸음 수: " + selectWalk.getCount());
                    binding.tvWalkTime.setText("걸은 시간: " + selectWalk.calculateHours());
                    binding.tvWalkKcal.setText("칼로리 소모량: " + selectWalk.getKcal());
                    binding.tvWalkKM.setText("걸은 거리: " + selectWalk.getDistance());

                } else {
                    binding.tvWalkGoal.setText("걸음 목표: " + "데이터 없음");
                    binding.tvWalkCount.setText("걸음 수: " + "데이터 없음");
                    binding.tvWalkTime.setText("걸은 시간: " + "데이터 없음");
                    binding.tvWalkKcal.setText("칼로리 소모량: " + "데이터 없음");
                    binding.tvWalkKM.setText("걸은 거리: " + "데이터 없음");
                }
            }
        });

        return binding.getRoot();
    }
}