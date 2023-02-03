package com.lab_team_projects.my_walking_pet.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSettingBinding.inflate(inflater, container, false);


        binding.tvSetting.setOnClickListener(v->{
            Intent intent = new Intent(requireContext(), SettingsActivity.class);
            startActivity(intent);
        });


        return binding.getRoot();
    }
}