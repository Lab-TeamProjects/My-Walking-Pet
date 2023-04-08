package com.lab_team_projects.my_walking_pet.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.R;

public class FindPasswordFragment extends Fragment {
    public FindPasswordFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_password, container, false);
        return view;
    }
}