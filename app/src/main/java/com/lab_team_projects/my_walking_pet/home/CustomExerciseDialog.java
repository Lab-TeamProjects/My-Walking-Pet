package com.lab_team_projects.my_walking_pet.home;

import static android.content.Context.BIND_AUTO_CREATE;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.CustomExerciseDialogBinding;

import java.util.Arrays;
import java.util.List;

public class CustomExerciseDialog extends Dialog {

    private CustomExerciseDialogBinding binding;
    private final Context context;
    private final List<Integer> exerciseTimes = Arrays.asList(30, 40, 50, 60);
    int selected = 30;


    public CustomExerciseDialog(@NonNull Context context) {
        super(context);
        this.context = context;

        this.getWindow().setDimAmount(0);    // 배경 어두워지는 것 없애기
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CustomExerciseDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_yellow);

        // 스마트폰의 화면 크기를 가져옴
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        this.getWindow().setLayout((int)(screenWidth * 0.9), (int)(screenHeight * 0.4));

        selected = Integer.parseInt(binding.tvTime.getText().toString());

        binding.btnPlus.setOnClickListener(v->{
            if (selected < 100) {
                selected += 10;
                binding.tvTime.setText(String.valueOf(selected));
            }
        });

        binding.btnMinus.setOnClickListener(v->{
            if (selected > 30) {
                selected -= 10;
                binding.tvTime.setText(String.valueOf(selected));
            }
        });

        binding.btnOK.setOnClickListener(v->{
            // ok 버튼
            selected = Integer.parseInt(binding.tvTime.getText().toString());
            Toast.makeText(context, String.valueOf(selected), Toast.LENGTH_SHORT).show();

            WalkingTimeCheckService service = new WalkingTimeCheckService(selected);
            Intent intent = new Intent(context, service.getClass());
            context.startService(intent);

            context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        });

        binding.btnCancel.setOnClickListener(v->{
            this.dismiss();
        });
    }

    private ServiceInterface serviceInterface;

    // 서비스 바인딩 처리
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceInterface = (ServiceInterface) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceInterface = null;
        }
    };
}
