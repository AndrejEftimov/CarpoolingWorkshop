package com.example.carpoolingworkshop;

public class OfferViewModel {
    public String first_name;
    public String last_name;
    public String origin; // Address (not coordinates)
    public String dest; // Address (not coordinates)
    public String vehicle;
    public float rating;

    public String full_name(){
        return first_name + " " + last_name;
    }
}
