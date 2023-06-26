package com.lab_team_projects.my_walking_pet.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.collection.Collection;

import java.util.List;

/**
 * 도감 프래그먼트에서 리사이클러뷰를 이용하여 동물 도감을 구현하기 위한 어댑터 클래스
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    private List<Collection> collectionList;

    @NonNull
    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_collection_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    /**
     * Sets collection list.
     *
     * @param collectionList the collection list
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setCollectionList(List<Collection> collectionList) {
        this.collectionList = collectionList;
        notifyDataSetChanged();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
