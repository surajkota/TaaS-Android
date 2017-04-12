
package com.fitbit.api.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Summary implements Serializable
{

    @SerializedName("deep")
    @Expose
    private Deep deep;
    @SerializedName("light")
    @Expose
    private Light light;
    @SerializedName("rem")
    @Expose
    private Rem rem;
    @SerializedName("wake")
    @Expose
    private Wake wake;
    @SerializedName("asleep")
    @Expose
    private Asleep asleep;
    @SerializedName("awake")
    @Expose
    private Awake awake;
    @SerializedName("restless")
    @Expose
    private Restless restless;
    private final static long serialVersionUID = 5592575431827912337L;

    public Deep getDeep() {
        return deep;
    }

    public void setDeep(Deep deep) {
        this.deep = deep;
    }

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public Rem getRem() {
        return rem;
    }

    public void setRem(Rem rem) {
        this.rem = rem;
    }

    public Wake getWake() {
        return wake;
    }

    public void setWake(Wake wake) {
        this.wake = wake;
    }

    public Asleep getAsleep() {
        return asleep;
    }

    public void setAsleep(Asleep asleep) {
        this.asleep = asleep;
    }

    public Awake getAwake() {
        return awake;
    }

    public void setAwake(Awake awake) {
        this.awake = awake;
    }

    public Restless getRestless() {
        return restless;
    }

    public void setRestless(Restless restless) {
        this.restless = restless;
    }

}
