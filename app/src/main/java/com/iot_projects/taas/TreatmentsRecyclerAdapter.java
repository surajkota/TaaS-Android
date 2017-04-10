package com.iot_projects.taas;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iot_projects.taas.databinding.CardTreatmentBinding;
import com.iot_projects.taas.models.Treatment;


import java.util.List;

/**
 * Created by Kartikk on 4/10/2017.
 */

public class TreatmentsRecyclerAdapter extends RecyclerView.Adapter<TreatmentsRecyclerAdapter.ViewHolder> {

    private List<Treatment> treatmentList;

    public TreatmentsRecyclerAdapter(List<Treatment> treatmentList) {
        this.treatmentList = treatmentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardTreatmentBinding cardTreatmentBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.card_treatment, parent, false
                );
        return new ViewHolder(cardTreatmentBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Context context = holder.itemView.getContext();
        final Treatment treatment = treatmentList.get(position);
        holder.cardTreatmentBinding.setTreatment(treatment);
        holder.cardTreatmentBinding.executePendingBindings();
        holder.cardTreatmentBinding.treatmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TreatmentDetailActivity.class);
                intent.putExtra("treatment", treatment);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return treatmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardTreatmentBinding cardTreatmentBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            cardTreatmentBinding = DataBindingUtil.findBinding(itemView);
        }
    }
}
