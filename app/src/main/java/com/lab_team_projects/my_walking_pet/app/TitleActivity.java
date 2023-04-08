package com.lab_team_projects.my_walking_pet.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lab_team_projects.my_walking_pet.databinding.ActivityTitleBinding;

public class TitleActivity extends AppCompatActivity {
    ActivityTitleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTitleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}