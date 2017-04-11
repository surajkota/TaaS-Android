package com.iot_projects.taas;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iot_projects.taas.databinding.CardSubscribeTreatmentBinding;
import com.iot_projects.taas.models.Treatment;

import java.util.List;

/**
 * Created by Kartikk on 4/9/2017.
 */

public class SubscribeRecyclerAdapter extends RecyclerView.Adapter<SubscribeRecyclerAdapter.ViewHolder> {

    private List<Treatment> treatmentList;

    public SubscribeRecyclerAdapter(List<Treatment> treatmentList) {
        this.treatmentList = treatmentList;
    }

    @Override
    public SubscribeRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardSubscribeTreatmentBinding cardSubscribeTreatmentBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.card_subscribe_treatment, parent, false
        );
        return new ViewHolder(cardSubscribeTreatmentBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(SubscribeRecyclerAdapter.ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        holder.cardSubscribeTreatmentBinding.setTreatment(treatmentList.get(position));
        holder.cardSubscribeTreatmentBinding.executePendingBindings();
        holder.cardSubscribeTreatmentBinding.subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return treatmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardSubscribeTreatmentBinding cardSubscribeTreatmentBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            cardSubscribeTreatmentBinding = DataBindingUtil.findBinding(itemView);
        }
    }
}
