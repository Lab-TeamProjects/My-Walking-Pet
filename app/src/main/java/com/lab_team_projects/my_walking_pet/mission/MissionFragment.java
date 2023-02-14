package com.lab_team_projects.my_walking_pet.mission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.FragmentSettingBinding;

public class MissionFragment extends Fragment {

    private FragmentSettingBinding binding;

    public MissionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);


        return inflater.inflate(R.layout.fragment_mission, container, false);
    }
}