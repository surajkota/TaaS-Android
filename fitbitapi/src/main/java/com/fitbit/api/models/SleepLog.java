
package com.fitbit.api.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SleepLog implements Serializable
{

    @SerializedName("sleep")
    @Expose
    private List<Sleep> sleep = null;
    @SerializedName("summary")
    @Expose
    private Summary_ summary;
    private final static long serialVersionUID = 2673418019958798932L;

    public List<Sleep> getSleep() {
        return sleep;
    }

    public void setSleep(List<Sleep> sleep) {
        this.sleep = sleep;
    }

    public Summary_ getSummary() {
        return summary;
    }

    public void setSummary(Summary_ summary) {
        this.summary = summary;
    }

}
