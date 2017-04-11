package com.iot_projects.taas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot_projects.taas.models.DangerSign;
import com.iot_projects.taas.models.Medication;
import com.iot_projects.taas.models.Treatment;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by adhit on 3/18/2017.
 */

public class AsyncGet extends Thread {

    String endpoint;
    Context context;
    MainActivity mainActivity;
    public AsyncGet(String ep, Context c)
    {
        endpoint = ep;
        context = c;
        mainActivity = (MainActivity) context;
    }

    @Override
    public void run() {
        try {
            Log.d("Debug", "Inside AsyncGet!");
            final String USER_AGENT = "Mozilla/5.0";
            URL obj = new URL(endpoint);
            RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
            StringRequest MyStringRequest = new StringRequest(Request.Method.GET, endpoint, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //This code is executed if the server responds, whether or not the response contains data.
                    //The String 'response' contains the server's response.
                    Log.d(TAG, response);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Treatment treatment = null;
                    try {
                        treatment = objectMapper.readValue(response,Treatment.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BackgroundService.subscription.setTreatment(treatment);
                    Log.d("Debug", BackgroundService.subscription.toString());
                    installTDL();
                }
            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                }
            });
            MyRequestQueue.add(MyStringRequest);

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }

    public void installTDL() {
        Treatment treatment = BackgroundService.subscription.getTreatment();
        int intentId = 0;
        Map<String,Map<Long, Boolean>> medicineNotTakenMap = new HashMap<>();
        Map<String,Integer> improperTimeMedicine = new HashMap<>();
        Map<String,Integer> skippedMedicine = new HashMap<>();
        for(Medication m : treatment.getMedication()) {
            Map<Long, Boolean> medicineNotTaken = new HashMap<>();
            BackgroundService.subscribeToTopic(m.getMedicineId());
            improperTimeMedicine.put(m.getMedicineId(),0);
            skippedMedicine.put(m.getMedicineId(),0);
            for(String time : m.getTimeThreshold()) {
                int hour, minute;
                String[] tokens = time.split(":");
                String ampm = tokens[1].substring(tokens[1].length()-2);
                Log.d("Debug", ampm);
                if(ampm.equals("AM"))
                {
                    hour = Integer.parseInt(tokens[0]);
                    if(hour==12)
                        hour = 0;
                }
                else
                    hour = 12+Integer.parseInt((tokens[0]));
                minute = Integer.parseInt(tokens[1].substring(0,tokens[1].length()-2).replaceAll("\\s+",""));
                Log.d("Debug", "Hour : "+hour);
                Log.d("Debug", "Minute : "+minute);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                Long time1 = calendar.getTimeInMillis();
                Log.d("Debug","Putting "+ time1 + " to true");
                medicineNotTaken.put(time1,true);

                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent;
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                alarmIntent.putExtra("medicine", m.getMedicineId());
                alarmIntent.putExtra("time", time1.toString());
                Log.d("Debug", "Putting extra "+time1);
                pendingIntent = PendingIntent.getBroadcast(context, intentId, alarmIntent, 0);
                intentId++;
                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time1, AlarmManager.INTERVAL_DAY, pendingIntent);
                Log.d("Debug","Alarm for medicine "+ m.getMedicineId()+ " and time "+ time1 + " is set");
            }
            medicineNotTakenMap.put(m.getMedicineId(),medicineNotTaken);
        }
        for(DangerSign dangerSign : treatment.getDangerSigns())
        {
            for(Integer day : dangerSign.getWhenToAlert()) {
                Long time = System.currentTimeMillis();
                time+=day*24*60*60*1000;
                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent;
                Intent alarmIntent = new Intent(context, SideEffectsReceiver.class);
                alarmIntent.putExtra("symptom", dangerSign.getSymptom());
                alarmIntent.putExtra("time", time);
                Log.d("Debug", "Putting extra " + time + dangerSign.getSymptom());
                pendingIntent = PendingIntent.getBroadcast(context, intentId, alarmIntent, 0);
                intentId++;
                manager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
        }
        BackgroundService.subscription.setMedicineNotTakenMap(medicineNotTakenMap);
        BackgroundService.subscription.setImproperTimeMedicine(improperTimeMedicine);
        BackgroundService.subscription.setSkippedMedicine(skippedMedicine);
        ObjectMapper subscMapper = new ObjectMapper();
        String subscStr = null;
        try {
            subscStr = subscMapper.writeValueAsString(BackgroundService.subscription);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Log.d("Debug", subscStr);
        SharedPreferences settings = context.getSharedPreferences("myPref", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("subscription", subscStr);
        // Commit the edits!
        editor.commit();
    }
}
