package com.lab_team_projects.my_walking_pet.login;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.helpers.PermissionsCheckHelper;


public class UserInfoEntryActivity extends AppCompatActivity {

    private Uri uri;

    private PermissionsCheckHelper pch;
    private ImageView ivUserProfile;
    private NumberPicker npUserWeight;
    private DatePicker dpUserBirthday;
    private RadioGroup rgUserGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_entry);

        ivUserProfile = findViewById(R.id.ivUserProfile);
        npUserWeight = findViewById(R.id.npUserWeight);
        dpUserBirthday = findViewById(R.id.dpUserBirthday);
        rgUserGender = findViewById(R.id.rgGender);

        npUserWeight.setMaxValue(300);
        npUserWeight.setMinValue(10);
        npUserWeight.setValue(60);

        rgUserGender.check(R.id.rbMan);

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
        intent.setType("image/*");
        activityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        //ToDo
                        uri = result.getData().getData();
                        Glide.with(UserInfoEntryActivity.this).load(uri).into(ivUserProfile);
                    }
                }
            }
    );
}