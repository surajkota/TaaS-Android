
package com.fitbit.api.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Levels implements Serializable
{

    @SerializedName("summary")
    @Expose
    private Summary summary;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("shortData")
    @Expose
    private List<ShortDatum> shortData = null;
    private final static long serialVersionUID = 6745799739338604567L;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public List<ShortDatum> getShortData() {
        return shortData;
    }

    public void setShortData(List<ShortDatum> shortData) {
        this.shortData = shortData;
    }

}
