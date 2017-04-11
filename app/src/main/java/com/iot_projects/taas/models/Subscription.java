package com.iot_projects.taas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adhit on 4/7/2017.
 */

public class Subscription {
    @JsonProperty("treatment")
    private Treatment treatment = new Treatment();
    @JsonProperty("medicineNotTakenMap")
    private Map<String, Map<Long,Boolean>> medicineNotTakenMap = new HashMap<>();
    @JsonProperty("improperTimeMedicine")
    private Map<String, Integer> improperTimeMedicine = new HashMap<>();
    @JsonProperty("skippedMedicine")
    private Map<String, Integer> skippedMedicine = new HashMap<>();

    /*@JsonProperty("foodNearBy")
    private Map<String, Interval> foodNearBy = new HashMap<>();

    @JsonProperty("foodNearBy")
    public Map<String, Interval> getFoodNearBy() {
        return foodNearBy;
    }

    @JsonProperty("foodNearBy")
    public void setFoodNearBy(Map<String, Interval> foodNearBy) {
        this.foodNearBy = foodNearBy;
    }*/

    @JsonProperty("improperTimeMedicine")
    public Map<String, Integer> getImproperTimeMedicine() {
        return improperTimeMedicine;
    }

    @JsonProperty("improperTimeMedicine")
    public void setImproperTimeMedicine(Map<String, Integer> improperTimeMedicine) {
        this.improperTimeMedicine = improperTimeMedicine;
    }

    @JsonProperty("skippedMedicine")
    public Map<String, Integer> getSkippedMedicine() {
        return skippedMedicine;
    }

    @JsonProperty("skippedMedicine")
    public void setSkippedMedicine(Map<String, Integer> skippedMedicine) {
        this.skippedMedicine = skippedMedicine;
    }

    @JsonProperty("treatment")
    public void setTreatment(Treatment t) {
        treatment = t;
    }
    @JsonProperty("treatment")
    public Treatment getTreatment()
    {
        return treatment;
    }
    @JsonProperty("medicineNotTakenMap")
    public void setMedicineNotTakenMap(Map<String, Map<Long, Boolean>> m)
    {
        medicineNotTakenMap = m;
    }

    @JsonProperty("medicineNotTakenMap")
    public Map<String, Map<Long, Boolean>> getMedicineNotTakenMap()
    {
        return medicineNotTakenMap;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "treatment=" + treatment +
                ", medicineNotTakenMap=" + medicineNotTakenMap +
                ", improperTimeMedicine=" + improperTimeMedicine +
                ", skippedMedicine=" + skippedMedicine +
                //", foodNearBy=" + foodNearBy +
                '}';
    }
}
