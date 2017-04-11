package com.iot_projects.taas;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.iot_projects.taas.databinding.TreatmentDetailBinding;
import com.iot_projects.taas.models.Treatment;


/**
 * Created by Kartikk on 4/10/2017.
 */

public class TreatmentDetailActivity extends AppCompatActivity {

    TreatmentDetailBinding treatmentDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        treatmentDetailBinding = DataBindingUtil.setContentView(this, R.layout.treatment_detail);
        Treatment treatment = (Treatment) getIntent().getExtras().get("treatment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (treatment != null) {
            setTitle(treatment.getTreatmentName());

            if (treatment.getMedication() != null && !treatment.getMedication().isEmpty()) {
                treatmentDetailBinding.medicationDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                treatmentDetailBinding.medicationDetailRecyclerView.setAdapter(new MedicationRecyclerAdapter(treatment.getMedication()));
            } else {
                treatmentDetailBinding.medicationDetailCardView.setVisibility(View.GONE);
            }

            if (treatment.getDangerSigns() != null && !treatment.getDangerSigns().isEmpty()) {
                treatmentDetailBinding.dangerDetailRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false));
                treatmentDetailBinding.dangerDetailRecyclerView.setAdapter(new DangerSignRecyclerAdapter(treatment.getDangerSigns()));
            } else {
                treatmentDetailBinding.dangerDetailCardView.setVisibility(View.GONE);
            }

            if (treatment.getRestrictedFood() != null && !treatment.getRestrictedFood().isEmpty()) {
                treatmentDetailBinding.restrictedDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                treatmentDetailBinding.restrictedDetailRecyclerView.setAdapter(new RestrictedFoodRecyclerAdapter(treatment.getRestrictedFood()));
            } else {
                treatmentDetailBinding.restrictedDetailCardView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
