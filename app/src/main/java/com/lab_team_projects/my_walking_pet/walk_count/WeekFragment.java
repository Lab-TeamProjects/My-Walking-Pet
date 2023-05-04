package com.lab_team_projects.my_walking_pet.walk_count;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.FragmentWeekBinding;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;
import com.lab_team_projects.my_walking_pet.helpers.BarChartHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeekFragment extends Fragment {

    private FragmentWeekBinding binding;

    public WeekFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWeekBinding.inflate(inflater, container, false);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Walk> walkList = AppDatabase.getInstance(requireContext()).walkDao().getAll();

        /*
            7일 평균을 내려고 하니
            날짜간에 비어있을 경우를 생각해야한다
            그래서 날짜와 날짜 사이 비어있을 경우 걸음수를 0으로 해서 새로운 걸음 데이터를 넣어준다
            하지만 이렇게 만든 리스트는 db에 업데이트 하지 않고 보여줄때만 사용한다
         */

        int next = 1;
        for (int i = 0; i < walkList.size(); i += next) {
            int nextIndex = i + 1 < walkList.size() ? i + 1 : -1;
            if (nextIndex == -1) {
                break;
            }

            // 리스트 개수만큼 반복문을 돌려서 현재 걸음데이터와 다음 데이터를 비교한다
            Walk nextWalk = walkList.get(nextIndex);    // 다음 걸음 데이터는 현재 비교중인 데이터 + 1 인덱스
            Walk currentWalk = walkList.get(i);

            try {
                // 걸음 데이터에서 년, 월, 일을 파싱한다
                Date nextDate = dateFormat.parse(nextWalk.getDate());
                Calendar nextCal = Calendar.getInstance();
                nextCal.setTime(nextDate);
                // 다음 데이터에서 하루를 빼고 비교 데이터로 만든다
                //nextCal.add(Calendar.DATE, -1);

                Calendar currentCal = Calendar.getInstance();

                Date currentDate = dateFormat.parse(currentWalk.getDate());
                while (true) {
                    currentCal.setTime(currentDate);
                    // 현재 걸음 데이터의 날짜와 비교 데이터와 같지 않으면
                    // 사이사이 비어있다는 뜻

                    boolean isSameDate = nextCal.get(Calendar.YEAR) <= currentCal.get(Calendar.YEAR) &&
                            nextCal.get(Calendar.MONTH) <= currentCal.get(Calendar.MONTH) &&
                            nextCal.get(Calendar.DAY_OF_MONTH) <= currentCal.get(Calendar.DAY_OF_MONTH);

                    if (isSameDate && walkList.size() % 7 == 0) {
                        break;
                    } else {


                        // 현재 데이터에 하루를 더한다
                        currentCal.add(Calendar.DATE, 1);

                        // 새로운 데이터를 만들어준다
                        String newDate = String.format(Locale.getDefault()
                                , "%04d-%02d-%02d 00:00:00"
                                , currentCal.get(Calendar.YEAR)
                                , currentCal.get(Calendar.MONTH) + 1
                                , currentCal.get(Calendar.DATE));
                        Walk newWalk = new Walk();
                        newWalk.setDate(newDate);

                        // 새로 만든 날짜 스트링을 date
                        currentDate = dateFormat.parse(newWalk.getDate());

                        next++;

                        if (nextCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) &&
                                nextCal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH) &&
                                nextCal.get(Calendar.DAY_OF_MONTH) == currentCal.get(Calendar.DAY_OF_MONTH)) {
                            continue;
                        }

                        walkList.add(newWalk);


                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        walkList.sort(new Comparator<Walk>() {
            @Override
            public int compare(Walk o1, Walk o2) {
                try {
                    Date date1 = dateFormat.parse(o1.getDate());
                    Date date2 = dateFormat.parse(o2.getDate());
                    return date1.compareTo(date2);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        BarChartHelper barChartHelper = new BarChartHelper(binding.barChart, false);
        barChartHelper.setData(walkList);

        binding.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                CustomWalkViewDialog dialog = new CustomWalkViewDialog(requireContext(), walkList.get((int) e.getX()));
                dialog.setDialogCancelListener(() -> binding.barChart.highlightValue(null));
                dialog.show();
            }

            @Override
            public void onNothingSelected() {
                // 빈 메소드
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}