package com.lab_team_projects.my_walking_pet.collection;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.databinding.CustomCollectionDialogBinding;
import com.lab_team_projects.my_walking_pet.helpers.AnimalMappingHelper;
import com.lab_team_projects.my_walking_pet.home.Broods;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomCollectionDialog extends Dialog {
    private CustomCollectionDialogBinding binding;
    private final Context context;
    private final Collection collection;

    private final Map<Broods, List<String>> infoMap = Map.of(
            Broods.CAT, Arrays.asList("귀여운 아기 고양이", "귀여운 꼬마 고양이", "귀여운 어른 고양이"),
            Broods.DOG, Arrays.asList("귀여운 아기 강아지", "귀여운 꼬마 강아지", "귀여운 어른 강아지"),
            Broods.MONKEY, Arrays.asList("귀여운 아기 원숭이", "귀여운 꼬마 원숭이", "귀여운 어른 원숭이"),
            Broods.HAMSTER, Arrays.asList("귀여운 아기 햄스터", "귀여운 꼬마 햄스터", "귀여운 어른 햄스터")
    );

    public CustomCollectionDialog(@NonNull Context context, Collection collection) {
        super(context);
        this.context = context;
        this.collection = collection;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CustomCollectionDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_yellow);
        // 스마트폰의 화면 크기를 가져옴
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int)(screenWidth * 0.9);
        params.height = (int)(screenHeight * 0.6);
        getWindow().setAttributes(params);

        AnimalMappingHelper animalMappingHelper = new AnimalMappingHelper();
        int imgId = animalMappingHelper.getAnimalImg(collection.getBroodName(), collection.getLv());

        Glide.with(context).load(imgId).into(binding.ivAnimalImg);
        String animalInfo = Objects.requireNonNull(infoMap.get(Broods.valueOf(collection.getBroodName()))).get(collection.getLv() - 1);

        binding.tvContent.setText(animalInfo);
    }

}
