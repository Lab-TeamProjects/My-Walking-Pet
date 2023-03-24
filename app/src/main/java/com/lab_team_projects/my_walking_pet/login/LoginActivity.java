package com.lab_team_projects.my_walking_pet.login;

import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_AUTH_EMAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_CORRECT_PASSWORD;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_FOUND_EMAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.SUCCESS;

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
                } catch (JSONException e) { Log.e("JSONException : ", "btnLogin", e); }
                ServerConnection sc = new ServerConnection(SIGN_IN, jsonObject);
                sc.setClientCallBackListener((call, response) -> runOnUiThread(() -> {
                    if(response.isSuccessful()) {
                        try {
                            JSONObject responseJson = new JSONObject(response.body().string());
                            String result = responseJson.getString("result");
                            switch(result) {
                                case NOT_FOUND_EMAIL:
    /*
    아이디가 없는 경우
     */
                                    break;
                                case NOT_AUTH_EMAIL:
    /*
    인증되지 않은 이메일 일 경우
     */
                                    break;
                                case NOT_CORRECT_PASSWORD:
    /*
    비밀번호가 틀린 경우
     */
                                    break;
                                case SUCCESS:
    /*
    성공한 경우
     */
                                    break;
                                default:
    /*
    그 외
     */
                                    break;
                            }
                        }
                        catch (JSONException e) { Log.e("JSONException", "btnLogin", e); }
                        catch (IOException e) { Log.e("IOException", "btnLogin", e); }
                    } else { Log.e("response failed : ", "btnLogin"); }
                }));
            };
        }
        );

        binding.tvFindPW.setOnClickListener(new View.OnClickListener() { // 비밀번호 찾기
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FindAccountActivity.class));
            }
        });
    }


}