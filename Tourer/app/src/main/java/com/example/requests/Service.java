package com.example.requests;

import com.example.Api.Countryapi;
import com.example.Api.Credentials;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Service {

    private static Service service;
    private static Retrofit retrofit;

    private Service() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Credentials.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized Service getInstance() {
        if (service == null)
            service = new Service();
        return service;
    }

    public Countryapi getCountryApi() {
        return retrofit.create(Countryapi.class);
    }
}
