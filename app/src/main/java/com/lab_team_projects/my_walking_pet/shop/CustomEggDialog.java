package com.lab_team_projects.my_walking_pet.shop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.CustomEggDialogBinding;

import java.io.IOException;

public class CustomEggDialog extends Dialog {

    private CustomEggDialogBinding binding;
    private final Context context;

    private ItemPurchaseListener itemPurchaseListener;    // 구매 했다고 단방향으로 알려주는 리스너 인터페이스

    public void setItemPurchaseListener(ItemPurchaseListener itemPurchaseListener) {
        this.itemPurchaseListener = itemPurchaseListener;
    }

    public interface ItemPurchaseListener {
        void itemPurchase();

    }

    public CustomEggDialog(@NonNull Context context) throws IOException {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CustomEggDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_blue);
        // 스마트폰의 화면 크기를 가져옴
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        this.getWindow().setLayout((int)(screenWidth * 0.9), (int)(screenHeight * 0.5));


        binding.btnCancel.setOnClickListener(v->this.dismiss());    // 취소 버튼
    }

}
