package com.example.carpoolingworkshop;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class LocationHelper {

    private static final String GEOCODING_API_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String API_KEY = "YOUR_API_KEY_HERE";

    public static void getAddressFromLocation(double latitude, double longitude, OnAddressFetchedListener listener) {
        // Build the URL for the API request
        String url = GEOCODING_API_BASE_URL + "?latlng=" + latitude + "," + longitude + "&key=" + API_KEY;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                listener.onError("Failed to fetch address: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                        JSONArray results = json.getJSONArray("results");

                        if (results.length() > 0) {
                            // Extract the formatted address from the first result
                            String address = results.getJSONObject(0).getString("formatted_address");
                            listener.onAddressFetched(address);
                        } else {
                            listener.onError("No address found for the provided coordinates.");
                        }
                    } catch (Exception e) {
                        listener.onError("Error parsing response: " + e.getMessage());
                    }
                } else {
                    listener.onError("API call unsuccessful: " + response.message());
                }
            }
        });
    }

    // Callback interface for delivering the address
    public interface OnAddressFetchedListener {
        void onAddressFetched(String address);
        void onError(String errorMessage);
    }
}
