package com.example.engineeringaipractical.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static synchronized ApiInterface getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(" https://hn.algolia.com/api/v1/").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(ApiInterface.class);
    }

}
