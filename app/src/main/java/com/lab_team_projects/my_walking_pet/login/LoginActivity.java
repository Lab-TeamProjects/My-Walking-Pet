package com.lab_team_projects.my_walking_pet.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.walk_count.WalkCountForeGroundService;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin = findViewById(R.id.btnLogin);
    TextView tvFindPW = findViewById(R.id.tvFindPW);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   }
        });

        tvFindPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   }
        });
    }


}