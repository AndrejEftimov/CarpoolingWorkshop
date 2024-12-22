package com.example.carpoolingworkshop;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingApiService {
    @GET("json")
    Call<GeocodeResponse> getAddress(
            @Query("latlng") String latLng,
            @Query("key") String apiKey
    );
}