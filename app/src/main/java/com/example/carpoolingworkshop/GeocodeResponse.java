package com.example.carpoolingworkshop;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeocodeResponse {
    @SerializedName("results")
    private List<Result> results;

    @SerializedName("status")
    private String status;

    public String getFormattedAddress() {
        if (results != null && !results.isEmpty()) {
            return results.get(0).getFormattedAddress();
        }
        return null;
    }

    public static class Result {
        @SerializedName("formatted_address")
        private String formattedAddress;

        @SerializedName("address_components")
        private List<AddressComponent> addressComponents;

        public String getFormattedAddress() {
            return formattedAddress;
        }
    }

    public static class AddressComponent {
        @SerializedName("long_name")
        private String longName;

        @SerializedName("short_name")
        private String shortName;

        @SerializedName("types")
        private List<String> types;
    }
}