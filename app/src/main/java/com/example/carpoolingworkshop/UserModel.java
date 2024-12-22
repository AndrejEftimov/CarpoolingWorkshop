package com.example.carpoolingworkshop;

import android.util.Log;

import java.util.ArrayList;

public class UserModel {
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String phone_number;
    private float rating;
    private int rating_count;
    private String origin_latitude;
    private String origin_longitude;
    private String dest_latitude;
    private String dest_longitude;
    private int active_vehicle_id;

    //Constructors START --------------------------------
    public UserModel(String first_name, String last_name, String email, String password, String phone_number, float rating, int rating_count, String origin_latitude, String origin_longitude, String dest_latitude, String dest_longitude, int active_vehicle_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.rating = rating;
        this.rating_count = rating_count;
        this.origin_latitude = origin_latitude;
        this.origin_longitude = origin_longitude;
        this.dest_latitude = dest_latitude;
        this.dest_longitude = dest_longitude;
        this.active_vehicle_id = active_vehicle_id;
    }

    public UserModel(int id, String first_name, String last_name, String email, String password, String phone_number, float rating, int rating_count, String origin_latitude, String origin_longitude, String dest_latitude, String dest_longitude, int active_vehicle_id) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.rating = rating;
        this.rating_count = rating_count;
        this.origin_latitude = origin_latitude;
        this.origin_longitude = origin_longitude;
        this.dest_latitude = dest_latitude;
        this.dest_longitude = dest_longitude;
        this.active_vehicle_id = active_vehicle_id;
    }

    public UserModel(int id, String first_name, String last_name, String email, String password, String phone_number, float rating) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.rating = rating;
        this.origin_latitude = origin_latitude;
        this.origin_longitude = origin_longitude;
        this.dest_latitude = dest_latitude;
        this.dest_longitude = dest_longitude;
    }

    public UserModel(int id, String first_name, String last_name, String email, String password, String phone_number, float rating, String origin_latitude, String origin_longitude, String dest_latitude, String dest_longitude) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.rating = rating;
        this.origin_latitude = origin_latitude;
        this.origin_longitude = origin_longitude;
        this.dest_latitude = dest_latitude;
        this.dest_longitude = dest_longitude;
    }



    public UserModel(String first_name, String last_name, String email, String password, String phone_number, float rating, int rating_count, int active_vehicle_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.rating = rating;
        this.rating_count = rating_count;
        this.active_vehicle_id = active_vehicle_id;
    }

    public UserModel() {
    }
    //Constructors END --------------------------------

    @Override
    public String toString() {
        return "User: " + id + " | " + first_name + " " + last_name + ", "  + email;
    }

    public ArrayList<String> checkErrors(){
        ArrayList<String> errors = new ArrayList<String>();
        if(first_name.isBlank()){
            errors.add("First Name is required.");
        }
        if(last_name.isBlank()){
            errors.add("Last Name is required.");
        }
        if(email.isBlank()){
            errors.add("Email is required.");
        }
        if(password.isBlank()){
            errors.add("Password is required.");
        }
        if(phone_number.isBlank()){
            errors.add("Phone Number is required.");
        }

        return errors;
    }

    //Getters and Setters START --------------------------------
    public String get_fullname(){
        return first_name + " " + last_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_first_name() {
        return first_name;
    }

    public void set_first_name(String first_name) {
        this.first_name = first_name;
    }

    public String get_last_name() {
        return last_name;
    }

    public void set_last_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getOrigin_latitude() {
        return origin_latitude;
    }

    public void setOrigin_latitude(String origin_latitude) {
        this.origin_latitude = origin_latitude;
    }

    public String getOrigin_longitude() {
        return origin_longitude;
    }

    public void setOrigin_longitude(String origin_longitude) {
        this.origin_longitude = origin_longitude;
    }

    public String getDest_latitude() {
        return dest_latitude;
    }

    public void setDest_latitude(String dest_latitude) {
        this.dest_latitude = dest_latitude;
    }

    public String getDest_longitude() {
        return dest_longitude;
    }

    public void setDest_longitude(String dest_longitude) {
        this.dest_longitude = dest_longitude;
    }

    public int getActive_vehicle() {
        Log.d("HERE is UserModel", "active_vehicle_id = " + active_vehicle_id);
        return active_vehicle_id;
    }

    public void setActive_vehicle(int active_vehicle_id) {
        this.active_vehicle_id = active_vehicle_id;
    }

    //Getters and Setters END --------------------------------
}
