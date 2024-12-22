package com.example.carpoolingworkshop;

import java.util.ArrayList;

public class VehicleModel {
    private int id;
    private String manufacturer;
    private String model;
    private String color;
    private String license_plate;
    private int user_id; // FK to UserModel

    public VehicleModel(int id, String manufacturer, String model, String color, String license_plate, int user_id) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.color = color;
        this.license_plate = license_plate;
        this.user_id = user_id;
    }

    public VehicleModel(String manufacturer, String model, String color, String license_plate, int user_id) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.color = color;
        this.license_plate = license_plate;
        this.user_id = user_id;
    }

    public VehicleModel() {
    }

    @Override
    public String toString() {
        return manufacturer + ", " + model + ", " + color;
    }

    public ArrayList<String> checkErrors(){
        ArrayList<String> errors = new ArrayList<String>();
        if(user_id < 0){
            errors.add("User Id is required.");
        }
        if(manufacturer.isBlank()){
            errors.add("Manufacturer is required.");
        }
        if(model.isBlank()){
            errors.add("Model is required.");
        }
        if(color.isBlank()){
            errors.add("Color is required.");
        }
        if(license_plate.isBlank()){
            errors.add("License Plate is required.");
        }

        return errors;
    }


    // Getters and Setters -------------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String get_license_plate() {
        return license_plate;
    }

    public void set_license_plate(String license_plate) {
        this.license_plate = license_plate;
    }
}
