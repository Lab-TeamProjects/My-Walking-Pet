package com.lab_team_projects.my_walking_pet.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.SettingsActivityBinding;
import com.lab_team_projects.my_walking_pet.walk_count.WalkCountForeGroundService;

public class NoticeSettingActivity extends AppCompatActivity {

    private static final WalkCountForeGroundService service = new WalkCountForeGroundService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsActivityBinding binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnArrowBack.setOnClickListener(v -> onBackPressed());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private static final String SETTING_SENSOR_BG = "foreground_use";
        private String rootKey = "";

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            this.rootKey = rootKey;

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());

            if(!service.isRunning()) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(SETTING_SENSOR_BG, false);
                editor.apply();
            }

            prefs.registerOnSharedPreferenceChangeListener(prefListener);
        }

        @Override
        public void onResume() {
            super.onResume();
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals(SETTING_SENSOR_BG)) {
                    if (getContext() != null) {
                        if (sharedPreferences.getBoolean(key, true)) {
                            startService();
                        }
                        else {
                            stopService();
                        }
                    }
                }
            }
        };

        public void startService() {
            // 서비스 시작 함수
            Intent serviceIntent;
            serviceIntent = new Intent(requireContext(), service.getClass());
            requireActivity().startForegroundService(serviceIntent);
        }

        public void stopService() {
            // 서비스 종료 함수
            Intent serviceIntent;
            service.stopTask();
            serviceIntent = new Intent(requireContext(), service.getClass());
            requireActivity().stopService(serviceIntent); // 나중에 수정할 것
        }

    }


}