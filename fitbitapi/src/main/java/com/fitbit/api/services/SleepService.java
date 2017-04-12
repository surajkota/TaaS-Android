package com.fitbit.api.services;

import android.app.Activity;
import android.content.Loader;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fitbit.api.APIUtils;
import com.fitbit.api.exceptions.MissingScopesException;
import com.fitbit.api.exceptions.TokenExpiredException;
import com.fitbit.authentication.AccessToken;
import com.fitbit.authentication.AuthenticationManager;
import com.fitbit.authentication.Scope;
import com.fitbit.fitbitcommon.network.BasicHttpRequest;
import com.fitbit.fitbitcommon.network.BasicHttpResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by adhit on 4/11/2017.
 */

public class SleepService {
    private final static String SLEEP_URL = "https://api.fitbit.com/1/user/-/sleep/date/";
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


    public static void getDailySleepSummary(Activity activityContext, Date date) throws MissingScopesException, TokenExpiredException {

        try {
            Log.d("Debug","Entering sleep service");
            Scope[] requiredScopes = new Scope[1];
            requiredScopes[0] = Scope.sleep;
            String dateStr = dateFormat.format(date);
            APIUtils.validateToken(activityContext, AuthenticationManager.getCurrentAccessToken(), requiredScopes);

            AccessToken accessToken = AuthenticationManager.getCurrentAccessToken();
            String bearer="";
            if(accessToken==null || accessToken.hasExpired())
            {
                Log.d("Debug","access token error");
                bearer = "foo";
            }
            else
            {
                bearer = accessToken.getAccessToken();
            }
            final String authorization = String.format("Bearer %s", bearer);

            Log.d("Debug","After validating token "+authorization);

            RequestQueue queue = Volley.newRequestQueue(activityContext);
            String url = SLEEP_URL+dateStr+".json";
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Debug", response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Debug","error => "+error.toString());
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", authorization);
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            Log.d("Debug", e.toString());
        }
    }
}