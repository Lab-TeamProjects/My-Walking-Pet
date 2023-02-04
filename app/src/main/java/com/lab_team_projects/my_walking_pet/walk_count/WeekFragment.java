package com.lab_team_projects.my_walking_pet.walk_count;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.FragmentWeekBinding;

import java.util.ArrayList;

public class WeekFragment extends Fragment {

    private FragmentWeekBinding binding;
    private BarChart barChart;

    public WeekFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWeekBinding.inflate(inflater, container, false);
        barChart = binding.barChart;

        setupBarChart();
        loadBarData();

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    private void setupBarChart() {
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawBorders(false);
        barChart.setScaleEnabled(false);

        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        barChart.animateY(1000);
        barChart.animateX(1000);

        XAxis xBottomAxis = barChart.getXAxis();
        xBottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xBottomAxis.setGranularity(1f);
        xBottomAxis.setDrawAxisLine(false);
        xBottomAxis.setDrawGridLines(false);
        xBottomAxis.setDrawLabels(true);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawTopYLabelEntry(true);


        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawAxisLine(true);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(true);
        rightAxis.setDrawZeroLine(true);
        rightAxis.setDrawLimitLinesBehindData(true);
        barChart.getLegend().setEnabled(false);

    }

    private void loadBarData() {
        ArrayList<BarEntry> valueList = new ArrayList<>();
        String title = "걸음 수";

        // 임의 데이터
        for (int i = 0; i<30; i++) {
            valueList.add(new BarEntry((float)i, i * 100f));
        }

        BarDataSet barDataSet = new BarDataSet(valueList, title);
        barDataSet.setColors(rgb("#3CB371"));

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.2f);

        barChart.setScaleMinima((float) data.getEntryCount() / 5, 1f ); // 막대차트 최대 보여주는 개수
        barChart.zoom(-10f,0,0,0);

        barChart.setData(data);
        barChart.invalidate();
    }
}