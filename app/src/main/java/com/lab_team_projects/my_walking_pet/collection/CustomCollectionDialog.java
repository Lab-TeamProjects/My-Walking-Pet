package com.lab_team_projects.my_walking_pet.collection;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.CustomCollectionDialogBinding;
import com.lab_team_projects.my_walking_pet.databinding.CustomPurchaseDialogBinding;
import com.lab_team_projects.my_walking_pet.databinding.CustomResultDialogBinding;

public class CustomCollectionDialog extends Dialog {
    private CustomCollectionDialogBinding binding;
    private final Context context;
    public CustomCollectionDialog(@NonNull Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CustomCollectionDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_yellow);
        // 스마트폰의 화면 크기를 가져옴
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int)(screenWidth * 0.9);
        params.height = (int)(screenHeight * 0.6);
        getWindow().setAttributes(params);
    }

}
