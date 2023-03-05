package com.lab_team_projects.my_walking_pet.walk_count;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
import com.lab_team_projects.my_walking_pet.db.AppDatabase;
import com.lab_team_projects.my_walking_pet.login.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DayFragment extends Fragment {

    private FragmentDayBinding binding;

    private PieChart pieChart;
    private BarChart barChart;
    private GameManager gm = GameManager.getInstance();
    private Walk walk = gm.getWalk();
    List<Walk> walks;



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

        walks = AppDatabase.getInstance(requireContext()).walkDao().getAll();

        User user = gm.getUser();
        walk.setDistance(walk.calculateDistance(user));
        binding.tvKcalValue.setText(String.format(Locale.getDefault(),"%.2f", walk.getKcal()));
        binding.tvKmValue.setText(String.format(Locale.getDefault(),"%.2f", walk.getDistance()));

        binding.tvMinValue.setText(walk.calculateHours());

        setupPieChart();
        loadPieChartData();

        setupBarChart();
        loadBarData();


        binding.cdvPieChart.setOnClickListener(v -> {
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
                // 골을 화면에 표시해야함
            });

            builder.setNegativeButton("취소", (dialog, which) -> {
                dialog.cancel();
            });

            builder.show();
        });

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                CustomWalkViewDialog dialog = new CustomWalkViewDialog(requireContext(), walks.get((int) e.getX()));
                dialog.setDialogCancelListener(() -> barChart.highlightValue(null));
                dialog.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WalkViewModel walkViewModel = new ViewModelProvider(this).get(WalkViewModel.class);
        walkViewModel.getWalkLiveData().observe(getViewLifecycleOwner(), walk->{
            setPieChartCenterText(walk.getCount(), walk.getGoal());
        });
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

    private void setPieChartCenterText(int count, int goal) {
        String centerText = String.format(Locale.getDefault()
                ,"목표 걸음 수 %d\n%d", goal, count);
        int index = centerText.indexOf("\n");
        SpannableString spannableString = new SpannableString(centerText);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), 0, index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pieChart.setCenterText(spannableString);
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


        for (int i = 0; i < walks.size(); i++) {
            valueList.add(new BarEntry((float) i, walk.getCount()));
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