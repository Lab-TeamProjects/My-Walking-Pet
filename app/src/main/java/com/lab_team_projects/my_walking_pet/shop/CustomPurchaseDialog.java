package com.lab_team_projects.my_walking_pet.shop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.databinding.CustomPurchaseDialogBinding;
import com.lab_team_projects.my_walking_pet.helpers.InventoryHelper;
import com.lab_team_projects.my_walking_pet.home.Item;
import com.lab_team_projects.my_walking_pet.home.ItemDetail;
import com.lab_team_projects.my_walking_pet.login.User;

import java.io.IOException;

public class CustomPurchaseDialog extends Dialog {

    private final InventoryHelper inventoryHelper;
    private final int itemId;
    private int count;
    private int price;
    private CustomPurchaseDialogBinding binding;
    private final Context context;
    private final ItemDetail itemDetail;

    private ItemPurchaseListener itemPurchaseListener;    // 구매 했다고 단방향으로 알려주는 리스너 인터페이스

    public void setItemPurchaseListener(ItemPurchaseListener itemPurchaseListener) {
        this.itemPurchaseListener = itemPurchaseListener;
    }

    public interface ItemPurchaseListener {
        void itemPurchase();

    }

    public CustomPurchaseDialog(@NonNull Context context, int id) throws IOException {
        super(context);
        this.context = context;
        this.itemId = id;

        this.inventoryHelper = new InventoryHelper(context);
        this.itemDetail = inventoryHelper.findItem(itemId);

        this.count = 1;
        this.price = this.itemDetail.getPrice();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CustomPurchaseDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_blue);
        // 스마트폰의 화면 크기를 가져옴
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        this.getWindow().setLayout((int)(screenWidth * 0.9), (int)(screenHeight * 0.45));

        binding.tvItemName.setText(itemDetail.getName());    // 아이템 이름

        price = itemDetail.getPrice();
        binding.tvItemPrice.setText(String.valueOf(price));    // 아이템 가격

        // 아이템 효과
        // 아이템 효과 값
        binding.tvItemInfo.setText(itemDetail.getExplanation());    // 아이템 정보


        // 개수 추가 감소
        binding.ibAdd.setOnClickListener(v -> {
            if (count < 100) {
                count++;
                updateItemDetails();
            }
        });

        binding.ibSubtract.setOnClickListener(v -> {
            if (count > 1) {
                count--;
                updateItemDetails();
            }
        });

        binding.btnOK.setOnClickListener(v->{
            // 구매
            GameManager gm = GameManager.getInstance();
            User user = gm.getUser();

            if (user.getMoney() >= price) {
                user.setMoney(user.getMoney() - price);
                Toast.makeText(context, "구매 완료", Toast.LENGTH_SHORT).show();
                itemPurchaseListener.itemPurchase();    // 구매 했다고 알려주는 리스너 함수

                // 유저 인벤토리에 구매한 아이템 추가
                Item userItem = null;
                boolean flag = false;
                for (Item item : user.getItemLists()) {
                    if (item.getCode().equals(itemDetail.getCode())) {
                        userItem = item;
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    // 유저 인벤토리에서 해당 아이템이 1개라도 있을 경우
                    userItem.setCount(userItem.getCount() + count);
                } else {
                    // 유저 인벤토리에서 해당 아이템이 없을 경우
                    userItem = new Item(itemDetail.getCode(), count);
                    user.getItemLists().add(userItem);
                }

                this.dismiss();
            } else {
                Toast.makeText(context, "돈이 부족합니다", Toast.LENGTH_SHORT).show();
            }


        });

        binding.btnCancel.setOnClickListener(v->this.dismiss());    // 취소 버튼
    }

    private void updateItemDetails() {
        price = itemDetail.getPrice() * count;
        binding.tvItemNum.setText(String.valueOf(count));
        binding.tvItemPrice.setText(String.valueOf(price));
    }
}
