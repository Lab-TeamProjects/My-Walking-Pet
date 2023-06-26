package com.lab_team_projects.my_walking_pet.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.R;

/**
 * The type Find email fragment.
 */
public class FindEmailFragment extends Fragment {

    /**
     * Instantiates a new Find email fragment.
     */
    public FindEmailFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_email, container, false);
        return view;
    }
}