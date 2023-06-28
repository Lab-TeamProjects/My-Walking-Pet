package com.lab_team_projects.my_walking_pet.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Permissions check helper.
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
public class PermissionsCheckHelper {

    private final Context context;
    private final Activity activity;

    private final String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
    };




    private List<Object> permissionList;

    private final int MULTIPLE_PERMISSIONS = 1023 ;

    /**
     * Instantiates a new Permissions check helper.
     *
     * @param _activity the activity
     * @param _context  the context
     */
    public PermissionsCheckHelper(Activity _activity, Context _context) {
        this.activity = _activity;
        this.context = _context;
    }

    /**
     * 허용되지 않은 권한이 있는지 체크하는 함수
     *
     * @return the boolean
     */
    public boolean checkPermission(){
        int result;
        permissionList = new ArrayList<>();

        // 배열로 저장한 권한 중 허용되지 않은 권한이 있는지 체크
        for (String pm : permissions){
            result = ContextCompat.checkSelfPermission(context, pm);
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(pm);
            }
        }

        return permissionList.isEmpty();
    }

    /**
     * 배열에 있는 권한을 요청하는 함수
     */
    public void requestPermission(){
        ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]),
                MULTIPLE_PERMISSIONS);
    }

    /**
     * Permission result boolean.
     *
     * @param requestCode  the request code
     * @param permissions  the permissions
     * @param grantResults the grant results
     * @return the boolean
     */
    public boolean permissionResult(int requestCode , @NonNull String[] permissions, @NonNull int[] grantResults){

        if (requestCode == MULTIPLE_PERMISSIONS && (grantResults.length > 0)){
            for(int i =0; i < grantResults.length ; i ++){

                // grantResults == 0 사용자가 허용한 것
                // grantResults == -1 사용자가 거부한 것
                if(grantResults[i] == -1){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Battery optimization.
     */
    @SuppressLint("BatteryLife")
    public void batteryOptimization() {
        // 베터리 최적화 설정 요청
        Intent intent = new Intent();
        String packageName = context.getPackageName();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
        }
    }
}
