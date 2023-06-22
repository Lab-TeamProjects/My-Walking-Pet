package com.lab_team_projects.my_walking_pet.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.CustomPetInfoDialogBinding;

/**
 * 펫 정보를 열람할 수 있는 커스텀 펫 정보 열람 다이얼로그
 * 다이얼로그 클래스를 상속 받음
 */
public class CustomPetInfoDialog extends Dialog {

    /**
     * 바인딩 클래스
     */
    private CustomPetInfoDialogBinding binding;
    /**
     * 메인 화면에 접근하기 위한 Context 클래스
     */
    private final Context context;

    public CustomPetInfoDialog(@NonNull Context context) {
        super(context);
        this.context = context;

        this.getWindow().setDimAmount(0);    // 배경 어두워지는 것 없애기
    }

    /**
     * 다이얼로그가 생성되면 실행되는 메서드
     * 사용자의 화면 크기에 비례하여 다이얼로그 크기도 변경
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CustomPetInfoDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_yellow);

        // 스마트폰의 화면 크기를 가져옴
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        this.getWindow().setLayout((int)(screenWidth * 0.9), (int)(screenHeight * 0.6));


        binding.btnCancel.setOnClickListener(v->{
            this.dismiss();
        });
    }
}
