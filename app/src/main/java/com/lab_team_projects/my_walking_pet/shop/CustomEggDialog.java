package com.lab_team_projects.my_walking_pet.shop;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.databinding.CustomEggDialogBinding;
import com.lab_team_projects.my_walking_pet.home.Animal;
import com.lab_team_projects.my_walking_pet.home.Broods;
import com.lab_team_projects.my_walking_pet.login.User;

import java.io.IOException;

/**
 * 동물 알 구입 다이얼로그
 * 사용자는 해당 다이얼로그를 이용하여 동물을 기르기 위한 알을 구매할 수 있습니다.
 */
public class CustomEggDialog extends Dialog {

    /**
     * xml id로 직접 접근하기 위한 바인딩 클래스
     */
    private CustomEggDialogBinding binding;
    private final Context context;

    private ItemPurchaseListener itemPurchaseListener;    // 구매 했다고 단방향으로 알려주는 리스너 인터페이스

    /**
     * 동물 알 사진들
     */
    private final int[] eggImages = {R.drawable.img_egg_black, R.drawable.img_egg_blue, R.drawable.img_egg_white, R.drawable.img_egg_yellow};
    private int currentIndex;
    private final String[][] eggs = {{"튼튼한 알", "50000"}, {"아름다운 알", "500000"}, {"빛나는 알", "600000"}, {"깨끗한 알", "6000000"}};

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
     * Instantiates a new Custom egg dialog.
     *
     * @param context the context
     * @throws IOException the io exception
     */
    public CustomEggDialog(@NonNull Context context) throws IOException {
        super(context);
        this.context = context;
    }

    /**
     * 사용자의 화면 크기에 비례하여 다이얼로그 창의 크기를 설정합니다.
     */
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

        // 구매 버튼
        binding.btnOK.setOnClickListener(v->{
            int selectEggPrice = Integer.parseInt(eggs[currentIndex][1]);

            User user = GameManager.getInstance().getUser();
            if (user.getMoney() < selectEggPrice) {
                Toast.makeText(context, "돈이 부족합니다.", Toast.LENGTH_SHORT).show();
            } else {
                // 동물 이름 설정 다이얼로그 표시
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.custom_input_dialog, null);

                final EditText editText = view.findViewById(R.id.editText);
                Button okButton = view.findViewById(R.id.btnOK);
                Button cancelButton = view.findViewById(R.id.btnCancel);

                // Dialog 생성
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(view);


                // 화면 크기, 배경 설정
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_yellow);
                dialog.getWindow().setLayout((int)(screenWidth * 0.8), (int)(screenHeight * 0.3));

                // 최종적으로 구매 버튼 누름
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 유저 재화에서 알 금액 만큼 차감
                        user.setMoney(user.getMoney() - selectEggPrice);

                        // 사용자가 입력한 이름
                        String input = editText.getText().toString();

                        // 선택한 알에 따른 종족
                        Broods broods;
                        switch (currentIndex) {
                            case 2:
                                broods = Broods.DOG;
                                break;
                            case 3:
                                broods = Broods.MONKEY;
                                break;
                            case 4:
                                broods = Broods.HAMSTER;
                                break;
                            case 1:
                            default:
                                broods = Broods.CAT;
                        }

                        // 알 생성
                        Animal animal = new Animal(input, broods.name(), user.getUid(), user.getUid());

                        // 유저 동물 리스트에 추가
                        user.getAnimalList().add(animal);

                        Toast.makeText(context, "알 구매 완료", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                        dis();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // 대화 상자를 표시합니다.
                dialog.show();

            }
        });

        // imageSwitcher 설정
        binding.isEggPicture.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;
            }
        });

        // 첫 번째 이미지 설정
        binding.isEggPicture.setImageResource(eggImages[currentIndex]);

        // 이미지 스위처 왼쪽 버튼 설정
        binding.ibLeft.setOnClickListener(v -> {
            currentIndex--;
            if (currentIndex == -1) {
                currentIndex = eggImages.length - 1; // 배열의 끝에 도달하면 처음으로 돌아갑니다.
            }

            setCurrentEgg();
        });

        // 이미지 스위처 오른쪽 버튼 설정
        binding.ibRight.setOnClickListener(v -> {
            currentIndex++;
            if (currentIndex == eggImages.length) {
                currentIndex = 0; // 배열의 끝에 도달하면 처음으로 돌아갑니다.
            }

            setCurrentEgg();
        });

    }

    void setCurrentEgg() {
        binding.isEggPicture.setImageResource(eggImages[currentIndex]); // 다음 이미지로 전환
        binding.tvEggInfo.setText(String.format("%s\n%s원", eggs[currentIndex][0], eggs[currentIndex][1]));
    }

    void dis() {
        dismiss();
    }


}
