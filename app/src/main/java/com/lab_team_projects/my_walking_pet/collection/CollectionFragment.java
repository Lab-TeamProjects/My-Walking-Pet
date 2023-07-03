package com.lab_team_projects.my_walking_pet.collection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.lab_team_projects.my_walking_pet.adapters.CollectionAdapter;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.databinding.FragmentCollectionBinding;
import com.lab_team_projects.my_walking_pet.helpers.AnimalMappingHelper;
import com.lab_team_projects.my_walking_pet.home.Animal;
import com.lab_team_projects.my_walking_pet.home.Broods;
import com.lab_team_projects.my_walking_pet.login.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 도감 프래그먼트 클래스
 */
public class CollectionFragment extends Fragment {

    private FragmentCollectionBinding binding;
    private MainActivity mMainActivity;

    /**
     * Instantiates a new Collection fragment.
     */
    public CollectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        mMainActivity.onAppBarLoad();

        // 유저로부터 도감 컬렉션을 받아옴
        User user = GameManager.getInstance().getUser();
        List<Collection> collectionList = user.getCollectionList();

        // 리사이클러뷰 설정

        CollectionAdapter collectionAdapter = new CollectionAdapter();
        collectionAdapter.setCollectionList(collectionList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);

        binding.rvCollection.setHasFixedSize(true);
        binding.rvCollection.setAdapter(collectionAdapter);
        binding.rvCollection.setLayoutManager(gridLayoutManager);

        // 현재 기르고 있는 동물 정보로 도감 상단에 정보 채우기

        AnimalMappingHelper mappingHelper = new AnimalMappingHelper();

        Animal nowAnimal = user.getAnimalList().get(user.getNowSelectedPet());
        Broods nowBrood = Broods.valueOf(nowAnimal.getBrood());

        binding.tvAnimalName.setText(String.format("이름 : %s", mappingHelper.getBroodsName(nowBrood)));
        binding.tvAnimalLv.setText(String.format(Locale.getDefault(), "레벨 : %d", nowAnimal.getLevel()));

        int nowImg = mappingHelper.getImgValue(nowBrood)[nowAnimal.getLevel()];
        Glide.with(requireContext()).load(nowImg).into(binding.ivAnimalImg);

        // 현재 isHave 변수가 true 값인 개수를 구함
        int collectsSize = collectionList.size();
        int isHaveCollectsSize = (int) collectionList.stream().filter(Collection::getHave).count();
        float ratio = 100.0f / collectsSize;
        float normalizedProgress = isHaveCollectsSize * ratio;
        binding.progressBar.setProgress(normalizedProgress);



        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}