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
import com.iot_projects.taas.databinding.SubscribePageBinding;
import com.iot_projects.taas.models.Treatment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kartikk on 4/9/2017.
 */

public class SubscribeFragment extends Fragment {

    SubscribePageBinding subscribePageBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        subscribePageBinding = DataBindingUtil.inflate(inflater, R.layout.subscribe_page, container, false);
        RecyclerView recyclerView = subscribePageBinding.subscribeRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Treatment> treatmentList = new ArrayList<>();
        String treatmentString = "{\n" +
                "    \"treatmentName\" : \"Treatment 1\",\n" +
                "    \"duration\" : 90,\n" +
                "    \"medication\" :\n" +
                "    [\n" +
                "        {\n" +
                "            \"medicineId\" : \"AE8934BC\",\n" +
                "            \"timeThreshold\" :[\"10:00AM\", \"3:00PM\", \"10:00PM\"],\n" +
                "            \"quantity\" : 1,\n" +
                "            \"procedure\" : \"Mix in 200ml warm water and drink\",\n" +
                "            \"frequency\" : 2,\n" +
                "            \"startDay\" : 1,\n" +
                "            \"endDay\" : 90\n" +
                "        },\n" +
                "        {\n" +
                "            \"medicineId\" : \"ASDF92AV\",\n" +
                "            \"timeThreshold\" :[\"10:00AM\", \"10:00PM\"],\n" +
                "            \"quantity\" : 1,\n" +
                "            \"procedure\" : \"Swallow without biting. Drink warm water if needed\",\n" +
                "            \"frequency\" : 1,\n" +
                "            \"startDay\" : 1,\n" +
                "            \"endDay\" : 10\n" +
                "        }\n" +
                "    ],\n" +
                "    \"restrictedFood\":\n" +
                "    [\n" +
                "        {\n" +
                "            \"foodId\" : \"OIREW342AS\",\n" +
                "            \"startDay\" : 1,\n" +
                "            \"endDay\" : 10\n" +
                "        },\n" +
                "        {\n" +
                "            \"foodId\" : \"LSADFW93AG\",\n" +
                "            \"startDay\" : 30,\n" +
                "            \"endDay\" : 90\n" +
                "        }\n" +
                "    ],\n" +
                "    \"dangerSigns\" :\n" +
                "    [\n" +
                "        {\n" +
                "            \"symptom\": \"Lips/finger nails turning blue\",\n" +
                "            \"whenToAlert\": [5,20] \n" +
                "        },\n" +
                "        {\n" +
                "            \"symptom\": \"Going out of breath while walking\",\n" +
                "            \"whenToAlert\": [2]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"sleep\" : {\n" +
                "        \"minThreshold\": 6,\n" +
                "        \"maxThreshold\": 8\n" +
                "    }\n" +
                "}";

        try {
            Treatment treatment = new ObjectMapper().readValue(treatmentString, Treatment.class);
            treatmentList.add(treatment);
            recyclerView.setAdapter(new SubscribeRecyclerAdapter(treatmentList));
            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("treatmentString", treatmentString);
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subscribePageBinding.getRoot();
    }
}
