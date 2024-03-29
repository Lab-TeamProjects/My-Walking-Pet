package com.lab_team_projects.my_walking_pet.shop;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.databinding.CustomPurchaseDialogBinding;
import com.lab_team_projects.my_walking_pet.helpers.InventoryHelper;
import com.lab_team_projects.my_walking_pet.home.Item;
import com.lab_team_projects.my_walking_pet.home.ItemDetail;
import com.lab_team_projects.my_walking_pet.login.User;

import java.io.IOException;
import java.util.Locale;

/**
 * 상점에서 아이템을 구매하기 위한 다이얼로그 클래스
 * 동물의 밥, 청결 도구 등등을 구매할 수 있습니다.
 * <p>
 * 사용자가 아이템을 구매하면 리스너로 감지하여 사용자의 재화에서 금액을 차감합니다.
 */
public class CustomPurchaseDialog extends Dialog {

    private final InventoryHelper inventoryHelper;
    private final int itemId;
    private int count;
    private int price;
    private CustomPurchaseDialogBinding binding;
    private final Context context;
    private final ItemDetail itemDetail;
    private final Drawable drawable;

    private ItemPurchaseListener itemPurchaseListener;    // 구매 했다고 단방향으로 알려주는 리스너 인터페이스

    /**
     * Sets item purchase listener.
     *
     * @param itemPurchaseListener the item purchase listener
     */
    public void setItemPurchaseListener(ItemPurchaseListener itemPurchaseListener) {
        this.itemPurchaseListener = itemPurchaseListener;
    }

    /**
     * The interface Item purchase listener.
     */
    public interface ItemPurchaseListener {
        /**
         * Item purchase.
         */
        void itemPurchase();

    }

    /**
     * 구매 다이얼로그 생성자
     * 사용자가 선택한 아이템으로 다이얼로그 뷰를 세팅합니다.
     *
     * @param context the context
     * @param id      the id
     * @param background
     * @throws IOException the io exception
     */
    public CustomPurchaseDialog(@NonNull Context context, int id, Drawable background) throws IOException {
        super(context);
        this.context = context;
        this.itemId = id;
        this.drawable = background;

        this.inventoryHelper = new InventoryHelper(context);
        this.itemDetail = inventoryHelper.findItem(itemId);

        this.count = 1;
        this.price = this.itemDetail.getPrice();
    }

    /**
     * 다이얼로그 뷰가 생성되면 실행되는 메서드
     *
     * 사용자가 아이템을 구매할 수 있는지 판단하고 구매 로직을 실행합니다.
     */
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
        this.getWindow().setLayout((int)(screenWidth * 0.9), (int)(screenHeight * 0.4));

        binding.tvItemName.setText(itemDetail.getName());    // 아이템 이름

        price = itemDetail.getPrice();
        binding.tvItemPrice.setText(price + "원");    // 아이템 가격

        StringBuilder sb = new StringBuilder();
        itemDetail.getEffects().forEach(e -> {
            String toKorean = "효과값";
            switch (e) {
                case "HUNGER":
                    toKorean = "배고픔";
                    break;
                case "MOOD":
                    toKorean = "기분";
                    break;
                case "CLEANLINESS":
                    toKorean = "청결";
                    break;
                case "DURABILITY":
                    toKorean = "내구성";
                    break;
                case "THIRST":
                    toKorean = "갈증해소";
                    break;
            }
            sb.append(String.format("%s\n", toKorean));
        });

        String effects = sb.toString();
        binding.tvItemEffect.setText(effects); // 아이템 효과
        sb.setLength(0);

        itemDetail.getValues().forEach(e -> sb.append(String.format(Locale.getDefault(), "%d\n", e)));
        String values = sb.toString();
        binding.tvItemValue.setText(values); // 아이템 효과 값
        sb.setLength(0);

        binding.tvItemInfo.setText(itemDetail.getExplanation());    // 아이템 정보
        Glide.with(context).load(drawable).into(binding.ivItem);    // 아이템 그림 넣기

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
        binding.tvItemNum.setText(String.valueOf(count) + "개");
        binding.tvItemPrice.setText(String.valueOf(price) + "원");
    }
}
