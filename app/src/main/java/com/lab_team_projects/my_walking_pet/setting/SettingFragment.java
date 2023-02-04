package com.lab_team_projects.my_walking_pet.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.FragmentSettingBinding;

import java.util.List;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        final String[] SETTING_LIST = getResources().getStringArray(R.array.SETTING_LIST);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, SETTING_LIST);
        binding.lvSettingList.setAdapter(adapter);

        binding.lvSettingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), SETTING_LIST[position], Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        //To-Do : 사용자 정보 변경
                        break;
                    case 1:
                        Intent intent = new Intent(requireContext(), NoticeSettingActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        //To-Do : 문의하기
                        break;
                    case 3:
                        //To-Do : 버그제보
                        break;
                    case 4:
                        //To-Do : 로그아웃
                        break;

                }
            }
        });



        return binding.getRoot();
    }
}