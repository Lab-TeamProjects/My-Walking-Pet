package com.lab_team_projects.my_walking_pet.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.SendData;
import com.lab_team_projects.my_walking_pet.app.ServerRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {
    private Button btnSignUp;
    private Button btnDuplicationCheck;
    private TextView tvDuplicationResult;
    private static final String urls = "http://203.232.193.164:9999/sign-up"; // flask 호출 url
    private static final String url = "check-email-duplication";
    private EditText input_id, input_pwd; // 아이디 비밀번호 이름 받아오기위한 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        input_id = findViewById(R.id.tbID);
        input_pwd = findViewById(R.id.tbPW);
        btnDuplicationCheck = findViewById(R.id.btnDuplicationCheck);
        tvDuplicationResult = findViewById(R.id.tvDuplicationResult);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnDuplicationCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", input_id.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ServerRequest serverRequest = new ServerRequest(url, jsonObject);
                serverRequest.setClientCallBackListener((call, response) -> runOnUiThread(() -> {
                    if(response.isSuccessful()) {
                        try {
                            if(Objects.requireNonNull(response.body()).string().equals("사용가능한 이메일")) {
                                tvDuplicationResult.setText("사용가능한 이메일");
                            } else {
                                tvDuplicationResult.setText("중복된 이메일");
                            }
                        } catch (IOException e) {
                            Log.e("Email Duplication : ", "이메일 중복", e);
                        }
                    } else {
                        tvDuplicationResult.setText("서버 응답에 실패했습니다.");
                        Log.e("Email Duplication - else : ", "응답 실패");
                    }
                }));

                /*JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("email",  input_id.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SendData sendData = new SendData(jsonObject, urls);
                sendData.execute();*/
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("email",  input_id.getText().toString());
                    jsonObject.put("password",  input_pwd.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SendData sendData = new SendData(jsonObject, urls);
                sendData.execute();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}