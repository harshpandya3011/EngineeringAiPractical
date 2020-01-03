package com.example.engineeringaipractical.rest;

import com.example.engineeringaipractical.entities.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("search_by_date?tags=story&=1")
    Call<Example> getAlbums(@Query("tags") String tags, @Query("page") int limit);

}
