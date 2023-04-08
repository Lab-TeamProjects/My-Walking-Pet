package com.lab_team_projects.my_walking_pet.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.lab_team_projects.my_walking_pet.databinding.ActivityProfileSettingBinding;

public class ProfileSettingActivity extends AppCompatActivity {

    ActivityProfileSettingBinding binding;

    ImageButton ibSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}