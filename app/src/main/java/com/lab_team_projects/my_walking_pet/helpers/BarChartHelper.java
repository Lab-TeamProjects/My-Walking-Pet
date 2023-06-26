package com.lab_team_projects.my_walking_pet.helpers;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.lab_team_projects.my_walking_pet.walk_count.Walk;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 막대 차트를 생성하는데 각종 설정을 당담하는 클래스
 */
public class BarChartHelper {
    private final BarChart barChart;
    private final boolean isDay;

    /**
     * Instantiates a new Bar chart helper.
     *
     * @param barChart the bar chart
     * @param isDay    the is day
     */
    public BarChartHelper(BarChart barChart, boolean isDay) {
        this.barChart = barChart;
        this.isDay = isDay;
        initLayout(isDay);
    }

    /**
     * 막대 차트의 데이터를 설정합니다.
     *
     * @param walkList 데이터로 설정할 걸음 객체 리스트
     */
    public void setData(List<Walk> walkList) {
        List<BarEntry> valueList = new ArrayList<>();
        String title = "걸음 수";

        List<String> dateList = new ArrayList<>();

        for (int i = 0; i < walkList.size(); i++) {
            valueList.add(new BarEntry(i, walkList.get(i).getCount()));
            String label = String.format(Locale.getDefault()
                    ,"%s\n%s"
                    , walkList.get(i).getDate().substring(5, 7)
                    , walkList.get(i).getDate().substring(8, 10));
            dateList.add(label);

        }

        BarDataSet barDataSet = new BarDataSet(valueList, title);
        barDataSet.setColors(rgb("#3CB371"));

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.2f);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dateList));

        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);


        if (isDay) {
            barChart.setScaleMinima((float) data.getEntryCount() / 5, 1f ); // 막대차트 최대 보여주는 개수
        } else {
            barChart.setScaleMinima((float) data.getEntryCount() / 7, 1f ); // 막대차트 최대 보여주는 개수
        }
        barChart.zoom(-10f,0f,0,0);
        barChart.setData(data);
        barChart.moveViewToX(walkList.size()-1);
        barChart.invalidate();
    }

    /**
     * 막대 차트의 ui 설정 메서드
     *
     * @param isDay 막대 차트를 일일 통계 화면에서 보여주는지 판단하는 플래그 변수
     */
    public void initLayout(boolean isDay) {
        barChart.setScaleEnabled(false);
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
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        if (isDay) {
            barChart.setDragEnabled(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            leftAxis.setDrawAxisLine(false);
            leftAxis.setDrawZeroLine(true);

            rightAxis.setDrawAxisLine(false);
            rightAxis.setDrawLabels(false);

        } else {
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
            xAxis.setDrawLabels(true);

            barChart.setDragEnabled(false);
            leftAxis.setDrawAxisLine(true);
            leftAxis.setDrawTopYLabelEntry(true);

            rightAxis.setDrawAxisLine(true);
            rightAxis.setDrawLabels(true);
            rightAxis.setDrawZeroLine(true);
            rightAxis.setDrawLimitLinesBehindData(true);
        }
        barChart.getLegend().setEnabled(false);
    }
}
