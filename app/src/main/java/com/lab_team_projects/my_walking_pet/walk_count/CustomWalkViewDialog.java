package com.lab_team_projects.my_walking_pet.walk_count;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.lab_team_projects.my_walking_pet.databinding.CustomWalkViewDialogBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomWalkViewDialog extends Dialog {

    private Walk walk;

    public interface DialogCancelListener {
        void onDialogCancel();
    }

    private DialogCancelListener dialogCancelListener;

    public void setDialogCancelListener(DialogCancelListener dialogCancelListener) {
        this.dialogCancelListener = dialogCancelListener;
    }

    private CustomWalkViewDialogBinding binding;
    private WindowManager.LayoutParams params;

    public CustomWalkViewDialog(@NonNull Context context, Walk walk) {
        super(context);
        this.walk = walk;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CustomWalkViewDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        this.getWindow().setAttributes(params);

        /*
        * 임시
        * 현재 선택한 차트에 대한 설명을 다이얼로그 화면으로 표시함
        * */

        binding.tvWalkCount.setText(String.valueOf(walk.getCount()));
        binding.tvGoalCount.setText(String.valueOf(walk.getGoal()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date walkDate;
        try {
            walkDate = sdf.parse(walk.getDate());
            sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
            String time = sdf.format(walkDate);
            binding.tvTitle.setText(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.tvCalories.setText(String.valueOf(walk.getKcal()));
        binding.tvDistance.setText(String.format(Locale.getDefault(), "%.2f", walk.getDistance()));
        binding.tvTime.setText(walk.calculateHours());
    }

    @Override
    public void cancel() {
        super.cancel();
        dialogCancelListener.onDialogCancel();
    }
}
