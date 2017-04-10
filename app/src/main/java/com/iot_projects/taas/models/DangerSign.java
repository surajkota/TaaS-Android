
package com.iot_projects.taas.models;

import android.os.Parcelable;

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
    "symptom",
    "whenToAlert"
})
public class DangerSign implements Serializable {

    @JsonProperty("symptom")
    public String symptom;
    @JsonProperty("whenToAlert")
    public List<Integer> whenToAlert = null;
    @JsonIgnore
    public Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("symptom")
    public String getSymptom() {
        return symptom;
    }

    @JsonProperty("symptom")
    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    @JsonProperty("whenToAlert")
    public List<Integer> getWhenToAlert() {
        return whenToAlert;
    }

    @JsonProperty("whenToAlert")
    public void setWhenToAlert(List<Integer> whenToAlert) {
        this.whenToAlert = whenToAlert;
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
