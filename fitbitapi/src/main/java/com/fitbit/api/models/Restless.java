
package com.fitbit.api.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restless implements Serializable
{

    @SerializedName("count")
    @Expose
    private Long count;
    @SerializedName("minutes")
    @Expose
    private Long minutes;
    private final static long serialVersionUID = -2315059418334994620L;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getMinutes() {
        return minutes;
    }

    public void setMinutes(Long minutes) {
        this.minutes = minutes;
    }

}
