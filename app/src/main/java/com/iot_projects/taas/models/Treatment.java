
package com.iot_projects.taas.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "treatmentName",
        "duration",
        "medication",
        "restrictedFood",
        "dangerSigns",
        "sleep"
})
public class Treatment implements Serializable {

    @JsonProperty("duration")
    public Integer duration;
    @JsonProperty("medication")
    public List<Medication> medication = null;
    @JsonProperty("restrictedFood")
    public List<RestrictedFood> restrictedFood = null;
    @JsonProperty("dangerSigns")
    public List<DangerSign> dangerSigns = null;
    @JsonProperty("sleep")
    public Sleep sleep;
    @JsonProperty("treatmentName")
    public String treatmentName;

    @JsonProperty("treatmentName")
    public String getTreatmentName() {
        return treatmentName;
    }

    @JsonProperty("treatmentName")
    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @JsonProperty("medication")
    public List<Medication> getMedication() {
        return medication;
    }

    @JsonProperty("medication")
    public void setMedication(List<Medication> medication) {
        this.medication = medication;
    }

    @JsonProperty("restrictedFood")
    public List<RestrictedFood> getRestrictedFood() {
        return restrictedFood;
    }

    @JsonProperty("restrictedFood")
    public void setRestrictedFood(List<RestrictedFood> restrictedFood) {
        this.restrictedFood = restrictedFood;
    }

    @JsonProperty("dangerSigns")
    public List<DangerSign> getDangerSigns() {
        return dangerSigns;
    }

    @JsonProperty("dangerSigns")
    public void setDangerSigns(List<DangerSign> dangerSigns) {
        this.dangerSigns = dangerSigns;
    }

    @JsonProperty("sleep")
    public Sleep getSleep() {
        return sleep;
    }

    @JsonProperty("sleep")
    public void setSleep(Sleep sleep) {
        this.sleep = sleep;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
