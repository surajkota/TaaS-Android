package com.iot_projects.taas;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot_projects.taas.databinding.TreatmentsPageBinding;
import com.iot_projects.taas.models.Treatment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kartikk on 4/10/2017.
 */

public class TreatmentsFragment extends Fragment {

    TreatmentsPageBinding treatmentsPageBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        treatmentsPageBinding = DataBindingUtil.inflate(inflater, R.layout.treatments_page, container, false);
        RecyclerView recyclerView = treatmentsPageBinding.treatmentsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
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
       return treatmentsPageBinding.getRoot();
    }
}
