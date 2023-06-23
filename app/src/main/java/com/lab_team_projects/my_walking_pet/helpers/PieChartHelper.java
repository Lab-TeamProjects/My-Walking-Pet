package com.lab_team_projects.my_walking_pet.helpers;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.lab_team_projects.my_walking_pet.walk_count.Walk;

import java.util.ArrayList;
import java.util.Locale;

/**
 * 사용자의 일일 걸음 수를 파이차트를 이용한 시각적 표현을 위한 클래스
 */
public class PieChartHelper {

    private final PieChart pieChart;

    public PieChartHelper(PieChart pieChart) {
        this.pieChart = pieChart;

        initLayout();
    }

    /**
     * 파이 차트의 ui 레이아웃을 설정합니다.
     */
    public void initLayout() {
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

    /**
     * 일일 걸음 클래스에 해당하는 정보를 파이차트 데이터로 설정합니다.
     * @param walk 일일 걸음 정볼를 받습니다.
     */
    public void setData(Walk walk) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float) walk.getCount() / walk.getGoal()));
        entries.add(new PieEntry((float) 1 - (float) walk.getCount() / walk.getGoal()));

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

        setPieChartCenterText(walk.getCount(), walk.getGoal());
        pieChart.setData(data);
        pieChart.invalidate();
    }

    /**
     * 일일 목표와 현재 걸음 수를 두 줄로 표현하기 위한 메서드입니다.
     * spannable string 클래스를 이용합니다.
     */
    private void setPieChartCenterText(int count, int goal) {
        String centerText = String.format(Locale.getDefault()
                ,"목표 걸음 수 %d\n%d", goal, count);
        int index = centerText.indexOf("\n");
        SpannableString spannableString = new SpannableString(centerText);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), 0, index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pieChart.setCenterText(spannableString);
    }
}
