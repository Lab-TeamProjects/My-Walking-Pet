package com.lab_team_projects.my_walking_pet.login;

import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.PROFILE_SETTING;
import static com.lab_team_projects.my_walking_pet.app.ConnectionProtocol.SUCCESS;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.databinding.ActivityUserInfoEntryBinding;
import com.lab_team_projects.my_walking_pet.helpers.ServerConnectionHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserInfoEntryActivity extends AppCompatActivity {

    private ActivityUserInfoEntryBinding binding;

    private Bitmap profilePhoto;

    GameManager gm = GameManager.getInstance();
    String accessToken =  gm.getUser().getAccessToken();

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
                pickImageFromGallery();
            }
        });

        // 설정완료 버튼 클릭
        binding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("nickname", binding.etNickName.getText().toString());
                    jsonObject.put("status_message", binding.etStatusMsg.getText().toString());
                    jsonObject.put("birthday", binding.dpUserBirthday.getYear() + "-" + (binding.dpUserBirthday.getMonth() + 1) + "-" + binding.dpUserBirthday.getDayOfMonth());
                    jsonObject.put("weight", String.valueOf(binding.npUserWeight.getValue()));
                    jsonObject.put("height", String.valueOf(binding.npUserHeight.getValue()));
                    if(binding.rbMan.isChecked()) {
                        jsonObject.put("sex", "male");
                    } else {
                        jsonObject.put("sex", "female");
                    }

                    // 서버로 데이터 전송
                    ServerConnectionHelper serverRequest = new ServerConnectionHelper(PROFILE_SETTING, jsonObject, accessToken);
                    serverRequest.setClientCallBackListener((call, response) -> runOnUiThread(() -> {
                        if(response.isSuccessful()) {
                            try {
                                JSONObject responseJson = new JSONObject(response.body().string());
                                String result = responseJson.getString("result");
                                if(result.equals(SUCCESS)) { // 응답 메시지 입력해야함
                                    // 프로필 설정에 성공 했을 경우
                                    Toast.makeText(UserInfoEntryActivity.this, "프로필 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                } else {
                                    // 프로필 설정에 실패 했을 경우
                                    Toast.makeText(UserInfoEntryActivity.this, "프로필 설정에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (IOException e) { Log.e("IOException :", "btnComplete", e); }
                            catch (JSONException e) { Log.e("JSONException :", "btnComplete-serverRequest", e); }
                        } else { Log.e("btnComplete", "응답 실패"); }
                    }));
                } catch (JSONException e) { Log.e("JSONException :", "btnComplete", e); }
            }
        });
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
                            Bitmap resizeBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(uri.toString()), 480,480);

                            // 저장 경로 지정
                            File directory = getFilesDir();
                            File photoFile = new File(directory, "photo.jpg");

                            // 사진 저장
                            FileOutputStream fos = new FileOutputStream(photoFile);
                            resizeBitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                            fos.close();

                            ServerConnectionHelper sc = new ServerConnectionHelper(directory, "photo.jpg", gm.getUser().getAccessToken());
                            sc.setClientCallBackListener((call,response) -> runOnUiThread(()-> {
                                if(response.isSuccessful()) {
                                    // 사진 서버로 전송 구현 필요
                                }
                            }));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

}