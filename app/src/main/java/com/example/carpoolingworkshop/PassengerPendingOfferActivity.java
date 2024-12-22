package com.example.carpoolingworkshop;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.Places;

import java.util.List;

public class PassengerPendingOfferActivity extends AppCompatActivity {
    private TextView tv_fullname;
    private TextView tv_email;
    private TextView tv_phone_number;
    private TextView tv_vehicle;
    private TextView tv_rating;
    private TextView tv_date_time;
    private TextView tv_accepted;
    private TextView tv_completed;
    private TextView tv_price;
    private Button cancel_btn;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private Intent intent;
    private int passengerId;
    private int driverId;
    private DriveModel drive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passenger_pending_offer);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Offer Status");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup properties and event listeners
        initProperties();
        setData();
        setupEventListeners();
    }

    public void initProperties() {
        tv_fullname = findViewById(R.id.tv_fullname);
        tv_email = findViewById(R.id.tv_email);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_vehicle = findViewById(R.id.tv_vehicle);
        tv_rating = findViewById(R.id.tv_rating);
        tv_date_time = findViewById(R.id.tv_date_time);
        tv_accepted = findViewById(R.id.tv_accepted);
        tv_completed = findViewById(R.id.tv_completed);
        tv_price = findViewById(R.id.tv_price);
        cancel_btn = findViewById(R.id.cancel_btn);

        dbHelper = new DBHelper(this);
        sharedPreferences = getDefaultSharedPreferences(this);
        intent = getIntent();

        driverId = intent.getIntExtra("driverId", -1);
        passengerId = sharedPreferences.getInt("LoggedInUserId", -1);
        if(driverId == -1 || passengerId == -1){
            RedirectToActivity(PassengerHomeActivity.class);
        }

        drive = dbHelper.getDrive(driverId, passengerId);
    }

    public void setData(){
//        DriveModel drive = dbHelper.getPendingOffer(passengerId);
        UserModel driver = dbHelper.getUser(driverId);
        if(driver == null){
            return;
        }
        VehicleModel vehicle = null;
        if(driver.getActive_vehicle() >= 0){
            vehicle = dbHelper.getVehicle(driver.getActive_vehicle());
        }


        tv_fullname.setText(driver.get_fullname());
        tv_email.setText(driver.getEmail());
        tv_phone_number.setText(driver.getPhone_number());
        if(vehicle != null){
            tv_vehicle.setText(vehicle.toString());
        }
        tv_rating.setText(String.valueOf(driver.getRating()));
        tv_date_time.setText(drive.getDate_time());
        tv_accepted.setText("Accepted: " + String.valueOf(drive.isAccepted()));
        tv_completed.setText("Completed: " + String.valueOf(drive.isCompleted()));
        tv_price.setText("Price: " + String.valueOf(drive.getPrice()));
    }

    public void setupEventListeners(){
        cancel_btn
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOffer();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.passenger_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.home_item){
            RedirectToActivity(HomeActivity.class);
        }else if(itemId == R.id.set_origin_item){
            Intent intent = new Intent(this, DriverMapsActivity.class);
            intent.putExtra("originOrDestination", "origin");
            startActivity(intent);
        }else if(itemId == R.id.set_destination_item){
            Intent intent = new Intent(this, DriverMapsActivity.class);
            intent.putExtra("originOrDestination", "destination");
            startActivity(intent);
        }else if(itemId == R.id.pending_offer_item){
            RedirectToActivity(PassengerPendingOfferActivity.class);
        }else if(itemId == R.id.logout_item){
            RedirectToActivity(LogoutActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    public void cancelOffer(){
        drive.setPassenger_id(-1);
        RedirectToActivity(PassengerHomeActivity.class);
    }

    // UTILS
    public void RedirectToActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}