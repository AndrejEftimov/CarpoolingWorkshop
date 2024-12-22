package com.example.carpoolingworkshop;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;

public class GeocodingHelper {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/";
    private GeocodingApiService apiService;
    private String apiKey;

    // Constructor that takes API key
    public GeocodingHelper(String apiKey) {
        this.apiKey = apiKey;

        // Create OkHttpClient for logging (optional but helpful for debugging)
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create API service
        apiService = retrofit.create(GeocodingApiService.class);
    }

    public void getAddress(double latitude, double longitude, AddressCallback callback) {
        String latLng = latitude + "," + longitude;

        Call<GeocodeResponse> call = apiService.getAddress(latLng, apiKey);
        call.enqueue(new Callback<GeocodeResponse>() {
            @Override
            public void onResponse(Call<GeocodeResponse> call, Response<GeocodeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String address = response.body().getFormattedAddress();
                    callback.onAddressFound(address);
                } else {
                    callback.onError("Could not retrieve address");
                }
            }

            @Override
            public void onFailure(Call<GeocodeResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Callback interface
    public interface AddressCallback {
        void onAddressFound(String address);
        void onError(String errorMessage);
    }
}