package com.example.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("name")
    @Expose
    private CountryName name;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("subregion")
    @Expose
    private String subregion;

    public CountryName getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public static class CountryName{
       @SerializedName("common")
       private String commonName;

       public String getCommonName() {
           return commonName;
       }
   }
}
