package com.iot_projects.taas;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iot_projects.taas.databinding.ItemRestrictedFoodBinding;
import com.iot_projects.taas.models.RestrictedFood;

import java.util.List;

/**
 * Created by Kartikk on 4/10/2017.
 */

public class RestrictedFoodRecyclerAdapter extends RecyclerView.Adapter<RestrictedFoodRecyclerAdapter.ViewHolder> {

    List<RestrictedFood> restrictedFoodList;

    public RestrictedFoodRecyclerAdapter(List<RestrictedFood> restrictedFoodList) {
        this.restrictedFoodList = restrictedFoodList;
    }

    @Override
    public RestrictedFoodRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRestrictedFoodBinding itemRestrictedFoodBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_restricted_food, parent, false
        );
        return new ViewHolder(itemRestrictedFoodBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(RestrictedFoodRecyclerAdapter.ViewHolder holder, int position) {
        holder.itemRestrictedFoodBinding.setRestricted(restrictedFoodList.get(position));
        holder.itemRestrictedFoodBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return restrictedFoodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemRestrictedFoodBinding itemRestrictedFoodBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            itemRestrictedFoodBinding = DataBindingUtil.findBinding(itemView);
        }
    }
}
