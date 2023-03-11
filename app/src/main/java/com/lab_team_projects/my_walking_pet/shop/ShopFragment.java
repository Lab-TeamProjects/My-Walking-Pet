package com.lab_team_projects.my_walking_pet.shop;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.databinding.FragmentShopBinding;

public class ShopFragment extends Fragment implements View.OnClickListener {

    private FragmentShopBinding binding;
    private MainActivity mMainActivity;

    public ShopFragment() {
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
        binding = FragmentShopBinding.inflate(inflater, container, false);

        mMainActivity.onAppBarLoad();
        binding.btnFoodA.setOnClickListener(this);

        return binding.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFoodA) {
            Log.d("__my__", "asd");
        }
    }
}