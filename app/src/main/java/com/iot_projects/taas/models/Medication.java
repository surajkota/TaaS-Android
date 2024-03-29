
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

public class Medication implements Serializable {

    @JsonProperty("medicineName")
    public String medicineName;
    @JsonProperty("medicineId")
    public String medicineId;
    @JsonProperty("timeThreshold")
    public List<String> timeThreshold = null;
    @JsonProperty("quantity")
    public Integer quantity;
    @JsonProperty("procedure")
    public String procedure;
    @JsonProperty("frequency")
    public Integer frequency;
    @JsonProperty("startDay")
    public Integer startDay;
    @JsonProperty("endDay")
    public Integer endDay;
    @JsonIgnore
    public Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("medicineName")
    public String getMedicineName() { return medicineName; }
    @JsonProperty("medicineName")
    public void setMedicineName(String name) { this.medicineName = name; }
    @JsonProperty("medicineId")
    public String getMedicineId() {
        return medicineId;
    }

    @JsonProperty("medicineId")
    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    @JsonProperty("timeThreshold")
    public List<String> getTimeThreshold() {
        return timeThreshold;
    }

    @JsonProperty("timeThreshold")
    public void setTimeThreshold(List<String> timeThreshold) {
        this.timeThreshold = timeThreshold;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("procedure")
    public String getProcedure() {
        return procedure;
    }

    @JsonProperty("procedure")
    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    @JsonProperty("frequency")
    public Integer getFrequency() {
        return frequency;
    }

    @JsonProperty("frequency")
    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @JsonProperty("startDay")
    public Integer getStartDay() {
        return startDay;
    }

    @JsonProperty("startDay")
    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }

    @JsonProperty("endDay")
    public Integer getEndDay() {
        return endDay;
    }

    @JsonProperty("endDay")
    public void setEndDay(Integer endDay) {
        this.endDay = endDay;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "Medication{" +
                "medicineName='" + medicineName + '\'' +
                ", medicineId='" + medicineId + '\'' +
                ", timeThreshold=" + timeThreshold +
                ", quantity=" + quantity +
                ", procedure='" + procedure + '\'' +
                ", frequency=" + frequency +
                ", startDay=" + startDay +
                ", endDay=" + endDay +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
