package com.lab_team_projects.my_walking_pet.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lab_team_projects.my_walking_pet.databinding.ActivityEmailAuthNoticeBinding;

/**
 * The type Email auth notice activity.
 */
public class EmailAuthNoticeActivity extends AppCompatActivity {
    /**
     * The Binding.
     */
    ActivityEmailAuthNoticeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailAuthNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}