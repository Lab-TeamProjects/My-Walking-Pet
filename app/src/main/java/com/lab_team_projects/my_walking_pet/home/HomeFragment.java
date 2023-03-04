package com.lab_team_projects.my_walking_pet.home;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.help.HelpActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private boolean isInteractionBtnClick = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        /* 버튼 이동 */

        binding.ibAR.setOnClickListener(v->{
            Toast.makeText(requireContext(), "AR 이동 버튼", Toast.LENGTH_SHORT).show();
        });

        binding.ibShop.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.shopFragment, null);

        });

        binding.ibSetting.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.settingFragment, null);
        });

        binding.ibMission.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.missionFragment, null);
        });

        binding.ibCollection.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.collectionFragment, null);
        });

        binding.ibHelp.setOnClickListener(v->{
            startActivity(new Intent(requireContext(), HelpActivity.class));
            Toast.makeText(requireContext(), "도움말 버튼", Toast.LENGTH_SHORT).show();
        });

        binding.tvWalkCount.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.walkCountFragment, null);
        });


        binding.fabInteraction.setOnClickListener(v->{
            isInteractionBtnClick = !isInteractionBtnClick;
            clickInteractionBtn(isInteractionBtnClick);
        });


        return binding.getRoot();
    }

    private void clickInteractionBtn(boolean isClick){
        if(isClick) {
            binding.fabWater.setVisibility(View.VISIBLE);
            binding.fabFood.setVisibility(View.VISIBLE);
            binding.fabWash.setVisibility(View.VISIBLE);
            binding.fabclothes.setVisibility(View.VISIBLE);
        } else {
            binding.fabWater.setVisibility(View.INVISIBLE);
            binding.fabFood.setVisibility(View.INVISIBLE);
            binding.fabWash.setVisibility(View.INVISIBLE);
            binding.fabclothes.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 바인딩은 생명주기 이슈 때문에 프래그먼트가 종료되면 널을 넣어줘야 함
        binding = null;
    }


}