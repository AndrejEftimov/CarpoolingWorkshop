package com.example.carpoolingworkshop;

public class RatingModel {
    private int rated_user_id; // FK to UserModel
    private int rater_user_id; // FK to UserModel
    private int rating;

    public RatingModel(int rated_user_id, int rater_user_id, int rating) {
        this.rated_user_id = rated_user_id;
        this.rater_user_id = rater_user_id;
        this.rating = rating;
    }

    public RatingModel() {
    }

    public int getRated_user_id() {
        return rated_user_id;
    }

    public void setRated_user_id(int rated_user_id) {
        this.rated_user_id = rated_user_id;
    }

    public int getRater_user_id() {
        return rater_user_id;
    }

    public void setRater_user_id(int rater_user_id) {
        this.rater_user_id = rater_user_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
