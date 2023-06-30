package com.lab_team_projects.my_walking_pet.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lab_team_projects.my_walking_pet.R;
import com.lab_team_projects.my_walking_pet.collection.Collection;
import com.lab_team_projects.my_walking_pet.collection.CustomCollectionDialog;
import com.lab_team_projects.my_walking_pet.databinding.CustomCollectionItemBinding;
import com.lab_team_projects.my_walking_pet.helpers.AnimalMappingHelper;
import com.lab_team_projects.my_walking_pet.home.Broods;

import java.util.List;
import java.util.Map;

/**
 * 도감 프래그먼트에서 리사이클러뷰를 이용하여 동물 도감을 구현하기 위한 어댑터 클래스
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    private List<Collection> collectionList;
    private final AnimalMappingHelper animalMappingHelper = new AnimalMappingHelper();
    private Context context;

    @NonNull
    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        CustomCollectionItemBinding binding = CustomCollectionItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionAdapter.ViewHolder holder, int position) {
        Collection collection = collectionList.get(position);
        holder.bind(collection);
        holder.binding.getRoot().setOnClickListener(v->{
            if (!collection.getHave()) {
                return;
            }
            CustomCollectionDialog dialog = new CustomCollectionDialog(context, collection);
            dialog.show();
        });
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
     * 도감 리스트를 받아서 isHave 변수가 true이면 동물의 사진을 넣고 아니면 ? 사진을 넣는다.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        protected CustomCollectionItemBinding binding;
        public ViewHolder(@NonNull CustomCollectionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        /**
         * 동물과 매핑되어있는 이미지를 도감 아이템에 설정하는데
         * 현재 동물이
         * @param collection 도감 아이템
         */
        protected void bind(Collection collection) {
            boolean isHave = collection.getHave();
            int defaultImg = R.drawable.ic_baseline_gray2;
            int[] broodsImgMap = animalMappingHelper.getImgValueNoneEgg(Broods.valueOf(collection.getBroodName()));
            int animalImg = broodsImgMap[collection.getLv()-1];

            Glide.with(context)
                    .load(isHave ? animalImg : defaultImg)
                    .into(binding.ivContent);
        }
    }
}
