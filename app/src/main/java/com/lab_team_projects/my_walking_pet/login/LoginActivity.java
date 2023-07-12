package com.lab_team_projects.my_walking_pet.login;

import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.FAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.GET_EGGS;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.GET_PETS;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.GET_USER_ITEM;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.INVALID_ACCESS_TOKEN;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.LOGIN;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.MONEY;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_AUTH_EMAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_CORRECT_PASSWORD;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_FOUND_EMAIL;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.NOT_FOUND_PROFILE;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.PROFILE_SETTING;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.STEPS;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.SUCCESS;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.databinding.ActivityLoginBinding;
import com.lab_team_projects.my_walking_pet.helpers.PermissionsCheckHelper;
import com.lab_team_projects.my_walking_pet.helpers.ServerConnectionHelper;
import com.lab_team_projects.my_walking_pet.home.Animal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Request;

/**
 * The type Login activity.
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
public class LoginActivity extends AppCompatActivity {

    private PermissionsCheckHelper pch;
    private GameManager gm;
    private User user;

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
                    sc.setClientCallBackListener((call, response) -> {
                        if(response.isSuccessful()) {
                            try {
                                JSONObject responseJson = new JSONObject(Objects.requireNonNull(response.body()).string());
                                String result = responseJson.getString("result");
                                switch(result) {
                                    case NOT_FOUND_EMAIL:
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "회원가입을 해주세요.", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                        break;
                                    case NOT_AUTH_EMAIL:
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                    case NOT_CORRECT_PASSWORD:
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                    case SUCCESS:
                                        /*
                                        성공한 경우
                                         */
                                        permissionCheck(); // 권한 체크
                                        gm = GameManager.getInstance();
                                        user = gm.getUser();
                                        user.setAccessToken(responseJson.getString("access_token"));

                                        ServerConnectionHelper has_profile = new ServerConnectionHelper(PROFILE_SETTING, user.getAccessToken());
                                        has_profile.setClientCallBackListener(((call1, response1) -> {
                                            try {
                                                String e = Objects.requireNonNull(response1.body()).string();
                                                Log.e("문제", e);
                                                JSONObject responseJson1 = new JSONObject(e);
                                                String result1 = (String) responseJson1.get("result");
                                                switch(result1) {
                                                    case SUCCESS:
                                                        // 데이터 입력
                                                        if (getGameDataForUser(user.getAccessToken()) != null) {
                                                            Toast.makeText(getApplicationContext(),"데이터를 불러오는데 실패했습니다.\n다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(),"로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                        }
                                                        break;
                                                    case NOT_FOUND_PROFILE:
                                                        startActivity(new Intent(getApplicationContext(), UserInfoEntryActivity.class));
                                                        finish();
                                                        break;
                                                }
                                            } catch (JSONException e) { Log.e("has profile", "JSONException", e); }
                                        }));
                                        break;
                                    default:
                                        Log.e("asdf", result);
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "알 수 없는 이유로 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        break;
                                }
                            }
                            catch (JSONException e) { Log.e("JSONException", "btnLogin", e); }
                            catch (IOException e) { Log.e("IOException", "btnLogin", e); }
                        } else { Log.e("response failed : ", "btnLogin"); }
                    });
                }
        });

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

    /**
     * 유저의 게임데이터를 불러오는 함수
     *
     * @param accessToken : 불러올 유저의 엑세스 토큰
     * @return 펫 목록, 보유 중인 아이템, 보유중인 알 목록, 돈, 오늘 걸은 걸음 수 : Map<String,String>
     */
    public Map<String,String> getGameDataForUser(String accessToken) {
        //펫 목록, 보유 중인 아이템, 보유중인 알 목록, 돈, 오늘 걸은 걸음 수
        Map<String,String> results = new HashMap<>();

        ServerConnectionHelper sch = new ServerConnectionHelper(MONEY, accessToken);

        sch.setClientCallBackListener(((call, response) -> {
            try {
                JSONObject responseJson = new JSONObject(Objects.requireNonNull(response.body()).toString());
                String result = responseJson.getString("result");
                switch (result) {
                    case SUCCESS:
                        results.put("money", responseJson.getString("money"));
                        break;
                    case INVALID_ACCESS_TOKEN:
                        Log.e("money", "Invalid access token");
                        break;
                    case FAIL:
                        Log.e("money", "can't get money");
                        break;
                    default:
                        Log.e("money", "not found");
                        break;
                }
            } catch (JSONException e) { Log.e("ServerConnectionHelper-getDataForUser-moneyCallbackListener", "JSONException", e); }
        }));

        sch = new ServerConnectionHelper(GET_USER_ITEM, accessToken);

        sch.setClientCallBackListener(((call, response) -> {
            try {
                JSONObject responseJson = new JSONObject(Objects.requireNonNull(response.body()).toString());
                String result = responseJson.getString("result");
                switch (result) {
                    case SUCCESS:
                        results.put("items", responseJson.getString("items"));
                        break;
                    case FAIL:
                        Log.e("items", "can't get items");
                        break;
                    default:
                        Log.e("items", "not found");
                        break;
                }
            } catch (JSONException e) { Log.e("ServerConnectionHelper-getDataForUser-itemsCallbackListener", "JSONException", e); }
        }));

        sch = new ServerConnectionHelper(GET_PETS, accessToken);

        sch.setClientCallBackListener(((call, response) -> {
            try {
                JSONObject responseJson = new JSONObject(Objects.requireNonNull(response.body()).toString());
                String result = responseJson.getString("result");
                switch (result) {
                    case SUCCESS:
                        results.put("pets", responseJson.getString("items"));
                        break;
                    case FAIL:
                        Log.e("pets", "can't get pets");
                        break;
                    default:
                        Log.e("pets", "not found");
                        break;
                }
            } catch (JSONException e) { Log.e("ServerConnectionHelper-getDataForUser-petsCallbackListener", "JSONException", e); }
        }));

        sch = new ServerConnectionHelper(GET_EGGS, accessToken);

        sch.setClientCallBackListener(((call, response) -> {
            try {
                JSONObject responseJson = new JSONObject(Objects.requireNonNull(response.body()).toString());
                String result = responseJson.getString("result");
                switch (result) {
                    case SUCCESS:
                        results.put("eggs", responseJson.getString("eggs"));
                        break;
                    case FAIL:
                        Log.e("eggs", "can't get pets");
                        break;
                    default:
                        Log.e("eggs", "not found");
                        break;
                }
            } catch (JSONException e) { Log.e("ServerConnectionHelper-getDataForUser-eggsCallbackListener", "JSONException", e); }
        }));

        LocalDate currentDate = LocalDate.now();

        // 날짜 포맷 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 형식에 맞게 날짜를 문자열로 변환
        String formattedDate = currentDate.format(formatter);

        sch = new ServerConnectionHelper(STEPS.concat("?start_day=" + formattedDate + "&end_day=" + formattedDate), accessToken);

        sch.setClientCallBackListener(((call, response) -> {
            try {
                JSONObject responseJson = new JSONObject(Objects.requireNonNull(response.body()).toString());
                String result = responseJson.getString("result");
                switch (result) {
                    case SUCCESS:
                        results.put("steps", responseJson.getString("steps"));
                        break;
                    case FAIL:
                        Log.e("steps", "can't get pets");
                        break;
                    default:
                        Log.e("steps", "not found");
                        break;
                }
            } catch (JSONException e) { Log.e("ServerConnectionHelper-getDataForUser-eggsCallbackListener", "JSONException", e); }
        }));
        if(results.size() != 5) {
            return null;
        } else {
            return results;
        }
    }
}