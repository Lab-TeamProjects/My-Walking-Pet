package com.lab_team_projects.my_walking_pet.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.CustomResultDialogBinding;

/**
 * 결과를 화면에 보여주기 위한 커스텀 다이얼로그 클래스
 * 다이얼로그 클래스를 상속 받음
 */
public class CustomResultDialog extends Dialog {

    private final Context context;
    private CustomResultDialogBinding binding;

    /**
     * Instantiates a new Custom result dialog.
     *
     * @param context the context
     */
    public CustomResultDialog(@NonNull Context context) {
        super(context);
        this.context = context;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CustomResultDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setWindowPixels();

    }

    private void setWindowPixels() {
        // 스마트폰의 화면 크기를 가져옴
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        this.getWindow().setLayout((int)(screenWidth * 0.9), (int)(screenHeight * 0.5));
        getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_yellow);

    }
}
