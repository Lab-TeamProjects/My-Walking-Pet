package com.lab_team_projects.my_walking_pet.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.walk_count.WalkCountForeGroundService;

public class NoticeSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SharedPreferences prefs;

        private static final String SETTING_SENSOR_BG = "foreground_use";


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            prefs.registerOnSharedPreferenceChangeListener(prefListener);
        }

        SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        if(key.equals(SETTING_SENSOR_BG)) {
                            if (sharedPreferences.getBoolean(key, true)) {

                                startService();
                            }
                            else {
                                stopService();
                            }
                        }
                    }
                };

        public void startService() {
            // 서비스 시작 함수
            Intent serviceIntent;
            serviceIntent = new Intent(getContext(), WalkCountForeGroundService.class);
            requireContext().startService(serviceIntent);
        }

        public void stopService() {
            // 서비스 종료 함수
            Intent serviceIntent;
            serviceIntent = new Intent(getContext(), WalkCountForeGroundService.class);
            requireContext().stopService(serviceIntent);
        }
    }


}