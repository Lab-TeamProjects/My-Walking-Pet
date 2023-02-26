package com.lab_team_projects.my_walking_pet.help;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;

import com.lab_team_projects.my_walking_pet.R;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        c = findViewById(R.id.clHelp);
        c.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.clHelp) {
            onBackPressed();
        }
    }

}