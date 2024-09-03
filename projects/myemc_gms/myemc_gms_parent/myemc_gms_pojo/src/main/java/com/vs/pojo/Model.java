package com.vs.pojo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model implements Serializable {
    // test model
    @JsonProperty("Name")
    private String Name;
    @JsonProperty("Id")
    private String Id;
    @JsonProperty("Owner")
    private String Owner;
    @JsonProperty("Ts")
    private String Ts;
    @JsonProperty("Labels")
    private String Labels;
    @JsonProperty("Properties")
    private String Properties;
    @JsonProperty("Children")
    private String Children;
    @JsonProperty("Stock")
    private String Stock;

    // practical model
/*    @JsonProperty("Name")
    private String Name;
    @JsonProperty("Id")
    private String Id;
    @JsonProperty("Owner")
    private String Owner;
    @JsonProperty("Ts")
    private String Ts;
    @JsonProperty("Labels")
    private List<String> Labels;
    @JsonProperty("Properties")
    private List<Map<String, Object>> Properties;
    @JsonProperty("Children")
    private List<Map<String, String>> Children;*/
}
