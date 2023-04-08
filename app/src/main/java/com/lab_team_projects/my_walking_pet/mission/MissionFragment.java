package com.lab_team_projects.my_walking_pet.mission;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.databinding.FragmentMissionBinding;
import com.lab_team_projects.my_walking_pet.helpers.MissionCheckHelper;
import com.lab_team_projects.my_walking_pet.home.Item;

public class MissionFragment extends Fragment {

    private FragmentMissionBinding binding;
    private MainActivity mMainActivity;
    public MissionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMissionBinding.inflate(inflater, container, false);
        mMainActivity.onAppBarLoad();
        MissionCheckHelper missionCheckHelper = new MissionCheckHelper(requireContext());

        binding.pbMissionBar1.setOnClickListener(v->{
            // 걷기 미션 클릭
            Toast.makeText(requireContext(), "퀘스트 완료", Toast.LENGTH_SHORT).show();
        });
        
        binding.pbMissionBar2.setOnClickListener(v->{
            // 물주기 미션 클릭

            if (missionCheckHelper.getCount(Item.ItemType.DRINK.name()) >= 3) {
                missionCheckHelper.completeMission(Item.ItemType.DRINK.name(), 3);
                Toast.makeText(requireContext(), "퀘스트 완료", Toast.LENGTH_SHORT).show();
                binding.pbMissionBar2.setProgress(missionCheckHelper.getRatio(Item.ItemType.DRINK.name()));
            }
        });
        
        binding.pbMissionBar3.setOnClickListener(v->{

            // 밥주기 미션 클릭
            if (missionCheckHelper.getCount(Item.ItemType.FOOD.name()) >= 3) {
                missionCheckHelper.completeMission(Item.ItemType.FOOD.name(), 3);
                Toast.makeText(requireContext(), "퀘스트 완료", Toast.LENGTH_SHORT).show();
                binding.pbMissionBar3.setProgress(missionCheckHelper.getRatio(Item.ItemType.FOOD.name()));
            }
        });
        //setProgressRatio();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setProgressRatio();
    }

    private void setProgressRatio() {
        MissionCheckHelper missionCheckHelper = new MissionCheckHelper(requireContext());
        binding.pbMissionBar2.setProgress(missionCheckHelper.getRatio(Item.ItemType.DRINK.name()));
        binding.pbMissionBar3.setProgress(missionCheckHelper.getRatio(Item.ItemType.FOOD.name()));
        Toast.makeText(requireContext(), missionCheckHelper.getCount(Item.ItemType.FOOD.name()) + " " + missionCheckHelper.getCount(Item.ItemType.DRINK.name()), Toast.LENGTH_SHORT).show();
    }

}