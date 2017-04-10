package com.iot_projects.taas;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot_projects.taas.databinding.TreatmentsPageBinding;
import com.iot_projects.taas.models.Treatment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kartikk on 4/10/2017.
 */

public class TreatmentsActivity extends AppCompatActivity {

    TreatmentsPageBinding treatmentsPageBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        treatmentsPageBinding = DataBindingUtil.setContentView(this, R.layout.treatments_page);
        RecyclerView recyclerView = treatmentsPageBinding.treatmentsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        String treatmentString = mSettings.getString("treatmentString", "");
        List<Treatment> treatmentList = new ArrayList<>();
        Treatment treatment = null;
        try {
            treatment = new ObjectMapper().readValue(treatmentString, Treatment.class);
            treatmentList.add(treatment);
            recyclerView.setAdapter(new TreatmentsRecyclerAdapter(treatmentList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
