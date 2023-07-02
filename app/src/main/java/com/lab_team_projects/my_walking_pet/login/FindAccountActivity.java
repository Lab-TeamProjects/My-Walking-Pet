package com.lab_team_projects.my_walking_pet.login;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.adapters.FindAccountAdapter;
import com.lab_team_projects.my_walking_pet.databinding.ActivityFindAccountBinding;
import com.lab_team_projects.my_walking_pet.databinding.ActivityLoginBinding;

/**
 * The type Find account activity.
 */
public class FindAccountActivity extends AppCompatActivity {

    private ActivityFindAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFindAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TabLayout tabLayout = binding.tabLayout;
        ViewPager2 viewPager = binding.viewPager;

        FindAccountAdapter adapter = new FindAccountAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("아이디 찾기");
                    break;
                case 1:
                    tab.setText("비밀번호 변경");
                    break;
                default:
                    break;
            }
        }).attach();

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}