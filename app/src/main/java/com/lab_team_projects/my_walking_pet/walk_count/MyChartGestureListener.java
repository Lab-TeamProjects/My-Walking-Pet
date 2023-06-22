package com.lab_team_projects.my_walking_pet.walk_count;

import android.view.MotionEvent;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public class MyChartGestureListener implements OnChartGestureListener {

    private final List<Walk> walks;
    private final BarChart chart;
    private int count;
    private final TextView walkCount;
    private final TextView weekDate;
    private final DateTimeFormatter format =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public MyChartGestureListener(List<Walk> walkList, BarChart barChart, TextView tvWalkCount, TextView tvWeekDate) {
        this.walks = walkList;
        this.chart = barChart;
        this.count = walkList.size() - 7;
        this.walkCount = tvWalkCount;
        this.weekDate = tvWeekDate;

        invalidateTextView();
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {


    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        int moveCount = 7;
        float startX = me1.getX();
        float endX = me2.getX();
        float distanceX = endX - startX;

        if (distanceX > 0) {
            int moveIndex = count - moveCount;
            if (moveIndex >= 0) {
                count = moveIndex;
            }
        } else {
            int moveIndex = count + moveCount;
            if (moveIndex < walks.size() - 1) {
                count = moveIndex;
            }
        }

        chart.moveViewToX(count - 0.5F);

        invalidateTextView();

    }

    private void invalidateTextView() {
        int totalCount = IntStream.range(count, count + 7).mapToObj(i -> walks.get(i)).mapToInt(Walk::getCount).sum();
        totalCount = totalCount / 7;

        StringBuilder currentWeek = new StringBuilder();

        String startDate = walks.get(count).getDate();
        String endDate = walks.get(count + 6).getDate();

        LocalDateTime startDateTime = LocalDateTime.parse(startDate, format);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, format);
        LocalDateTime[] localDateTimes = {startDateTime, endDateTime};

        for (LocalDateTime localDateTime : localDateTimes) {
            int year = localDateTime.getYear();
            int month = localDateTime.getMonthValue();
            int day = localDateTime.getDayOfMonth();
            currentWeek.append(String.format(Locale.getDefault(), "%d년 %d월 %d일 ~ ", year, month, day));
        }

        currentWeek.delete(currentWeek.length()-3, currentWeek.length()-1);

        walkCount.setText(String.valueOf(totalCount));
        weekDate.setText(currentWeek.toString());
    }


    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }
}
