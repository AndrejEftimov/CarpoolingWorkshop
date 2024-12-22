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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class DriverHomeActivity extends AppCompatActivity {
    private TextView tv_your_vehicle;
    private TextView tv_chosen_vehicle;
    private TextView tv_your_requests;
    private ListView lv_requests;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Driver");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup properties
        initProperties();

        setupDataAndLayout();

        // setup event listeners
        setupEventListeners();

        // redirect to LoginActivity if not logged in
        RedirectIfLoggedOut();
    }

    public void initProperties(){
        tv_your_vehicle = findViewById(R.id.tv_your_vehicle);
        tv_chosen_vehicle = findViewById(R.id.tv_chosen_vehicle);
        tv_your_requests = findViewById(R.id.tv_your_requests);
        lv_requests = findViewById(R.id.lv_requests);
        dbHelper = new DBHelper(this);
        sharedPreferences = getDefaultSharedPreferences(this);

        tv_chosen_vehicle.setText("No vehicle chosen");
    }

    public void setupDataAndLayout(){
        int userId = sharedPreferences.getInt("LoggedInUserId", -1);
        UserModel user = dbHelper.getUser(userId);
        if(user == null){
            Toast.makeText(this, "user is null", Toast.LENGTH_SHORT).show();
        }
        Log.d("Debug", "Active Vehicle ID: " + user.getActive_vehicle());

        VehicleModel vehicle = dbHelper.getVehicle(user.getActive_vehicle());
        if(vehicle != null){
            tv_chosen_vehicle.setText(vehicle.toString());
        }

        List<DriveModel> requestedDrives = dbHelper.getRequestedDrives(userId);
        List<String> list = new ArrayList<String>();
        for(DriveModel drive : requestedDrives){
            list.add(dbHelper.getUser(drive.getPassenger_id()).toString());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        lv_requests.setAdapter(adapter);
    }

    public void setupEventListeners(){
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.driver_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.home_item){
            RedirectToActivity(HomeActivity.class);
        }else if(itemId == R.id.register_vehicle_item){
            RedirectToActivity(RegisterVehicleActivity.class);
        }else if(itemId == R.id.choose_vehicle_item){
            RedirectToActivity(DriverVehicleSelectActivity.class);
        }else if(itemId == R.id.set_date_time_item){
            RedirectToActivity(DriverSetDateTimeActivity.class);
        }else if(itemId == R.id.set_origin_item){
            Intent intent = new Intent(this, DriverMapsActivity.class);
            intent.putExtra("originOrDestination", "origin");
            startActivity(intent);
        }else if(itemId == R.id.set_destination_item){
            Intent intent = new Intent(this, DriverMapsActivity.class);
            intent.putExtra("originOrDestination", "destination");
            startActivity(intent);
        }else if(itemId == R.id.logout_item){
            RedirectToActivity(LogoutActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    public void RedirectToActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    public void RedirectIfLoggedOut(){
        int userId = sharedPreferences.getInt("LoggedInUserId", -1);
        if(userId < 0 || dbHelper.getUser(userId) == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("LoggedInUserId", -1);
            editor.apply();
            RedirectToActivity(LoginActivity.class);
        }
    }
}