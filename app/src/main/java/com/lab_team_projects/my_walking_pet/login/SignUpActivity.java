package com.lab_team_projects.my_walking_pet.login;

import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.CHECK_EMAIL_DUPLICATION;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.EMAIL_IS_DUPLICATION;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.SIGN_UP;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.SUCCESS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lab_team_projects.my_walking_pet.helpers.ServerConnectionHelper;
import com.lab_team_projects.my_walking_pet.databinding.ActivitySignUpBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnDuplicationCheck.setOnClickListener(btnDuplicationCheckListener);
        binding.btnSignUp.setOnClickListener(btnSignUpListener);

        binding.etEmail.addTextChangedListener(textWatcher);
        binding.etPassWord.addTextChangedListener(textWatcher);
        binding.etPassWordCheck.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // EditText 입력값이 변경될 때마다 모든 필드의 입력 여부를 검사하여 회원가입 버튼을 활성화 또는 비활성화함
            if (!binding.etEmail.getText().toString().isEmpty()
                    && !binding.etPassWord.getText().toString().isEmpty()
                    && !binding.etPassWordCheck.getText().toString().isEmpty()
            ) {
                binding.btnSignUp.setEnabled(true); // 모든 필드가 채워졌다면 회원가입 버튼 활성화
            } else {
                binding.btnSignUp.setEnabled(false); // 하나 이상의 필드가 비어있다면 회원가입 버튼 비활성화
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    // 중복체크 리스너
    View.OnClickListener btnDuplicationCheckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", binding.etEmail.getText().toString());
            } catch (JSONException e) {
                Log.e("JSONException : ", "btnDuplicationCheck", e);
            }
            if(!isEmailPattern(binding.etEmail.getText().toString())) {
                binding.tvDuplicationResult.setText("올바른 이메일 형식이 아닙니다.");
                binding.tvDuplicationResult.setTextColor(Color.RED);
            }
            else {
                ServerConnectionHelper sc = new ServerConnectionHelper(CHECK_EMAIL_DUPLICATION, jsonObject);
                sc.setClientCallBackListener((call, response) -> runOnUiThread(() -> {
                    if(response.isSuccessful()) {
                        try {
                            JSONObject responseJson = new JSONObject(Objects.requireNonNull(response.body()).string());
                            String result = (String) responseJson.get("result");

                            if(result.equals(SUCCESS)) {
                                // 사용 가능한 이메일일 경우
                                binding.tvDuplicationResult.setText("사용가능한 이메일입니다.");
                                binding.tvDuplicationResult.setTextColor(Color.GREEN);
                                binding.etEmail.setEnabled(false);
                            } else if (result.equals(EMAIL_IS_DUPLICATION)) {
                                // 중복되거나
                                binding.tvDuplicationResult.setText("중복된 이메일입니다.");
                                binding.tvDuplicationResult.setTextColor(Color.RED);
                            } else {
                                binding.tvDuplicationResult.setText("사용할 수 없는 이메일입니다.");
                                binding.tvDuplicationResult.setTextColor(Color.RED);
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException : ", "btnDuplicationCheck", e);
                        } catch (IOException e) {
                            Log.e("IOException : ", "btnDuplicationCheck", e);
                        }
                    } else {
                        binding.tvDuplicationResult.setText("서버 연결이 불안정합니다.");
                        binding.tvDuplicationResult.setTextColor(Color.RED);
                        Log.e("response failed : ", "btnDuplicationCheck");
                    }
                }));
            }
        }
    };

    // 회원가입 버튼
    View.OnClickListener btnSignUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (binding.tvDuplicationResult.getText().equals("사용가능한 이메일입니다.")) {
                if (binding.etPassWord.getText().toString().equals(binding.etPassWordCheck.getText().toString())) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", binding.etEmail.getText().toString());
                        jsonObject.put("password", binding.etPassWord.getText().toString());
                    } catch (JSONException e) {
                        Log.e("JSONException : ", "btnSignUp", e);
                    }

                    ServerConnectionHelper sc = new ServerConnectionHelper(SIGN_UP, jsonObject);
                    sc.setClientCallBackListener((call, response) -> runOnUiThread(() -> {
                        if(response.isSuccessful()) {
                            try {
                                JSONObject responseJson = new JSONObject(Objects.requireNonNull(response.body()).string());
                                String result = (String) responseJson.get("result");

                                if (result.equals(SUCCESS)) {
                                    startActivity(new Intent(getApplicationContext(), EmailAuthNoticeActivity.class));
                                    finish();
                                } else {
                                    // 회원가입에 실패 했을 경우
                                    Toast.makeText(SignUpActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                Log.e("IOException : ", "btnSignUp", e);
                            } catch (JSONException e) {
                                Log.e("JSONException : ", "btnSignUp", e);
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this,"서버연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
                            Log.e("response failed : ", "btnSignUp");
                        }
                    }));
                } else {
                    Toast.makeText(SignUpActivity.this,"비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(SignUpActivity.this,"이메일을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 이메일 형식 판별
    public static boolean isEmailPattern(String src) {
        return Pattern.matches("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+", src);
    }
}