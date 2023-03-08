package com.lab_team_projects.my_walking_pet.login;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.ServerConnection;
import com.lab_team_projects.my_walking_pet.databinding.ActivityTitleBinding;
import com.lab_team_projects.my_walking_pet.databinding.ActivityUserInfoEntryBinding;
import com.lab_team_projects.my_walking_pet.helpers.PermissionsCheckHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;


public class UserInfoEntryActivity extends AppCompatActivity {
    private final String PROFILE_SETTING = "URL 넣어야함"; // URL 넣어야 함

    private ActivityUserInfoEntryBinding binding;

    private PermissionsCheckHelper pch;

    private Bitmap profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 몸무게 Number Picker 설정
        binding.npUserWeight.setMaxValue(300);
        binding.npUserWeight.setMinValue(10);
        binding.npUserWeight.setValue(60);
        // 키 Number Picker 설정
        binding.npUserHeight.setMaxValue(300);
        binding.npUserHeight.setMinValue(10);
        binding.npUserHeight.setValue(170);
        // 성별 Radio Group 설정
        binding.rgGender.check(R.id.rbMan);

        try {
            // 저장된 프로필 사진 불러오기
            File directory = getFilesDir();
            File photo = new File(directory,"photo.jpg");

            profilePhoto = BitmapFactory.decodeFile(photo.getAbsolutePath());

            binding.ivUserProfile.setImageBitmap(profilePhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 프로필 이미지 클릭
        binding.ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    permissionCheck(); // 권한 체크
                    pickImageFromGallery();
                } else {
                    pickImageFromGallery();
                }
            }
        });

        // 설정완료 버튼 클릭
        binding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("profilePhoto", profilePhoto.toString());
                    jsonObject.put("nickName", binding.etNickName.getText().toString());
                    jsonObject.put("statusMsg", binding.etStatusMsg.getText().toString());
                    jsonObject.put("userBirthday", binding.dpUserBirthday.getYear() + "-" + (binding.dpUserBirthday.getMonth() + 1) + "-" + binding.dpUserBirthday.getDayOfMonth());
                    jsonObject.put("userWeight", String.valueOf(binding.npUserWeight.getValue()));
                    jsonObject.put("userHeight", String.valueOf(binding.npUserHeight.getValue()));
                    if(binding.rbMan.isChecked()) {
                        jsonObject.put("userGender", String.valueOf(binding.rbMan.getText()));
                    } else {
                        jsonObject.put("userGender", String.valueOf(binding.rbWomen.getText()));
                    }

                    // 서버로 데이터 전송
                    ServerConnection serverRequest = new ServerConnection(PROFILE_SETTING, jsonObject);
                    serverRequest.setClientCallBackListener((call, response) -> runOnUiThread(() -> {
                        if(response.isSuccessful()) {
                            try {
                                if(Objects.requireNonNull(response.body()).string().equals("올바른 응답 메시지")) { // 응답 메시지 입력해야함
                                    // 회원가입에 성공 했을 경우

                                } else {
                                    // 회원가입에 실패 했을 경우

                                }
                            } catch (IOException e) {
                                Log.e("Email Duplication : ", "이메일 중복", e);
                            }
                        } else {
                            Log.e("Email Duplication - else : ", "응답 실패");
                        }
                    }));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 권한 체크 함수
    private void permissionCheck(){
        if(Build.VERSION.SDK_INT >= 23){
            pch =  new PermissionsCheckHelper(this, this);

            if(!pch.checkPermission()){
                pch.requestPermission();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (!pch.permissionResult(requestCode, permissions, grantResults)){
            pch.requestPermission();
        }
    }

    // 갤러리 접근 함수
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        activityResultLauncher.launch(intent);
    }

    // 갤러리 접근 함수 실행 시
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // 사진 불러오기
                        Uri uri = result.getData().getData();
                        Glide.with(UserInfoEntryActivity.this).load(uri).into(binding.ivUserProfile);

                        try {
                            // 사진 지정
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                            // 저장 경로 지정
                            File directory = getFilesDir();
                            File photoFile = new File(directory, "photo.jpg");
                            // 사진 저장
                            FileOutputStream fos = new FileOutputStream(photoFile);
                            Log.d("fos" , fos.toString());
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                            fos.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

}