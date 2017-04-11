package com.iot_projects.taas.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by adhit on 4/10/2017.
 */

public class Interval {
    @JsonProperty("startTime")
    public long startTime;
    @JsonProperty("endTime")
    public long endTime;

    public Interval(long s, long e)
    {
        startTime = s;
        endTime = e;
    }
    @JsonProperty("startTime")
    public long getStartTime() {
        return startTime;
    }

    @JsonProperty("startTime")
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("endTime")
    public long getEndTime() {
        return endTime;
    }

    @JsonProperty("endTime")
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Interval{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}