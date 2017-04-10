package com.iot_projects.taas;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot_projects.taas.models.Medication;
import com.iot_projects.taas.models.Treatment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlarmManager;
import android.widget.Toast;

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
                    mainActivity.installTDL();
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
}
