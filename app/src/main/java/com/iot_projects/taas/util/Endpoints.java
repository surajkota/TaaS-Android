package com.iot_projects.taas.util;

import com.iot_projects.taas.models.Treatment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Kartikk on 4/10/2017.
 */

public interface Endpoints {

    @GET("/treatment")
    Call<List<Treatment>> getTreatmentList();
}
