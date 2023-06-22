package com.lab_team_projects.my_walking_pet.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.CustomExerciseDialogBinding;
import com.lab_team_projects.my_walking_pet.databinding.CustomExercisingDialogBinding;

public class CustomExerciseDialog extends Dialog {

    private CustomExerciseDialogBinding exerciseBinding;
    private CustomExercisingDialogBinding exercisingBinding;
    private final Context context;
    int selected = 30;
    private final MyBinder myBinder;

    private final boolean isExercising;
    private OnExerciseListener onExerciseListener;

    public void setOnExerciseListener(OnExerciseListener onExerciseListener) {
        this.onExerciseListener = onExerciseListener;
    }

    public interface OnExerciseListener {
        void onExercise(boolean flag, int selected);
    }

    public CustomExerciseDialog(@NonNull Context context, boolean isExercising, MyBinder svc) {
        super(context);
        this.context = context;
        this.getWindow().setDimAmount(0);    // 배경 어두워지는 것 없애기
        this.isExercising = isExercising;
        this.myBinder = svc;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isExercising) {
            exerciseBinding = CustomExerciseDialogBinding.inflate(getLayoutInflater());
            setContentView(exerciseBinding.getRoot());

            getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_yellow);
            setWindowPixels();
            selected = Integer.parseInt(exerciseBinding.tvTime.getText().toString());

            exerciseBinding.btnPlus.setOnClickListener(v->{
                if (selected < 100) {
                    selected += 10;
                    exerciseBinding.tvTime.setText(String.valueOf(selected));
                }
            });

            exerciseBinding.btnMinus.setOnClickListener(v->{
                if (selected > 30) {
                    selected -= 10;
                    exerciseBinding.tvTime.setText(String.valueOf(selected));
                }
            });

            exerciseBinding.tvOK.setOnClickListener(v->{
                // ok 버튼
                selected = Integer.parseInt(exerciseBinding.tvTime.getText().toString());
                Toast.makeText(context, String.valueOf(selected), Toast.LENGTH_SHORT).show();
                onExerciseListener.onExercise(true, selected);
                dismiss();
            });

            exerciseBinding.tvCancel.setOnClickListener(v->{
                dismiss();
            });

            exerciseBinding.tvResult.setOnClickListener(v->{
                CustomResultDialog customResultDialog = new CustomResultDialog(context);
                customResultDialog.show();

            });

        } else {
            exercisingBinding = CustomExercisingDialogBinding.inflate(getLayoutInflater());
            setContentView(exercisingBinding.getRoot());

            getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_yellow);
            setWindowPixels();

            exercisingBinding.tvCurrent.setText(myBinder.getState());

            myBinder.setStateListener(new MyBinder.OnBinderStateListener() {
                @Override
                public void onChange(String state) {
                    exercisingBinding.tvCurrent.setText(state);
                }
            });

        }
    }

    private void setWindowPixels() {
        // 스마트폰의 화면 크기를 가져옴
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        this.getWindow().setLayout((int)(screenWidth * 0.9), (int)(screenHeight * 0.5));
    }

}
