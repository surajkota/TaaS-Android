package com.iot_projects.taas.util;

import com.fasterxml.jackson.databind.ObjectMapper;

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
}
