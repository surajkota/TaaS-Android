package com.iot_projects.taas;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.altbeacon.beacon.AltBeaconParser.TAG;

/**
 * Created by adhit on 3/18/2017.
 */

class AsyncPost extends AsyncTask<Void,Void,Void> {

    String endpoint;
    String area;
    public AsyncPost(String ep, String a)
    {
        endpoint = ep;
        area = a;
    }
    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url; //Enter URL here
            url = new URL(endpoint);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            Log.d(TAG, "sending post to "+ endpoint + " data : "+ area);
            httpURLConnection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("area", area);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            wr.flush();
            wr.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }

            Log.d(TAG, sb.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
