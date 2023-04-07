package com.lab_team_projects.my_walking_pet.home;

import static android.content.Context.BIND_AUTO_CREATE;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.CustomExerciseDialogBinding;
import com.lab_team_projects.my_walking_pet.databinding.CustomExercisingDialogBinding;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class CustomExerciseDialog extends Dialog {

    private CustomExerciseDialogBinding exerciseBinding;
    private CustomExercisingDialogBinding exercisingBinding;

    private final Context context;
    private final List<Integer> exerciseTimes = Arrays.asList(30, 40, 50, 60);
    int selected = 30;

    private boolean isExercising;

    private ServiceInterface serviceInterface;

    private OnExerciseListener onExerciseListener;

    public void setOnExerciseListener(OnExerciseListener onExerciseListener) {
        this.onExerciseListener = onExerciseListener;
    }

    public interface OnExerciseListener {
        void exercise(boolean flag, int selected);
    }

    public CustomExerciseDialog(@NonNull Context context, boolean isExercising) {
        super(context);
        this.context = context;
        this.getWindow().setDimAmount(0);    // 배경 어두워지는 것 없애기
        this.isExercising = isExercising;
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

            exerciseBinding.btnOK.setOnClickListener(v->{
                // ok 버튼
                selected = Integer.parseInt(exerciseBinding.tvTime.getText().toString());
                Toast.makeText(context, String.valueOf(selected), Toast.LENGTH_SHORT).show();
                onExerciseListener.exercise(true, selected);
                dismiss();
            });

            exerciseBinding.btnCancel.setOnClickListener(v->{
                dismiss();
            });

        } else {
            exercisingBinding = CustomExercisingDialogBinding.inflate(getLayoutInflater());
            setContentView(exercisingBinding.getRoot());
            setWindowPixels();
        }
    }

    private void setWindowPixels() {
        // 스마트폰의 화면 크기를 가져옴
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        this.getWindow().setLayout((int)(screenWidth * 0.9), (int)(screenHeight * 0.4));
    }

}
