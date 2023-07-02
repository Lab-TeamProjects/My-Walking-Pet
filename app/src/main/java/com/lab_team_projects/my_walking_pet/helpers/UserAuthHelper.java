package com.lab_team_projects.my_walking_pet.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

public class UserAuthHelper {

    private Activity activity = null;
    private String password;
    private String passwordCheck;
    private EditText etPassword;
    private EditText etPasswordCheck;

    public UserAuthHelper(Activity activity, EditText etPassword, EditText etPasswordCheck) {
        this.activity = activity;
        this.etPassword = etPassword;
        this.etPasswordCheck = etPasswordCheck;
        this.password = etPassword.getText().toString();
        this.passwordCheck = etPasswordCheck.getText().toString();
    }

    public boolean isPasswordValid() {
        // 비밀번호의 길이가 8자 이상인지 확인
        if (this.password.length() < 8) {
            return false;
        }

        // 비밀번호에 영문 대문자, 영문 소문자, 숫자, 특수 문자가 포함되어 있는지 확인
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (Pattern.matches("[!@#$%^&*()-+=~]", String.valueOf(c))) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    public boolean isPasswordConfirmed() {
        // 비밀번호 확인 검사를 수행하고, 일치하면 true를 반환
        return password.equals(passwordCheck);
    }

    public void updatePasswordValidationStatus() {

        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isPasswordValid()) {
                        etPassword.setError(null);
                    } else {
                        etPassword.setError("비밀번호가 유효하지 않습니다.");
                    }
                }
            });
        }
    }

    public void updatePasswordCheckValidationStatus() {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 비밀번호 확인 검사 결과에 따라 UI 업데이트
                    if (isPasswordConfirmed()) {
                        etPasswordCheck.setError(null);
                    } else {
                        etPasswordCheck.setError("비밀번호가 일치하지 않습니다.");
                    }
                }
            });
        }
    }
}
