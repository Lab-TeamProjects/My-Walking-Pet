package com.lab_team_projects.my_walking_pet.collection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.lab_team_projects.my_walking_pet.adapters.CollectionAdapter;
import com.lab_team_projects.my_walking_pet.app.GameManager;
import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.databinding.FragmentCollectionBinding;
import com.lab_team_projects.my_walking_pet.login.User;

import java.util.ArrayList;
import java.util.List;

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

        CollectionAdapter collectionAdapter = new CollectionAdapter();
        collectionAdapter.setCollectionList(collectionList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);

        binding.rvCollection.setHasFixedSize(true);
        binding.rvCollection.setAdapter(collectionAdapter);
        binding.rvCollection.setLayoutManager(gridLayoutManager);


        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}