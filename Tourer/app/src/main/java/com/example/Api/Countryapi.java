package com.example.Api;

import com.example.Model.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Countryapi {
    @GET("name/{name}?fullText=true")
    Call<List<Country>> getCountryByName(@Path("name") String name);
}
