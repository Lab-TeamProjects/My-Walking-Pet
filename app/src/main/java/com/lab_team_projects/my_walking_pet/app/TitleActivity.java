package com.lab_team_projects.my_walking_pet.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lab_team_projects.my_walking_pet.databinding.ActivityTitleBinding;

/**
 * The type Title activity.
 */
public class TitleActivity extends AppCompatActivity {
    /**
     * The Binding.
     */
    ActivityTitleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTitleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}