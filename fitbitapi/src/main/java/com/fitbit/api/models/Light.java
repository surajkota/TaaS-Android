
package com.fitbit.api.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Light implements Serializable
{

    @SerializedName("count")
    @Expose
    private Long count;
    @SerializedName("minutes")
    @Expose
    private Double minutes;
    @SerializedName("thirtyDayAvgMinutes")
    @Expose
    private Double thirtyDayAvgMinutes;
    private final static long serialVersionUID = -3977863603845143903L;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getMinutes() {
        return minutes;
    }

    public void setMinutes(Double minutes) {
        this.minutes = minutes;
    }

    public Double getThirtyDayAvgMinutes() {
        return thirtyDayAvgMinutes;
    }

    public void setThirtyDayAvgMinutes(Double thirtyDayAvgMinutes) {
        this.thirtyDayAvgMinutes = thirtyDayAvgMinutes;
    }

}
