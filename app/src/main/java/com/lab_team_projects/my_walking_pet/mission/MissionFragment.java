package com.lab_team_projects.my_walking_pet.mission;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.databinding.FragmentSettingBinding;

public class MissionFragment extends Fragment {

    private FragmentSettingBinding binding;
    private MainActivity mMainActivity;

    public MissionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        mMainActivity.onAppBarLoad();



        return inflater.inflate(R.layout.fragment_mission, container, false);
    }
}