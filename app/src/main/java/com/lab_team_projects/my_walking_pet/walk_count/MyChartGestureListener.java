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

/**
 * 막대 차트의 제스처를 감지하는 제스처 감지 리스너를 구현하는 클래스
 * 주간 통계 화면에서 일주일 별로 막대 차트를 넘기기 위한 리스너입니다.
 */
public class MyChartGestureListener implements OnChartGestureListener {

    private final List<Walk> walks;
    private final BarChart chart;
    private int count;
    private final TextView walkCount;
    private final TextView weekDate;
    private final DateTimeFormatter format =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * Instantiates a new My chart gesture listener.
     *
     * @param walkList    the walk list
     * @param barChart    the bar chart
     * @param tvWalkCount the tv walk count
     * @param tvWeekDate  the tv week date
     */
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

    /**
     * 일주일 단위 탭에서 막대 차트를 드래그하면 실행되는 메서드
     * 터치 좌표와 터치 종료 좌표를 계산하여 왼쪽, 오른쪽을 판단하고 일주일 단위로 막대 차트를 넘깁니다.
     */
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

    /**
     * 막대 차트를 드래그하면 상단의 현재 날짜 텍스트를 변경합니다.
     */
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
