package com.lab_team_projects.my_walking_pet.login;

import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.GAME_DATA_VIEW;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.LOGIN;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_AUTH_EMAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_CORRECT_PASSWORD;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_FOUND_EMAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.PROFILE_VIEW;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.SUCCESS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.databinding.ActivityLoginBinding;
import com.lab_team_projects.my_walking_pet.helpers.PermissionsCheckHelper;
import com.lab_team_projects.my_walking_pet.helpers.ServerConnectionHelper;
import com.lab_team_projects.my_walking_pet.home.Animal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private PermissionsCheckHelper pch;

    private ActivityLoginBinding binding;

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
                    ServerConnectionHelper sc = new ServerConnectionHelper(LOGIN, jsonObject);
                    sc.setClientCallBackListener((call, response) -> runOnUiThread(() -> {
                        if(response.isSuccessful()) {
                            try {
                                JSONObject responseJson = new JSONObject(Objects.requireNonNull(response.body()).string());
                                String result = responseJson.getString("result");
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
                                        User user = gm.getUser();
                                        user.setAccessToken(responseJson.getString("access_token"));

                                        ServerConnectionHelper loadData = new ServerConnectionHelper(PROFILE_VIEW, user.getAccessToken());
                                        loadData.setClientCallBackListener((c,r) -> runOnUiThread(() -> {
                                            if (r.isSuccessful()) {
                                                try {
                                                    JSONObject data = new JSONObject(Objects.requireNonNull(r.body()).string());

                                                    //gm.loadUser()함수 수정해서 서버에서 가져온 데이터를 대입해야 함

                                                } catch (JSONException e) {
                                                    Log.e("loadData_user", "JSONException", e);
                                                } catch (IOException e) {
                                                    Log.e("loadData_user", "IOException", e);
                                                }
                                            }
                                        }));

                                        loadData = new ServerConnectionHelper(GAME_DATA_VIEW, user.getAccessToken());
                                        loadData.setClientCallBackListener((c, r) -> runOnUiThread(() -> {
                                            if (r.isSuccessful()) {
                                                try {
                                                    JSONObject data = new JSONObject(Objects.requireNonNull(r.body()).string());

                                                    String animalData = data.getString("animals");
                                                    List<Animal> animalList = new Gson().fromJson(animalData, new TypeToken<List<Animal>>(){}.getType());
                                                    user.setAnimalList(animalList);
                                                    /*
                                                    다른 게임 정보도 받아야 함
                                                     */

                                                } catch (JSONException e) {
                                                    Log.e("loadData_game", "JSONException", e);
                                                } catch (IOException e) {
                                                    Log.e("loadData_game", "IOException", e);
                                                }
                                            }
                                        }));

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
                }
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