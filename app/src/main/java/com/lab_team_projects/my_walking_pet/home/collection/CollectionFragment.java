package com.lab_team_projects.my_walking_pet.home.collection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.lab_team_projects.my_walking_pet.adapters.CollectionAdapter;
import com.lab_team_projects.my_walking_pet.app.MainActivity;
import com.lab_team_projects.my_walking_pet.databinding.FragmentCollectionBinding;

import java.util.ArrayList;
import java.util.List;

public class CollectionFragment extends Fragment {

    private FragmentCollectionBinding binding;
    private MainActivity mMainActivity;

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

        List<Collection> collectionList = new ArrayList<>();
        for(int i=0; i<40; i++) {
            collectionList.add(new Collection());
        }


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