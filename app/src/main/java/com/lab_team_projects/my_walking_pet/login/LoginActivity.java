package com.lab_team_projects.my_walking_pet.login;

import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.LOGIN;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.LOGIN_TEST;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_AUTH_EMAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_CORRECT_PASSWORD;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_FOUND_EMAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.SUCCESS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.app.ServerConnection;
import com.lab_team_projects.my_walking_pet.databinding.ActivityLoginBinding;
import com.lab_team_projects.my_walking_pet.helpers.PermissionsCheckHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private PermissionsCheckHelper pch;

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
                    ServerConnection sc = new ServerConnection(LOGIN, jsonObject);
                    sc.setClientCallBackListener((call, response) -> runOnUiThread(() -> {
                        if(response.isSuccessful()) {
                            try {
                                JSONObject responseJson = new JSONObject(Objects.requireNonNull(response.body()).string());
                                String result = responseJson.getString("result");
                                Log.d("result : ", result);
                                Log.d("send Pw : ", binding.etPassWord.getText().toString());
                                switch(result) {
                                    case NOT_FOUND_EMAIL:
                                        /*
                                        아이디가 없는 경우
                                         */
                                        Toast.makeText(getApplicationContext(), "회원가입을 해주세요.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case NOT_AUTH_EMAIL:
                                        /*
                                        인증되지 않은 이메일 일 경우
                                         */
                                        Toast.makeText(getApplicationContext(), "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case NOT_CORRECT_PASSWORD:
                                        /*
                                        비밀번호가 틀린 경우
                                         */
                                        Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                        break;
                                    case SUCCESS:
                                        /*
                                        성공한 경우
                                         */
                                        permissionCheck(); // 권한 체크
                                        GameManager gm = GameManager.getInstance();
                                        gm.getUser().setAccessToken(responseJson.getString("access_token"));

                                        startActivity(new Intent(getApplicationContext(), UserInfoEntryActivity.class));
                                        finish();
                                        break;

                                    default:
                                        /*
                                        그 외
                                         */
                                        Toast.makeText(getApplicationContext(), "알 수 없는 이유로 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
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

    // 권한 체크 함수
    private void permissionCheck(){
        pch =  new PermissionsCheckHelper(this, this);

        if(!pch.checkPermission()){
            pch.requestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (!pch.permissionResult(requestCode, permissions, grantResults)){
            pch.requestPermission();
        }
    }
}