package com.lab_team_projects.my_walking_pet.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.adapters.FindAccountAdapter;

/**
 * The type Find account activity.
 */
public class FindAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        FindAccountAdapter adapter = new FindAccountAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> {
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