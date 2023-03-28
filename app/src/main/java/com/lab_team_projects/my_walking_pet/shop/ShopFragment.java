package com.lab_team_projects.my_walking_pet.shop;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.databinding.CustomEggDialogBinding;
import com.lab_team_projects.my_walking_pet.databinding.FragmentShopBinding;

import java.io.IOException;

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
        setBtnOnClickListener();    // 버튼 클릭 리스너 등록


        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {

        /*
         * 실질적으로 버큰이 클릭되었을 때
         * 동작하는 메서드
         * */

        int btnId = v.getId();
        int itemId = -1;

        if (btnId == R.id.btnFood1) {
            itemId = 1001;
        } else if (btnId == R.id.btnFood2) {
            itemId = 1002;
        } else if (btnId == R.id.ibPetEgg) {
            try {
                CustomEggDialog dialog = new CustomEggDialog(requireContext());
                dialog.show();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(requireContext(), "상품 준비중 입니다!", Toast.LENGTH_SHORT).show();
        }
        // 선택된 아이템으로 다이얼로그 표시
        try {
            if (itemId != -1) {
                CustomPurchaseDialog dialog = new CustomPurchaseDialog(requireContext(), itemId);
                dialog.show();
                dialog.setItemPurchaseListener(() -> {
                    binding.tvMoney.setText(String.valueOf(GameManager.getInstance().getUser().getMoney()));    // 아이템 구매 OK 버튼을 눌렀을 때 변경된 재화를 표시하기 위해
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.tvMoney.setText(String.valueOf(GameManager.getInstance().getUser().getMoney()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setBtnOnClickListener() {
        binding.ibPetEgg.setOnClickListener(this);

        binding.btnFood1.setOnClickListener(this);
        binding.btnFood2.setOnClickListener(this);
        binding.btnFood3.setOnClickListener(this);
        binding.btnFood4.setOnClickListener(this);
        binding.btnFood5.setOnClickListener(this);
        binding.btnFood6.setOnClickListener(this);
        binding.btnFood7.setOnClickListener(this);
        binding.btnFood8.setOnClickListener(this);

        binding.btnOther1.setOnClickListener(this);
        binding.btnOther2.setOnClickListener(this);
        binding.btnOther3.setOnClickListener(this);
        binding.btnOther4.setOnClickListener(this);
        binding.btnOther5.setOnClickListener(this);
        binding.btnOther6.setOnClickListener(this);
        binding.btnOther7.setOnClickListener(this);
        binding.btnOther8.setOnClickListener(this);

        binding.btnClean1.setOnClickListener(this);
        binding.btnClean2.setOnClickListener(this);
        binding.btnClean3.setOnClickListener(this);
        binding.btnClean4.setOnClickListener(this);
        binding.btnClean5.setOnClickListener(this);
        binding.btnClean6.setOnClickListener(this);
        binding.btnClean7.setOnClickListener(this);
        binding.btnClean8.setOnClickListener(this);
    }
}