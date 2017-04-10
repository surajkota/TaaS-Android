package com.iot_projects.taas;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iot_projects.taas.databinding.ItemMedicationDetailBinding;
import com.iot_projects.taas.models.Medication;

import java.util.List;

/**
 * Created by Kartikk on 4/10/2017.
 */

public class MedicationRecyclerAdapter extends RecyclerView.Adapter<MedicationRecyclerAdapter.ViewHolder> {

    List<Medication> medicationList;

    public MedicationRecyclerAdapter(List<Medication> medicationList) {
        this.medicationList = medicationList;
    }

    @Override
    public MedicationRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMedicationDetailBinding itemMedicationDetailBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_medication_detail, parent, false
        );
        return new ViewHolder(itemMedicationDetailBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(MedicationRecyclerAdapter.ViewHolder holder, int position) {
        holder.itemMedicationDetailBinding.setMedication(medicationList.get(position));
        holder.itemMedicationDetailBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemMedicationDetailBinding itemMedicationDetailBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            itemMedicationDetailBinding = DataBindingUtil.findBinding(itemView);
        }
    }
}
