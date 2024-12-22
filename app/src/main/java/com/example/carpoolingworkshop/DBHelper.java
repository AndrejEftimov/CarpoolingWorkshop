package com.example.carpoolingworkshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    // SHARED PROPERTIES for User, Vehicle & Drive
    private static final String COLUMN_ID = "id";

    // SHARED PROPERTIES for User and Drive
    private static final String COLUMN_ORIGIN_LAT = "origin_latitude";
    private static final String COLUMN_ORIGIN_LNG = "origin_longitude";
    private static final String COLUMN_DEST_LAT = "dest_latitude";
    private static final String COLUMN_DEST_LNG = "dest_longitude";

    // SHARED PROPERTIES for User & Rating
    private static final String COLUMN_RATING = "rating";

    // For UserModel ///////////////////////////////
    private static final String USER_TABLE = "User";
    private static final String COLUMN_FIRSTNAME = "first_name";
    private static final String COLUMN_LASTNAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_ACTIVE_VEHICLE_ID = "active_vehicle_id";
    private static final String COLUMN_RATING_COUNT = "rating_count";

    // For VehicleModel ///////////////////////////////
    private static final String VEHICLE_TABLE = "Vehicle";
    private static final String COLUMN_MANUFACTURER = "manufacturer";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_LICENSE_PLATE = "license_plate";
    private static final String COLUMN_USER_ID = "user_id";

    // For DriveModel ///////////////////////////////
    private static final String DRIVE_TABLE = "Drive";
    private static final String COLUMN_DRIVER_ID = "driver_id";
    private static final String COLUMN_PASSENGER_ID = "passenger_id";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_ACCEPTED = "accepted";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String COLUMN_DATE_TIME = "date_time";

    // For RatingModel ///////////////////////////////
    private static final String RATING_TABLE = "Rating";
    private static final String COLUMN_RATED_USER_ID = "rated_user_id";
    private static final String COLUMN_RATER_USER_ID = "rater_user_id";

    public DBHelper(@Nullable Context context) {
        super(context, "Carpooling.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = null;
        sql = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIRSTNAME + " TEXT, " + COLUMN_LASTNAME + " TEXT, " + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, " + COLUMN_PHONE_NUMBER + " TEXT, "
                + COLUMN_RATING + " REAL, " + COLUMN_RATING_COUNT + " INTEGER, "
                + COLUMN_ORIGIN_LAT + " TEXT, " + COLUMN_ORIGIN_LNG + " TEXT, "
                + COLUMN_DEST_LAT + " TEXT, " + COLUMN_DEST_LNG + " TEXT, "
                + COLUMN_ACTIVE_VEHICLE_ID + " INTEGER, "
                + "FOREIGN KEY" + "(" + COLUMN_ACTIVE_VEHICLE_ID + ")" + " REFERENCES " + VEHICLE_TABLE + "(" + COLUMN_ID + ")"
                + ");";

        db.execSQL(sql);

        sql = "CREATE TABLE " + VEHICLE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MANUFACTURER + " TEXT, " + COLUMN_MODEL + " TEXT, "
                + COLUMN_COLOR + " TEXT, " + COLUMN_LICENSE_PLATE + " TEXT, "
                + COLUMN_USER_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY" + "(" + COLUMN_USER_ID + ")" + " REFERENCES " + USER_TABLE + "(" + COLUMN_ID +")"
                + ");";

        db.execSQL(sql);

        sql = "CREATE TABLE " + DRIVE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DRIVER_ID + " INTEGER, " + COLUMN_PASSENGER_ID + " INTEGER, "
                + COLUMN_ORIGIN_LAT + " TEXT, " + COLUMN_ORIGIN_LNG + " TEXT, "
                + COLUMN_DEST_LAT + " TEXT, " + COLUMN_DEST_LNG + " TEXT, "
                + COLUMN_PRICE + " INTEGER, "
                + COLUMN_ACCEPTED + " INTEGER, " + COLUMN_COMPLETED + " INTEGER, " // 1 for true, 0 for false
                + COLUMN_DATE_TIME + " TEXT, "
                + "FOREIGN KEY" + "(" + COLUMN_DRIVER_ID + ")" + " REFERENCES " + USER_TABLE + "(" + COLUMN_ID +"), "
                + "FOREIGN KEY" + "(" + COLUMN_PASSENGER_ID + ")" + " REFERENCES " + USER_TABLE + "(" + COLUMN_ID +")"
                + ");";

        db.execSQL(sql);

        sql = "CREATE TABLE " + RATING_TABLE + " (" + COLUMN_RATED_USER_ID + " INTEGER, " + COLUMN_RATER_USER_ID + " INTEGER, "
                + COLUMN_RATING + " INTEGER, "
                + "FOREIGN KEY" + "(" + COLUMN_RATED_USER_ID + ")" + " REFERENCES " + USER_TABLE + "(" + COLUMN_ID +"), "
                + "FOREIGN KEY" + "(" + COLUMN_RATER_USER_ID + ")" + " REFERENCES " + USER_TABLE + "(" + COLUMN_ID +"), "
                + "PRIMARY KEY" + "(" + COLUMN_RATED_USER_ID + ", " + COLUMN_RATER_USER_ID + ")"
                + ");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + VEHICLE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DRIVE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RATING_TABLE);

    }

    // CREATE
    // returns id of user inserted
    public long createUser(UserModel user){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("IN createUser --->", "origin_lat: " + user.getOrigin_latitude());

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FIRSTNAME, user.get_first_name());
        cv.put(COLUMN_LASTNAME, user.get_last_name());
        cv.put(COLUMN_EMAIL, user.getEmail());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_PHONE_NUMBER, user.getPhone_number());
        cv.put(COLUMN_RATING, 0);
        cv.put(COLUMN_RATING_COUNT, 0);
        cv.put(COLUMN_ACTIVE_VEHICLE_ID, -1);

        return db.insert(USER_TABLE, null, cv);
    }

    public boolean createVehicle(VehicleModel v){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_ID, v.getUser_id());
        cv.put(COLUMN_MANUFACTURER, v.getManufacturer());
        cv.put(COLUMN_MODEL, v.getModel());
        cv.put(COLUMN_COLOR, v.getColor());
        cv.put(COLUMN_LICENSE_PLATE, v.get_license_plate());

        long result = db.insert(VEHICLE_TABLE, null, cv);
        if(result == -1){
            return false;
        }
        return true;
    }

    public boolean createDrive(int driver_id){
        SQLiteDatabase db = this.getWritableDatabase();

        UserModel driver = getUser(driver_id);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DRIVER_ID, driver_id);
        cv.put(COLUMN_PASSENGER_ID, -1);
        cv.put(COLUMN_PRICE, 0);
        cv.put(COLUMN_ORIGIN_LAT, driver.getOrigin_latitude());
        cv.put(COLUMN_ORIGIN_LNG, driver.getOrigin_longitude());
        cv.put(COLUMN_DEST_LAT, driver.getDest_latitude());
        cv.put(COLUMN_DEST_LNG, driver.getDest_longitude());
        cv.put(COLUMN_ACCEPTED, 0);
        cv.put(COLUMN_COMPLETED, 0);
        cv.put(COLUMN_DATE_TIME, "");

        long result = db.insert(DRIVE_TABLE, null, cv);
        if(result == -1){
            return false;
        }
        return true;
    }

    // READ / VIEW / GET
    public UserModel getUser(int userId){
        UserModel user = null;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_ID + " = " + userId;

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            user = new UserModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getFloat(6),
                    cursor.getInt(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10),
                    cursor.getString(11),
                    cursor.getInt(12)
            );
        }

        cursor.close();
        return user;
    }

    public DriveModel getDrive(int driverId){
        DriveModel drive = null;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DRIVE_TABLE + " WHERE " + COLUMN_DRIVER_ID + " = " + driverId +
                " AND " + COLUMN_COMPLETED + " = 0";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            drive = new DriveModel(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getInt(8) != 0,
                    cursor.getInt(9) != 0,
                    cursor.getString(10)
            );
        }

        cursor.close();
        return drive;
    }

    public DriveModel getDrive(int driverId, int passengerId){
        DriveModel drive = null;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DRIVE_TABLE + " WHERE " + COLUMN_DRIVER_ID + " = " + driverId +
                " AND " + COLUMN_PASSENGER_ID + " = " + passengerId;

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            drive = new DriveModel(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getInt(8) != 0,
                    cursor.getInt(9) != 0,
                    cursor.getString(10)
            );
        }

        cursor.close();
        return drive;
    }

    public List<UserModel> getAllUsers(){
        List<UserModel> list = new ArrayList<>();

        String sql = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        if(!cursor.moveToFirst()){
            cursor.close();
            return list;
        }

        UserModel user = null;
        do{
            int id = cursor.getInt(0);
            String firstname = cursor.getString(1);
            String lastname = cursor.getString(2);
            String email = cursor.getString(3);
            String password = cursor.getString(4);
            String phone_number = cursor.getString(5);
            float rating = cursor.getFloat(6);

            user = new UserModel(id, firstname, lastname, email, password, phone_number, rating);
            list.add(user);

        }while(cursor.moveToNext());

        cursor.close();
        return list;
    }

    public VehicleModel getVehicle(int vehicleId){
        Log.d("HERE in DBHelper", "vehicleId = " + vehicleId);
        VehicleModel v = null;
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + VEHICLE_TABLE + " WHERE " + COLUMN_ID + " = " + vehicleId;

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            Log.d("HERE in DBHelper", "Inside if statement...");
            v = new VehicleModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5)
            );
        }

        cursor.close();
        return v;
    }

    public List<VehicleModel> getAllVehicles(){
        List<VehicleModel> list = new ArrayList<>();

        String sql = "SELECT * FROM " + VEHICLE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        if(!cursor.moveToFirst()){
            cursor.close();
            return list;
        }

        VehicleModel v = null;
        do{
            int id = cursor.getInt(0);
            String manufacturer = cursor.getString(1);
            String model = cursor.getString(2);
            String color = cursor.getString(3);
            String license_plate = cursor.getString(4);
            int user_id = cursor.getInt(5);

            v = new VehicleModel(id, manufacturer, model, color, license_plate, user_id);
            list.add(v);

        }while(cursor.moveToNext());

        cursor.close();
        return list;
    }

    public List<VehicleModel> getVehiclesForUser(int userId){
        List<VehicleModel> list = new ArrayList<>();

        String sql = "SELECT * FROM " + VEHICLE_TABLE + " WHERE " + COLUMN_USER_ID + " = " + userId;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        if(!cursor.moveToFirst()){
            cursor.close();
            return list;
        }

        VehicleModel v = null;
        do{
            int id = cursor.getInt(0);
            String manufacturer = cursor.getString(1);
            String model = cursor.getString(2);
            String color = cursor.getString(3);
            String license_plate = cursor.getString(4);
            int user_id = cursor.getInt(5);

            v = new VehicleModel(id, manufacturer, model, color, license_plate, user_id);
            list.add(v);

        }while(cursor.moveToNext());

        cursor.close();
        return list;
    }

//    public List<UserModel> getAllUsersExceptLoggedIn(int loggedInUserId) {
//        List<UserModel> users = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String query = "SELECT * FROM " + USER_TABLE + " WHERE id != ?";
//        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(loggedInUserId)});
//
//        if (cursor.moveToFirst()) {
//            do {
//                UserModel user = new UserModel(
//                        cursor.getInt(0),
//                        cursor.getString(1),
//                        cursor.getString(2),
//                        cursor.getString(3),
//                        cursor.getString(4),
//                        cursor.getString(5),
//                        cursor.getFloat(6),
//                        cursor.getString(7),
//                        cursor.getString(8),
//                        cursor.getString(9),
//                        cursor.getString(10),
//                        cursor.getInt(11)
//                );
//                users.add(user);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        return users;
//    }


//    public List<UserModel> getUsersInRadius(int userId, int meters){
//        List<DriveModel> drives = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        UserModel loggedUser = getUser(userId);
//        double currentLat = Double.parseDouble(loggedUser.getOrigin_latitude());
//        double currentLong = Double.parseDouble(loggedUser.getOrigin_longitude());
//
//        // SQL query to find users within the given radius
//        String sql = "SELECT * FROM " + USER_TABLE +
//                " WHERE id != ?";
//
//        Cursor cursor = db.rawQuery(sql, new String[]{ String.valueOf(userId) });
//
//        // Loop through the results and populate the list
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                double otherLat = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORIGIN_LAT)));
//                double otherLong = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORIGIN_LNG)));
//
//                double distance = calculateHaversineDistance(currentLat, currentLong, otherLat, otherLong);
//
//                if (distance <= meters) {
//                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
//                    String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRSTNAME));
//                    String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LASTNAME));
//                    String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
//                    String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
//                    String phone_number = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER));
//                    float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING));
//                    String originLat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORIGIN_LAT));
//                    String originLong = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORIGIN_LNG));
//                    String destLat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEST_LAT));
//                    String destLong = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEST_LNG));
//                    int active_vehicle_id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTIVE_VEHICLE_ID));
//
//
//                    // Create UserModel object
//                    UserModel user = new UserModel();
//                    user.setId(id);
//                    user.set_first_name(firstName);
//                    user.set_last_name(lastName);
//                    user.setEmail(email);
//                    user.setPassword(password);
//                    user.setPhone_number(phone_number);
//                    user.setRating(rating);
//                    user.setOrigin_latitude(originLat);
//                    user.setOrigin_longitude(originLong);
//                    user.setDest_latitude(destLat);
//                    user.setDest_longitude(destLong);
//                    user.setActive_vehicle(active_vehicle_id);
//
//                    // Add to the list
//                    users.add(user);
//                }
//            }
//            cursor.close();
//        }
//
//        return users;
//    }

    public List<DriveModel> getDrivesInRadius(int loggedInUserId, double radiusInMeters) {
        List<DriveModel> drives = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        UserModel passenger = getUser(loggedInUserId);
        if(passenger.getOrigin_latitude() == null || passenger.getOrigin_longitude() == null){
            return drives;
        }
        double passenger_origin_lat = Double.parseDouble(passenger.getOrigin_latitude());
        double passenger_origin_lng = Double.parseDouble(passenger.getOrigin_longitude());

        // SQL query to find offers (drives) in the nearby area (that don't have a  passenger yet),
        // and which are not yet accepted (are pending)
        String sql = "SELECT * FROM " + DRIVE_TABLE +
                " WHERE " + COLUMN_DRIVER_ID + " != ?" + " AND " + COLUMN_DATE_TIME + " != ''";

        Cursor cursor = db.rawQuery(sql, new String[]{ String.valueOf(loggedInUserId) });

        // Loop through the results and populate the list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d("DBHelper DEBUG", "Cursor has elements!");
                UserModel driver = getUser(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DRIVER_ID)));

                double driver_origin_lat = Double.parseDouble(driver.getOrigin_latitude());
                double driver_origin_lng = Double.parseDouble(driver.getOrigin_longitude());

                double distance = calculateHaversineDistance(passenger_origin_lat, passenger_origin_lng, driver_origin_lat, driver_origin_lng);

                if (distance <= radiusInMeters) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String destLat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEST_LAT));
                    String destLong = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEST_LNG));
                    int price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                    int accepted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACCEPTED));
                    int completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED));
                    String date_time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME));


                    // Create UserModel object
                    DriveModel drive = new DriveModel();
                    drive.setId(id);
                    drive.setDriver_id(driver.getId());
                    drive.setPassenger_id(passenger.getId());
                    drive.setOrigin_latitude(driver.getOrigin_latitude());
                    drive.setOrigin_longitude(driver.getOrigin_longitude());
                    drive.setDest_latitude(driver.getDest_latitude());
                    drive.setDest_longitude(driver.getDest_longitude());
                    drive.setPrice(price);
                    drive.setAccepted(accepted != 0);
                    drive.setCompleted(completed != 0);
                    drive.setDate_time(date_time);

                    // Add to the list
                    drives.add(drive);
                    Log.d("DBHelper DEBUG", drive.toString());
                }
            }
            cursor.close();
        }

        return drives;
    }

    // radius is in meters
    public List<OfferViewModel> getOffers(int radius){
        List<OfferViewModel> offers = new ArrayList<OfferViewModel>();
        OfferViewModel offer = null;

        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + USER_TABLE + " INNER JOIN " + VEHICLE_TABLE + " ON "
                + USER_TABLE + "." + COLUMN_ACTIVE_VEHICLE_ID + " = " + VEHICLE_TABLE + "." + COLUMN_ID
                + ";";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int user_id = cursor.getInt(0);
                String manufacturer = cursor.getString(1);
                String model = cursor.getString(2);

                //offer = new OfferViewModel(id, );
                offers.add(offer);
            } while(cursor.moveToNext());
        }

        cursor.close();
        return offers;
    }

    public List<DriveModel> getRequestedDrives(int driverId){
        List<DriveModel> drives = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + DRIVE_TABLE +
                " WHERE " + COLUMN_DRIVER_ID + " = ?" + " AND " + COLUMN_DATE_TIME + " != ''"
                + " AND " + COLUMN_PASSENGER_ID + " >= 1";

        Cursor cursor = db.rawQuery(sql, new String[]{ String.valueOf(driverId) });

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d("DBHelper DEBUG", "Cursor has elements!");
                UserModel driver = getUser(driverId);

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                int passenger_id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PASSENGER_ID));
                String destLat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEST_LAT));
                String destLong = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEST_LNG));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
                int accepted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACCEPTED));
                int completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED));
                String date_time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME));


                // Create UserModel object
                DriveModel drive = new DriveModel();
                drive.setId(id);
                drive.setDriver_id(driver.getId());
                drive.setPassenger_id(passenger_id);
                drive.setOrigin_latitude(driver.getOrigin_latitude());
                drive.setOrigin_longitude(driver.getOrigin_longitude());
                drive.setDest_latitude(driver.getDest_latitude());
                drive.setDest_longitude(driver.getDest_longitude());
                drive.setPrice(price);
                drive.setAccepted(accepted != 0);
                drive.setCompleted(completed != 0);
                drive.setDate_time(date_time);

                // Add to the list
                drives.add(drive);
                Log.d("DBHelper DEBUG", drive.toString());
            }
            cursor.close();
        }

        return drives;
    }

//    public DriveModel getPendingOffer(int passengerId){
//        DriveModel drive = null;
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String sql = "SELECT * FROM " + DRIVE_TABLE +
//                " WHERE " + COLUMN_PASSENGER_ID + " = ?" + " AND " + COLUMN_DATE_TIME + " != ''";
//
//        Cursor cursor = db.rawQuery(sql, new String[]{ String.valueOf(passengerId) });
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                Log.d("DBHelper DEBUG", "Cursor has elements!");
//                UserModel driver = getUser(driverId);
//
//                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
//                String destLat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEST_LAT));
//                String destLong = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEST_LNG));
//                int price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE));
//                int accepted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACCEPTED));
//                int completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED));
//                String date_time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME));
//
//
//                // Create UserModel object
//                DriveModel drive = new DriveModel();
//                drive.setId(id);
//                drive.setDriver_id(driver.getId());
//                drive.setOrigin_latitude(driver.getOrigin_latitude());
//                drive.setOrigin_longitude(driver.getOrigin_longitude());
//                drive.setDest_latitude(driver.getDest_latitude());
//                drive.setDest_longitude(driver.getDest_longitude());
//                drive.setPrice(price);
//                drive.setAccepted(accepted != 0);
//                drive.setCompleted(completed != 0);
//                drive.setDate_time(date_time);
//
//                // Add to the list
//                drives.add(drive);
//                Log.d("DBHelper DEBUG", drive.toString());
//            }
//            cursor.close();
//        }
//
//        return drive;
//    }

    // TODO: UPDATE / EDIT --------------------------------------------------
    public int updateUser(UserModel user) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ACTIVE_VEHICLE_ID, user.getActive_vehicle());
        cv.put(COLUMN_ORIGIN_LAT, user.getOrigin_latitude());
        cv.put(COLUMN_ORIGIN_LNG, user.getOrigin_longitude());
        cv.put(COLUMN_DEST_LAT, user.getDest_latitude());
        cv.put(COLUMN_DEST_LNG, user.getDest_longitude());


        SQLiteDatabase db = getWritableDatabase();
        int result = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});

        return result;
//        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_ID, user.getId());
//        cv.put(COLUMN_ACTIVE_VEHICLE_ID, user.getActive_vehicle());
//
//        int result = db.update(USER_TABLE, cv, null, null);
//
//        return result;
// --------------------------
//        Log.d("IN updateUser()", "active_vehicle_id = " + user.getActive_vehicle());
//
//        String sql = "UPDATE " + USER_TABLE + " SET "
//                + COLUMN_ORIGIN_LAT + " = " + user.getOrigin_latitude() + ", "
//                + COLUMN_ORIGIN_LNG + " = " + user.getOrigin_longitude() + ", "
//                + COLUMN_DEST_LAT + " = " + user.getDest_latitude() + ", "
//                + COLUMN_DEST_LNG + " = " + user.getDest_longitude() + ", "
//                + COLUMN_ACTIVE_VEHICLE_ID + " = " + user.getActive_vehicle()
//                + " WHERE "
//                + COLUMN_ID + " = " + user.getId() + ";"
//                ;
//
//        Cursor cursor = db.rawQuery(sql, null);
//        if(cursor.moveToFirst()){
//            Log.d("IN updateUser()", "after rawQuery, active_vehicle_id = " + cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTIVE_VEHICLE_ID)));
//            cursor.close();
//            return cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTIVE_VEHICLE_ID));
//        }
//
//        return 0;
    }

    public int updateDrive(DriveModel drive){
        SQLiteDatabase db = this.getWritableDatabase();

        UserModel driver = getUser(drive.getDriver_id());

        String sql = "UPDATE " + DRIVE_TABLE + " SET "
                + COLUMN_DATE_TIME + " = " + "'" + drive.getDate_time() + "', "
                + COLUMN_PASSENGER_ID + " = " + drive.getPassenger_id() + ", "
                + COLUMN_ORIGIN_LAT + " = " + driver.getOrigin_latitude() + ", "
                + COLUMN_ORIGIN_LNG + " = " + driver.getOrigin_longitude() + ", "
                + COLUMN_DEST_LAT + " = " + driver.getDest_latitude() + ", "
                + COLUMN_DEST_LNG + " = " + driver.getDest_longitude()
                + " WHERE "
                + COLUMN_ID + " = " + drive.getId() + ";"
                ;

        db.execSQL(sql);

        return 0;
    }

    // DELETE
    public boolean deleteUser(int userId){
        SQLiteDatabase db = getWritableDatabase();

        String sql = "DELETE FROM " + USER_TABLE + " WHERE " + COLUMN_ID + " = " + userId;

        try{
            db.execSQL(sql);
        }catch(Exception e){
            Log.d("In DBHelper.deleteUser()", "Exception: " + e.toString());
            return false;
        }

        return true;
    }

    // UTILS
    public int checkCredentials(String email, String password){
        int userId = -1;
        if(email.isBlank() || password.isBlank()){
            return userId;
        }

        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + USER_TABLE +
                " WHERE " + COLUMN_EMAIL + " = '" + email + "' AND " + COLUMN_PASSWORD + " = '" + password + "'";

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            userId = cursor.getInt(0);
        }

        cursor.close();
        return userId;
    }

    // DANGER ZONE
    public void dropUserTable(){
        SQLiteDatabase db = getWritableDatabase();

        String sql = "DROP TABLE IF EXISTS " + USER_TABLE;

        db.execSQL(sql);
    }

    // Haversine formula to calculate distance between two lat/lon points
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371000; // Earth's radius in meters

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
