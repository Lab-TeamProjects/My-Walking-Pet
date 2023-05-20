package com.lab_team_projects.my_walking_pet.walk_count;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.databinding.FragmentMonthBinding;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MonthFragment extends Fragment {

    private FragmentMonthBinding binding;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public MonthFragment() {
        // Required empty public constructor
    }

    private class MonthCount {
        String date;    // yy-MM
        int count;

        public MonthCount(String date, int count) {
            this.date = date;
            this.count = count;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMonthBinding.inflate(inflater, container, false);

        List<MonthCount> monthCounts = new ArrayList<>();
        List<Walk> walkList = AppDatabase.getInstance(requireContext()).walkDao().getAll();

        makeMonthCountsList(monthCounts, walkList);    // 각 월 별로 객체를 생성해서 리스트에 넣음
        setMonthCountsList(monthCounts, walkList);     // 내부 저장소에 저장되어있는 걸음 수를 리스트에 넣음

        binding.tvWalkCount.setText(String.valueOf(monthCounts.get(monthCounts.size() - 1).getCount()));
        binding.tvMonthDate.setText(monthCounts.get(monthCounts.size() - 1).getDate());

        barChartInit(binding.barChart);
        barChartSetData(binding.barChart, monthCounts);

        binding.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                binding.tvWalkCount.setText(String.valueOf(monthCounts.get((int) e.getX()).getCount()));
                binding.tvMonthDate.setText(monthCounts.get((int) e.getX()).getDate());
            }

            @Override
            public void onNothingSelected() {
                // 빈 메소드
            }
        });

        return binding.getRoot();
    }


    private void barChartSetData(BarChart barChart, List<MonthCount> monthCounts) {
        List<BarEntry> valueList = new ArrayList<>();
        String title = "걸음 수";

        List<String> dateList = new ArrayList<>();

        for (int i = 0; i < monthCounts.size(); i++) {
            valueList.add(new BarEntry(i, monthCounts.get(i).getCount()));
            String label = String.format(Locale.getDefault()
                    ,"%s %s"
                    , monthCounts.get(i).getDate().substring(0, 4)
                    , monthCounts.get(i).getDate().substring(5, 7));
            dateList.add(label);

        }

        BarDataSet barDataSet = new BarDataSet(valueList, title);
        barDataSet.setColors(rgb("#3CB371"));

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.1f);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dateList));

        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);

        barChart.setScaleMinima((float) data.getEntryCount() / 5, 1f ); // 막대차트 최대 보여주는 개수

        barChart.zoom(-10f,0f,0,0);
        barChart.setData(data);
        barChart.moveViewToX(monthCounts.size()-1);
        barChart.invalidate();
    }

    private void barChartInit(BarChart barChart) {
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setDrawBarShadow(false);
        barChart.setDrawBorders(false);

        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        barChart.animateY(1000);
        barChart.animateX(1000);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);

        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawTopYLabelEntry(true);

        rightAxis.setDrawAxisLine(true);
        rightAxis.setDrawLabels(true);
        rightAxis.setDrawZeroLine(true);
        rightAxis.setDrawLimitLinesBehindData(true);

        barChart.getLegend().setEnabled(false);
    }

    private void setMonthCountsList(List<MonthCount> monthCounts, List<Walk> walkList) {
        for (Walk walk : walkList) {

            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(Objects.requireNonNull(dateFormat.parse(walk.getDate())));
                String formattedDate = String.format(Locale.getDefault(),"%04d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);

                for (MonthCount monthCount : monthCounts) {
                    if (monthCount.getDate().equals(formattedDate)) {
                        monthCount.setCount(monthCount.getCount() + walk.getCount());
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeMonthCountsList(List<MonthCount> monthCounts, List<Walk> walkList) {
        Walk firstWalk = walkList.get(0); // 가장 예전에 저장된 데이터
        Walk lastWalk = walkList.get(walkList.size() - 1); // 가장 최근에 저장된 데이터

        try {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(Objects.requireNonNull(dateFormat.parse(firstWalk.getDate())));

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(Objects.requireNonNull(dateFormat.parse(lastWalk.getDate())));
            endCalendar.add(Calendar.MONTH, 1);

            do {
                String formattedDate = String.format(Locale.getDefault(),"%04d-%02d", startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH) + 1);
                MonthCount monthCount = new MonthCount(formattedDate, 0);
                monthCounts.add(monthCount);

                startCalendar.add(Calendar.MONTH, 1);

            } while (startCalendar.get(Calendar.YEAR) != endCalendar.get(Calendar.YEAR)
                    || startCalendar.get(Calendar.MONTH) != endCalendar.get(Calendar.MONTH));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}