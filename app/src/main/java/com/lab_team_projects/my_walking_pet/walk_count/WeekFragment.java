package com.lab_team_projects.my_walking_pet.walk_count;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lab_team_projects.my_walking_pet.databinding.FragmentWeekBinding;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;
import com.lab_team_projects.my_walking_pet.helpers.BarChartHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * 일주일 단위로 저장되어 있는 걸음 수 통계를 보여줍니다.
 * 기기 저장소에 실제로 저장되어 있는 걸음 수는 있을 수도 있고 없을 수도 있기 떄문에
 * 없는 데이터를 더미 데이터로 만들어서 통계 수치에 활용합니다.
 * <p>
 * 사용자는 막대 차트를 드래그하여 일주일 단위로 걸음 정보에 대한 막대 차트를 볼 수 있습니다.
 */
public class WeekFragment extends Fragment {

    private FragmentWeekBinding binding;

    /**
     * Instantiates a new Week fragment.
     */
    public WeekFragment() {
        // Required empty public constructor
    }

    /**
     * 일주일 걸음 통계 프래그먼트가 생성되면 실행되는 메서드
     * 실제 저장되어 있는 걸음 객체와 더미 걸음 객체를 생성하고 막대 차트 데이터에 저장합니다.
     * 막대 차트는 저장된 데이터를 기반으로 사용자에게 일주일 단위 통계를 보여줍니다.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentWeekBinding.inflate(inflater, container, false);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Walk> walkList = AppDatabase.getInstance(requireContext()).walkDao().getAll();

        addEmptyDate(dateFormat, walkList);    // 날짜 사이사이 비어있는 날짜를 추가함

        sortedList(walkList, dateFormat);

        addNextEmptyDate(dateFormat, walkList);    // 7개로 나눌 수 있을 때 까지 날짜를 추가함

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

        binding.barChart.setOnChartGestureListener(new MyChartGestureListener(walkList, binding.barChart, binding.tvWalkCount, binding.tvWeekDate));

        return binding.getRoot();
    }

    /**
     * 실제 내부 저장소에 저장되어 있는 걸음 수를 순회하여 없는 날짜에 더미 걸음 객체를 생성하여
     * 사용자에게 보여줄 걸음 리스트에 추가합니다.
     * @param dateFormat 걸음 객체에 저장되어 있는 날짜 포맷
     * @param walkList 실제 내부 저장소에 저장되어 있는 걸음 객체 리스트
     */
    private void addNextEmptyDate(SimpleDateFormat dateFormat, List<Walk> walkList) {
        while (walkList.size() % 7 != 0) {

            Walk walk = walkList.get(walkList.size() - 1);

            try {
                Date nextDate = dateFormat.parse(walk.getDate());
                Calendar currentCal = Calendar.getInstance();
                currentCal.setTime(Objects.requireNonNull(nextDate));

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

                walkList.add(newWalk);
                sortedList(walkList, dateFormat);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 실제 리스트를 순회하여 날짜가 없는 데이터를 판단하고 데이터를 추가합니다.
     * 비교 대상의 걸음 날짜와 실제 그 다음 날짜와 비교 후 데이터 공백을 판단합니다.
     */
    private void addEmptyDate(SimpleDateFormat dateFormat, List<Walk> walkList) {
        int next;
        for (int i = 0; i < walkList.size(); i += next) {
            next = 1;
            int nextIndex = i + 1 < walkList.size() ? i + 1 : -1;
            if (nextIndex == -1) {
                break;
            }

            // 리스트 개수만큼 반복문을 돌려서 현재 걸음데이터와 다음 데이터를 비교한다
            Walk nextWalk = walkList.get(nextIndex);    // 다음 걸음 데이터는 현재 비교중인 데이터 + 1 인덱스
            Walk currentWalk = walkList.get(i);

            try {
                // 걸음 데이터에서 년, 월, 일을 파싱한다
                // 인덱스 + 1 한 비교 데이터
                // 결론적으로 실제 걸음 데이터가 있는 현재 walk의 다음 walk
                Date nextDate = dateFormat.parse(nextWalk.getDate());
                Calendar nextCal = Calendar.getInstance();
                nextCal.setTime(Objects.requireNonNull(nextDate));

                Date currentDate = dateFormat.parse(currentWalk.getDate());    // 리스트에서 빼온 현재 walk date
                Calendar currentCal = Calendar.getInstance();
                nextCal.add(Calendar.DATE, -1);

                while (true) {
                    // 현재 인덱스 walk 시간 설정

                    currentCal.setTime(Objects.requireNonNull(currentDate));

                    // 현재 걸음 데이터의 날짜와 비교 데이터와 같지 않으면
                    // 사이사이 비어있다는 뜻 -> 데이터를 만들어야한다

                    boolean isSameDate = nextCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) &&
                            nextCal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH) &&
                            nextCal.get(Calendar.DAY_OF_MONTH) == currentCal.get(Calendar.DAY_OF_MONTH);

                    if ((isSameDate && i + next != walkList.size())) {
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
                        walkList.add(newWalk);
                        sortedList(walkList, dateFormat);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 걸음 객체 리스트를 정렬하는 메서드입니다.
     * 걸음 객체를 정렬하는 비교 대상을 지정해줘야 합니다.
     */
    private void sortedList(List<Walk> walkList, SimpleDateFormat dateFormat) {
        walkList.sort((o1, o2) -> {
            try {
                Date date1 = dateFormat.parse(o1.getDate());
                Date date2 = dateFormat.parse(o2.getDate());
                return date1.compareTo(date2);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}