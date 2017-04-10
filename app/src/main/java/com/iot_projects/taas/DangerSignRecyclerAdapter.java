package com.iot_projects.taas;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iot_projects.taas.databinding.ItemDangerSignBinding;
import com.iot_projects.taas.models.DangerSign;

import java.util.List;

/**
 * Created by Kartikk on 4/10/2017.
 */

public class DangerSignRecyclerAdapter extends RecyclerView.Adapter<DangerSignRecyclerAdapter.ViewHolder> {

    List<DangerSign> dangerSignList;

    public DangerSignRecyclerAdapter(List<DangerSign> dangerSignList) {
        this.dangerSignList = dangerSignList;
    }

    @Override
    public DangerSignRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDangerSignBinding itemDangerSignBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_danger_sign, parent, false
        );
        return new ViewHolder(itemDangerSignBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(DangerSignRecyclerAdapter.ViewHolder holder, int position) {
        holder.itemDangerSignBinding.setDanger(dangerSignList.get(position));
        holder.itemDangerSignBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dangerSignList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemDangerSignBinding itemDangerSignBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            itemDangerSignBinding = DataBindingUtil.findBinding(itemView);
        }
    }

}
