package com.example.carpoolingworkshop;

public class DriveModel {
    private int id;
    private int driver_id; // FK to UserModel
    private int passenger_id; // FK to UserModel
    private String origin_latitude;
    private String origin_longitude;
    private String dest_latitude;
    private String dest_longitude;
    private int price;
    private boolean accepted;
    private boolean completed;
    private String date_time;

    public DriveModel(int id, int driver_id, int passenger_id, String origin_latitude, String origin_longitude, String dest_latitude, String dest_longitude, int price, boolean accepted, boolean completed, String date_time) {
        this.id = id;
        this.driver_id = driver_id;
        this.passenger_id = passenger_id;
        this.origin_latitude = origin_latitude;
        this.origin_longitude = origin_longitude;
        this.dest_latitude = dest_latitude;
        this.dest_longitude = dest_longitude;
        this.price = price;
        this.accepted = accepted;
        this.completed = completed;
        this.date_time = date_time;
    }

    public DriveModel(int driver_id, int passenger_id, String origin_latitude, String origin_longitude, String dest_latitude, String dest_longitude, int price, boolean accepted, boolean completed, String date_time) {
        this.driver_id = driver_id;
        this.passenger_id = passenger_id;
        this.origin_latitude = origin_latitude;
        this.origin_longitude = origin_longitude;
        this.dest_latitude = dest_latitude;
        this.dest_longitude = dest_longitude;
        this.price = price;
        this.accepted = accepted;
        this.completed = completed;
        this.date_time = date_time;
    }

    public DriveModel(int driver_id, String origin_latitude, String origin_longitude, String dest_latitude, String dest_longitude) {
        this.driver_id = driver_id;
        this.origin_latitude = origin_latitude;
        this.origin_longitude = origin_longitude;
        this.dest_latitude = dest_latitude;
        this.dest_longitude = dest_longitude;
    }

    public DriveModel() {
    }

    @Override
    public String toString() {
        return "DriveModel{" +
                "id=" + id +
                ", driver_id=" + driver_id +
                ", passenger_id=" + passenger_id +
                ", accepted=" + accepted +
                ", date_time=" + date_time +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public int getPassenger_id() {
        return passenger_id;
    }

    public void setPassenger_id(int passenger_id) {
        this.passenger_id = passenger_id;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
