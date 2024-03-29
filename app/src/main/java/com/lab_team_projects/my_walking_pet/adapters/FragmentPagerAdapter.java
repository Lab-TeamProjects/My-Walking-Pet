package com.lab_team_projects.my_walking_pet.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * 프래그먼트를 탭으로 설정하기 위한 어댑터
 */
public class FragmentPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragments;

    /**
     * Instantiates a new Fragment pager adapter.
     *
     * @param fragmentActivity the fragment activity
     * @param fragments        the fragments
     */
    public FragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
