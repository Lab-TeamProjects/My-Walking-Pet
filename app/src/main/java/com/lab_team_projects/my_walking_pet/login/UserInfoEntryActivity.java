package com.lab_team_projects.my_walking_pet.login;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.lab_team_projects.my_walking_pet.helpers.PermissionsCheckHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserInfoEntryActivity extends AppCompatActivity {


    private PermissionsCheckHelper pch;

    private ImageView ivUserProfile;
    private NumberPicker npUserWeight, npUserHeight;
    private EditText etNickName, etStatusMsg;
    private DatePicker dpUserBirthday;
    private RadioGroup rgUserGender;
    private RadioButton rbMan, rbWomen;
    private Button btnComplete;
    private Bitmap profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_entry);

        ivUserProfile = findViewById(R.id.ivUserProfile);
        npUserWeight = findViewById(R.id.npUserWeight);
        npUserHeight = findViewById(R.id.npUserHeight);
        etNickName = findViewById(R.id.etNickName);
        etStatusMsg = findViewById(R.id.etStatusMsg);
        dpUserBirthday = findViewById(R.id.dpUserBirthday);
        rgUserGender = findViewById(R.id.rgGender);
        rbMan = findViewById(R.id.rbMan);
        rbWomen = findViewById(R.id.rbWomen);
        btnComplete = findViewById(R.id.btnComplete);

        npUserWeight.setMaxValue(300);
        npUserWeight.setMinValue(10);
        npUserWeight.setValue(60);

        npUserHeight.setMaxValue(300);
        npUserHeight.setMinValue(10);
        npUserHeight.setValue(170);

        rgUserGender.check(R.id.rbMan);

        try {
            // 저장된 프로필 사진 불러오기
            File directory = getFilesDir();
            File photo = new File(directory,"photo.jpg");

            profilePhoto = BitmapFactory.decodeFile(photo.getAbsolutePath());

            ivUserProfile.setImageBitmap(profilePhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 프로필 이미지 클릭
        ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    permissionCheck();
                    pickImageFromGallery();
                } else {
                    pickImageFromGallery();
                }
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("profilePhoto", profilePhoto.toString());
                Log.d("nickName",  etNickName.getText().toString());
                Log.d("statusMsg", etStatusMsg.getText().toString());
                Log.d("userBirthday", dpUserBirthday.getYear() + "-" + (dpUserBirthday.getMonth() + 1) + "-" + dpUserBirthday.getDayOfMonth());
                Log.d("userWeight", String.valueOf(npUserWeight.getValue()));
                Log.d("userHeight", String.valueOf(npUserHeight.getValue()));
                if(rbMan.isChecked()) {
                    Log.d("userGender", String.valueOf(rbMan.getText()));
                } else {
                    Log.d("userGender", String.valueOf(rbWomen.getText()));
                }
            }
        });
    }

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

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        activityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // 사진 불러오기
                        Uri uri = result.getData().getData();
                        Glide.with(UserInfoEntryActivity.this).load(uri).into(ivUserProfile);

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

    private void sendServer() { // 서버로 전송하기위한 함수
        class sendData extends AsyncTask<Void, Void, String> { // 쓰레드 만들기

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    OkHttpClient client = new OkHttpClient();
                    // okHttpClient 호출
                    JSONObject jsonInput = new JSONObject();
                    // Json객체 생성
                    jsonInput.put("profilePhoto", profilePhoto);
                    jsonInput.put("nickName",  etNickName.getText().toString());
                    jsonInput.put("statusMsg", etStatusMsg.getText().toString());
                    jsonInput.put("userBirthday", dpUserBirthday.getYear() + "-" + (dpUserBirthday.getMonth() + 1) + "-" + dpUserBirthday.getDayOfMonth());
                    jsonInput.put("userWeight", npUserWeight.toString());
                    jsonInput.put("userHeight", npUserHeight.toString());
                    if(rbMan.isChecked()) {
                        jsonInput.put("userGender", String.valueOf(rbMan.getText()));
                    } else {
                        jsonInput.put("userGender", String.valueOf(rbWomen.getText()));
                    }

                    // json객체에 데이터 추가
                    RequestBody reqBody = RequestBody.create(
                            jsonInput.toString(),
                            MediaType.parse("application/json; charset=utf-8")
                    );

                    /*Request request = new Request.Builder()
                            .post(reqBody)
                            .url(urls)
                            .build();

                    Response responses = null;
                    responses = client.newCall(request).execute();
                    System.out.println(responses.body().string());*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //catch (IOException e) {
//                        e.printStackTrace();
//                    }
                return null;
            }
        }
        sendData sendData = new sendData();
        sendData.execute();
        // 웹서버에 데이터 전송
    }

}