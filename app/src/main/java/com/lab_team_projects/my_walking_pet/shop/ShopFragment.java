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
import com.lab_team_projects.my_walking_pet.databinding.FragmentShopBinding;
import com.lab_team_projects.my_walking_pet.login.User;

import java.io.IOException;

/**
 * 상점 프래그먼트
 * 사용자는 상점에서 동물 육아에 필요한 아이템을 구매하고
 * 동물을 키우기 위한 알을 구매할 수 있습니다.
 */
public class ShopFragment extends Fragment implements View.OnClickListener {

    private FragmentShopBinding binding;
    private MainActivity mMainActivity;

    /**
     * Instantiates a new Shop fragment.
     */
    public ShopFragment() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);

        binding.tvMoney.setOnClickListener(v->{
            User user = GameManager.getInstance().getUser();
            user.setMoney(user.getMoney() + 100000);
        });

        mMainActivity.onAppBarLoad();
        setBtnOnClickListener();

        return binding.getRoot();
    }

    /**
     * 상점에서 버튼을 클릭하면 아이디를 반환받아 상점 판매 아이템을 클릭했는지 알 구매 버튼을 클릭했는지
     * 판단하여 서로 다른 다이얼로그를 화면에 표시합니다.
     */
    @Override
    public void onClick(View v) {
        /*
         * 실질적으로 버튼이 클릭되었을 때
         * 동작하는 메서드
         */
        int btnId = v.getId();
        int itemId = -1;

        if (btnId == R.id.btnFood1) {
            itemId = 1001;
        } else if (btnId == R.id.btnFood2) {
            itemId = 1002;
        } else if (btnId == R.id.btnFood3) {
            itemId = 1003;
        } else if (btnId == R.id.btnFood4) {
            itemId = 1004;
        } else if (btnId == R.id.btnOther1) {
            itemId = 1030;
        } else if (btnId == R.id.btnOther2) {
            itemId = 1031;
        } else if (btnId == R.id.btnOther3) {
            itemId = 1032;
        } else if (btnId == R.id.btnOther4) {
            itemId = 1033;
        } else if (btnId == R.id.btnOther5) {
            itemId = 1034;
        } else if (btnId == R.id.btnOther6) {
            itemId = 1035;
        } else if (btnId == R.id.btnOther7) {
            itemId = 1036;
        } else if (btnId == R.id.btnClean1) {
            itemId = 1061;
        } else if (btnId == R.id.btnClean2) {
            itemId = 1062;
        } else if (btnId == R.id.ibPetEgg) {
            try {
                CustomEggDialog dialog = new CustomEggDialog(requireContext());
                dialog.show();
                return;
            } catch (IOException e) {
                Log.e("ShopFragment_onClick_ibPetEgg_dialog", "IOException", e);
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
                    binding.tvMoney.setText(String.valueOf(GameManager.getInstance().getUser().getMoney())); // 아이템 구매 OK 버튼을 눌렀을 때 변경된 재화를 표시하기 위해
                });
            }
        } catch (IOException e) {
            Log.e("ShopFragment_onClick_item_dialog", "IOException", e);
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

    /**
     * 상점의 버튼은 리스너로 구성되어있습니다.
     */
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