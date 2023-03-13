package com.lab_team_projects.my_walking_pet.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lab_team_projects.my_walking_pet.login.FindEmailFragment;
import com.lab_team_projects.my_walking_pet.login.FindPasswordFragment;

public class FindAccountAdapter extends FragmentStateAdapter {

    public FindAccountAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new FindEmailFragment();
            case 1: return new FindPasswordFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
