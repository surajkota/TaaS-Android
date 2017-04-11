package com.iot_projects.taas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot_projects.taas.models.Subscription;

import java.io.IOException;
import java.util.Random;

/**
 * Created by adhit on 4/7/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String medicine = intent.getStringExtra("medicine");
        Long time = Long.parseLong(intent.getStringExtra("time"));
        if((System.currentTimeMillis()-time) <= 600000) {
            String s = "";
            SharedPreferences settings = context.getSharedPreferences("myPref", 0);
            if (settings.contains("subscription"))
                s = settings.getString("subscription", "");
            //Toast.makeText(context, medicine + " " + time, Toast.LENGTH_SHORT).show();
            if(!s.equals("")) {
                Log.d("Debug", medicine + " " + time);
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Subscription subscription = objectMapper.readValue(s,Subscription.class);
                    Boolean flag = true;
                    Log.d("Debug", subscription.toString());
                    if(subscription.getMedicineNotTakenMap().containsKey(medicine))
                    {
                        if(subscription.getMedicineNotTakenMap().get(medicine).containsKey(time))
                        {
                            flag = subscription.getMedicineNotTakenMap().get(medicine).get(time);
                            Log.d("Debug","Flag for medicine "+ medicine + " is "+ flag);
                            if(flag) {
                                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                PendingIntent pendingIntent;
                                Intent alarmIntent = new Intent(context, SkippedMedicineReceiver.class);
                                alarmIntent.putExtra("medicine", medicine);
                                alarmIntent.putExtra("time", time.toString());
                                Random random = new Random();
                                pendingIntent = PendingIntent.getBroadcast(context, random.nextInt(), alarmIntent, 0);
                                manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, pendingIntent);
                                Log.d("Debug", "Final Alarm for medicine " + medicine + " is set");

                                //TODO Show medicine reminder notification here
                                Intent speechIntent = new Intent();
                                speechIntent.setClass(context, ReadTheMessage.class);
                                speechIntent.putExtra("MESSAGE", "Medicine reminder!");
                                speechIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                context.startActivity(speechIntent);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            //Toast.makeText(context, String.valueOf(time), Toast.LENGTH_SHORT).show();
        }
    }
}
