
package com.iot_projects.taas.models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "foodId",
    "startDay",
    "endDay"
})
public class RestrictedFood {

    @JsonProperty("foodId")
    private String foodId;
    @JsonProperty("startDay")
    private Integer startDay;
    @JsonProperty("endDay")
    private Integer endDay;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("foodId")
    public String getFoodId() {
        return foodId;
    }

    @JsonProperty("foodId")
    public void setFoodId(String foodId) {
        this.foodId = foodId;
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

}
