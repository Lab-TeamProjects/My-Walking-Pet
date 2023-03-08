package com.lab_team_projects.my_walking_pet.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.ServerConnection;
import com.lab_team_projects.my_walking_pet.databinding.ActivityLoginBinding;
import com.lab_team_projects.my_walking_pet.databinding.ActivitySignUpBinding;
import com.lab_team_projects.my_walking_pet.walk_count.WalkCountForeGroundService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private final String SIGN_IN = "login"; // URL 입력해야함

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", binding.etEmail.getText().toString());
                    jsonObject.put("password", binding.etPassWord.getText().toString());
                } catch (JSONException e) {
                    Log.e("JSONException : ", "btnLogin", e);
                }
                ServerConnection sc = new ServerConnection(SIGN_IN, jsonObject);
                sc.setClientCallBackListener((call, response) -> runOnUiThread(() -> {
                    if(response.isSuccessful()) {
                        try {
                            if(Objects.requireNonNull(response.body()).string().equals("올바른 응답 메시지")) { // 응답 메시지 입력해야함
                                /*
                                 유저가 로그인을 했을 때,
                                 DB에 사용자 정보가 있으면 MainActivity,
                                 그렇지 않으면 UserInfoEntryActivity 이동
                                 */
                            } else {
                                /*
                                로그인에 실패했을 때,
                                아이디는 놔두고 비밀번호를 초기화 하며
                                토스트 메시지로 "로그인에 실패했습니다." 출력
                                 */
                            }
                        } catch (IOException e) {
                            Log.e("IOException : ", "btnLogin", e);
                        }
                    } else {
                        Log.e("response failed : ", "btnLogin");
                    }
                }));
                };
            }
        );

        binding.tvFindPW.setOnClickListener(new View.OnClickListener() { // 비밀번호 찾기
            @Override
            public void onClick(View v) {   }
        });
    }


}