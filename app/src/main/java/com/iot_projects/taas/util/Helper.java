package com.iot_projects.taas.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot_projects.taas.R;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Kartikk on 4/10/2017.
 */

public class Helper {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(Constants.baseURL)
                    .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper())).build();
        }
        return retrofit;
    }

    public static Endpoints getRetrofitEndpoints() {
        return getRetrofitInstance().create(Endpoints.class);
    }

    public static void cancelNotification(Context context, int nID) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(nID);
    }

    public static int sendNotificationYesNo(Context context, String title, String body, Intent yesIntent, Intent noIntent) {
        return sendNotificationWithActions(context, (int) System.currentTimeMillis(), title, body, R.drawable.vector_bell_dark,
                noIntent, context.getString(R.string.noti_no), R.drawable.vector_close_dark,
                yesIntent, context.getString(R.string.noti_yes), R.drawable.vector_check_dark);
    }

    public static int sendNotificationWithActions(Context context, int nID, String title, String body, int iconID, Intent intent1, String action1Title, int action1IconID, Intent intent2, String action2Title, int action2IconID) {
        int requestID = (int) System.currentTimeMillis();
        int flags = Notification.FLAG_AUTO_CANCEL;
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, requestID, intent1, flags);
        int requestID2 = requestID + 1;
        int flags2 = Notification.FLAG_AUTO_CANCEL;
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, requestID2, intent2, flags2);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(iconID)
                .setContentTitle(title)
                .setContentText(body)
                .addAction(action1IconID, action1Title, pendingIntent1)
                .addAction(action2IconID, action2Title, pendingIntent2)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(nID, notification);
        return nID;
    }

    public static int sendNotification(Context context, String title, String body) {
        return sendNotification((int) System.currentTimeMillis(), context, title, body, R.drawable.vector_bell_dark);
    }

    public static int sendNotification(int nID, Context context, String title, String body, int iconID) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(iconID)
                .setContentTitle(title)
                .setContentText(body);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(nID, builder.build());
        return nID;
    }

}
