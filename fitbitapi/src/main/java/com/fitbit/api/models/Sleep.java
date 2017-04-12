
package com.fitbit.api.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sleep implements Serializable
{

    @SerializedName("dateOfSleep")
    @Expose
    private String dateOfSleep;
    @SerializedName("duration")
    @Expose
    private Long duration;
    @SerializedName("efficiency")
    @Expose
    private Double efficiency;
    @SerializedName("isMainSleep")
    @Expose
    private Boolean isMainSleep;
    @SerializedName("levels")
    @Expose
    private Levels levels;
    @SerializedName("logId")
    @Expose
    private Long logId;
    @SerializedName("minutesAfterWakeup")
    @Expose
    private Long minutesAfterWakeup;
    @SerializedName("minutesAsleep")
    @Expose
    private Long minutesAsleep;
    @SerializedName("minutesAwake")
    @Expose
    private Long minutesAwake;
    @SerializedName("minutesToFallAsleep")
    @Expose
    private Long minutesToFallAsleep;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("timeInBed")
    @Expose
    private Long timeInBed;
    @SerializedName("type")
    @Expose
    private String type;
    private final static long serialVersionUID = 4741728808334660631L;

    public String getDateOfSleep() {
        return dateOfSleep;
    }

    public void setDateOfSleep(String dateOfSleep) {
        this.dateOfSleep = dateOfSleep;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Double efficiency) {
        this.efficiency = efficiency;
    }

    public Boolean getIsMainSleep() {
        return isMainSleep;
    }

    public void setIsMainSleep(Boolean isMainSleep) {
        this.isMainSleep = isMainSleep;
    }

    public Levels getLevels() {
        return levels;
    }

    public void setLevels(Levels levels) {
        this.levels = levels;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getMinutesAfterWakeup() {
        return minutesAfterWakeup;
    }

    public void setMinutesAfterWakeup(Long minutesAfterWakeup) {
        this.minutesAfterWakeup = minutesAfterWakeup;
    }

    public Long getMinutesAsleep() {
        return minutesAsleep;
    }

    public void setMinutesAsleep(Long minutesAsleep) {
        this.minutesAsleep = minutesAsleep;
    }

    public Long getMinutesAwake() {
        return minutesAwake;
    }

    public void setMinutesAwake(Long minutesAwake) {
        this.minutesAwake = minutesAwake;
    }

    public Long getMinutesToFallAsleep() {
        return minutesToFallAsleep;
    }

    public void setMinutesToFallAsleep(Long minutesToFallAsleep) {
        this.minutesToFallAsleep = minutesToFallAsleep;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Long getTimeInBed() {
        return timeInBed;
    }

    public void setTimeInBed(Long timeInBed) {
        this.timeInBed = timeInBed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
