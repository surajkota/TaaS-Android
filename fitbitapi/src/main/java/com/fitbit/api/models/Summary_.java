
package com.fitbit.api.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Summary_ implements Serializable
{

    @SerializedName("totalMinutesAsleep")
    @Expose
    private Long totalMinutesAsleep;
    @SerializedName("totalSleepRecords")
    @Expose
    private Long totalSleepRecords;
    @SerializedName("totalTimeInBed")
    @Expose
    private Long totalTimeInBed;
    private final static long serialVersionUID = 3910041099267290575L;

    public Long getTotalMinutesAsleep() {
        return totalMinutesAsleep;
    }

    public void setTotalMinutesAsleep(Long totalMinutesAsleep) {
        this.totalMinutesAsleep = totalMinutesAsleep;
    }

    public Long getTotalSleepRecords() {
        return totalSleepRecords;
    }

    public void setTotalSleepRecords(Long totalSleepRecords) {
        this.totalSleepRecords = totalSleepRecords;
    }

    public Long getTotalTimeInBed() {
        return totalTimeInBed;
    }

    public void setTotalTimeInBed(Long totalTimeInBed) {
        this.totalTimeInBed = totalTimeInBed;
    }

}
