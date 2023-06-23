package com.lab_team_projects.my_walking_pet.walk_count;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.databinding.FragmentDayBinding;
import com.lab_team_projects.my_walking_pet.db.AppDatabase;
import com.lab_team_projects.my_walking_pet.helpers.BarChartHelper;
import com.lab_team_projects.my_walking_pet.helpers.PieChartHelper;
import com.lab_team_projects.my_walking_pet.login.User;

import java.util.List;
import java.util.Locale;

/**
 * 일일 걸음 정보를 보기 위한 프래그먼트입니다.
 */
public class DayFragment extends Fragment {
    private final GameManager gm = GameManager.getInstance();
    private final User user = gm.getUser();
    private List<Walk> walks;
    private Walk walk;
    /**
     * 파이 차트를 이용하여 당일 걸음 수를 시각적으로 볼 수 있습니다.
     */
    private PieChartHelper pieChartHelper;
    /**
     * 일일 단위로 실제 사용자 기기에 저장되어있는 걸음 정보를 따로 볼 수 있습니다.
     */
    private BarChartHelper barchartHelper;

    public DayFragment() {
        // Required empty public constructor
    }

    /**
     * 기기 내부 저장소에 저장되어있는 걸음 정보를 불러와서 파이차트와 바차트에 데이터를 설정하고
     * 사용자가 걸음 정보를 시각적으로 볼 수 있도록 설정합니다.
     * 파이 차트를 클릭하여 일일 목표를 설정할 수 있고 바 차트를 클릭하여 걸음 정보 다이얼로그를 표시합니다.
     * 사용자가 걷고 있을 떄 실시간으로 변화되는 값을 볼 수 있도록 뷰모델 라이브 데이터 클래스를 이용하여
     * 걸음 카운트가 저장되는 화면을 실시간으로 볼 수 있습니다.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 바인딩 객체를 반환합니다.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.lab_team_projects.my_walking_pet.databinding.FragmentDayBinding binding = FragmentDayBinding.inflate(inflater, container, false);

        WalkViewModel walkViewModel = new ViewModelProvider(this).get(WalkViewModel.class);
        walkViewModel.getWalkLiveData().observe(getViewLifecycleOwner(), walk->{
            this.walk = walk;

            if (pieChartHelper != null && barchartHelper != null) {
                pieChartHelper.setData(walk);
                barchartHelper.setData(walks);
            }
        });

        AppDatabase db = AppDatabase.getInstance(requireContext());
        walks = db.walkDao().getAll();    // 일단 내부저장소에 저장되어있는 Walk 데이터들을 리스트로 만든다.

        walk = walks.get(walks.size() - 1);    // walks 리스트 마지막에는 오늘 날짜의 walk가 저장되어 있기 때문에 가져온다.
        binding.tvKcalValue.setText(String.format(Locale.getDefault(),"%.2f", walk.getKcal()));    // 오늘 칼로리 소모량을 화면에 표시한다.
        binding.tvKmValue.setText(String.format(Locale.getDefault(),"%.2f", walk.getDistance()));    // 오늘 걸은 거리를 화면에 표시한다.
        binding.tvMinValue.setText(walk.calculateHours());    // 오늘 걸은 시간을 화면에 표시한다.

        pieChartHelper = new PieChartHelper(binding.pieChart);
        pieChartHelper.setData(walk);

        barchartHelper = new BarChartHelper(binding.barChart, true);
        barchartHelper.setData(walks);


        binding.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                CustomWalkViewDialog dialog = new CustomWalkViewDialog(requireContext(), walks.get((int) e.getX()));
                dialog.setDialogCancelListener(() -> binding.barChart.highlightValue(null));
                dialog.show();
            }

            @Override
            public void onNothingSelected() {
                // 빈 메소드
            }
        });

        binding.pieChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("목표 걸음 수 설정");
                builder.setMessage("목표 걸음 수를 설정해주세요");

                final EditText editText = new EditText(requireContext());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(editText);

                builder.setPositiveButton("설정", (dialog, which) -> {
                    int goal = Integer.parseInt(editText.getText().toString());
                    if (goal < 1000) {
                        Toast.makeText(requireContext(), "목표 걸음 수는 1000이하로 설정할 수 없습니다!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    walk.setGoal(goal);
                    AppDatabase db = AppDatabase.getInstance(requireContext());
                    db.walkDao().update(walk);
                });

                builder.setNegativeButton("취소", (dialog, which) -> {
                    dialog.cancel();
                });

                builder.show();
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });

        return binding.getRoot();
    }
}