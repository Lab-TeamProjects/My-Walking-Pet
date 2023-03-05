package com.lab_team_projects.my_walking_pet.walk_count;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.databinding.FragmentDayBinding;
import com.lab_team_projects.my_walking_pet.login.User;

import java.util.ArrayList;
import java.util.Locale;

public class DayFragment extends Fragment {

    private FragmentDayBinding binding;

    private PieChart pieChart;
    private BarChart barChart;


    int goalCount = 123, nowCount = 4000;

    public DayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDayBinding.inflate(inflater, container, false);
        pieChart = binding.pieChart;
        barChart = binding.barChart;


        GameManager gm = GameManager.getInstance();
        User user = gm.getUser();
        Walk walk = gm.getWalk();
        nowCount = walk.getCount();
        walk.setDistance(walk.calculateDistance(user));
        double kcal = walk.calculateKcal(user);
        binding.tvKcalValue.setText(String.format(Locale.getDefault(),"%.2f", kcal));
        binding.tvKmValue.setText(String.format(Locale.getDefault(),"%.2f", walk.getDistance()));

        binding.tvMinValue.setText(walk.calculateHours());

        setupPieChart();
        loadPieChartData();

        setupBarChart();
        loadBarData();

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                CustomWalkViewDialog dialog = new CustomWalkViewDialog(requireContext());
                dialog.setDialogCancelListener(() -> {
                    barChart.highlightValue(null);
                });


                dialog.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });



        return binding.getRoot();
    }


    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(90f);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterTextSize(Utils.convertDpToPixel(12f));
        pieChart.getRenderer().getPaintRender().setTextAlign(Paint.Align.CENTER);
        pieChart.setCenterTextOffset(0f, -15f);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHighlightPerTapEnabled(false);
    }


    /*
     * 아래 차트 부분을 클래스로 옮겨야할지 나중에 고려해야함
     * */

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.5f));
        entries.add(new PieEntry(0.4f));

        final int[] MY_COLORS = {rgb("#3CB371"), rgb("#F2F3F5")};
        ArrayList<Integer> colors = new ArrayList<>();
        for(int i: MY_COLORS) {
            colors.add(i);
        }

        PieDataSet dataSet = new PieDataSet(entries, "walk");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        data.setValueFormatter(new PercentFormatter(pieChart));

        // 중앙 텍스트 설정
        // 임시데이터
        String centerText = String.format("목표 걸음 수 %d\n%d", goalCount, nowCount);
        int index = centerText.indexOf("\n");
        SpannableString spannableString = new SpannableString(centerText);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), 0, index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pieChart.setCenterText(spannableString);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void setupBarChart() {
        barChart.setScaleEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawBorders(false);

        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        barChart.animateY(1000);
        barChart.animateX(1000);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setDrawLabels(false);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

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
        barChart.zoom(-10f,0f,0,0);
        barChart.setData(data);
        barChart.invalidate();
    }
}