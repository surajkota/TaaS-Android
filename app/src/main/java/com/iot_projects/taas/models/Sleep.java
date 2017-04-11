
package com.iot_projects.taas.models;

import java.io.Serializable;
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
    "minThreshold",
    "maxThreshold"
})
public class Sleep implements Serializable {

    @JsonProperty("minThreshold")
    public Integer minThreshold;
    @JsonProperty("maxThreshold")
    public Integer maxThreshold;
    @JsonIgnore
    public Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("minThreshold")
    public Integer getMinThreshold() {
        return minThreshold;
    }

    @JsonProperty("minThreshold")
    public void setMinThreshold(Integer minThreshold) {
        this.minThreshold = minThreshold;
    }

    @JsonProperty("maxThreshold")
    public Integer getMaxThreshold() {
        return maxThreshold;
    }

    @JsonProperty("maxThreshold")
    public void setMaxThreshold(Integer maxThreshold) {
        this.maxThreshold = maxThreshold;
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
        return "Sleep{" +
                "minThreshold=" + minThreshold +
                ", maxThreshold=" + maxThreshold +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
