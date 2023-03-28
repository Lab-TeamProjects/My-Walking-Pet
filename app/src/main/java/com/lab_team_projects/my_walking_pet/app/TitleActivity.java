package com.lab_team_projects.my_walking_pet.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.ActivityProfileSettingBinding;
import com.lab_team_projects.my_walking_pet.databinding.ActivityTitleBinding;
import com.lab_team_projects.my_walking_pet.login.LoginActivity;
import com.lab_team_projects.my_walking_pet.login.SignUpActivity;

public class TitleActivity extends AppCompatActivity {
    ActivityTitleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTitleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}