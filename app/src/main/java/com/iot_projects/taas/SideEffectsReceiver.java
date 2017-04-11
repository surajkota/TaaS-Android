package com.iot_projects.taas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SideEffectsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String symptom = intent.getStringExtra("symptom");
        Long time = Long.parseLong(intent.getStringExtra("time"));

        //TODO Show side effect notification, if user clicks yes, send post request to baseUrl/sideEffect
    }
}
