package com.iot_projects.taas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot_projects.taas.models.Subscription;

import java.io.IOException;

public class SkippedMedicineReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String medicine = intent.getStringExtra("medicine");
        String time = intent.getStringExtra("time");

        String s = "";
        SharedPreferences settings = context.getSharedPreferences("myPref", 0);
        if (settings.contains("subscription"))
            s = settings.getString("subscription", "hi");
        if(!s.equals("hi")) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Subscription subscription = objectMapper.readValue(s,Subscription.class);
                Log.d("Debug", subscription.toString());
                int flag = 0;
                int skippedMedicine = 0;
                if(subscription.getSkippedMedicine().containsKey(medicine))
                    skippedMedicine = subscription.getSkippedMedicine().get(medicine);
                subscription.getSkippedMedicine().put(medicine,skippedMedicine+1);

                ObjectMapper objMapper = new ObjectMapper();
                String subsStr = objMapper.writeValueAsString(subscription);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("subscription", subsStr);
                Log.d("Debug", subsStr);
                editor.commit();
                if((skippedMedicine+1)>=5) {
                    //TODO Report to doctor (Post request to baseUrl/medicineSkip with subscription id and medicine name as payload
                    Toast.makeText(context, "Medicine " + medicine + " is skipped more than 5 times!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("Debug", "Final alarm for medicine "+medicine + " " + time + " is fired!");
        Toast.makeText(context, "Final alarm for " + medicine + " " + time + " is fired!", Toast.LENGTH_SHORT).show();
    }
}
