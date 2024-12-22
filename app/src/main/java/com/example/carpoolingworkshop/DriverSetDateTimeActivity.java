package com.example.carpoolingworkshop;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DriverSetDateTimeActivity extends AppCompatActivity {
    private DatePicker date_picker;
    private TimePicker time_picker;
    private Button save_btn;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_set_date_time);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Driver");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initProperties();
        setupEventListeners();
    }

    public void initProperties(){
        date_picker = findViewById(R.id.date_picker);
        time_picker = findViewById(R.id.time_picker);
        save_btn = findViewById(R.id.save_btn);
        dbHelper = new DBHelper(this);
        sharedPreferences = getDefaultSharedPreferences(this);
    }

    public void setupEventListeners(){
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDateTime();
            }
        });
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

    public void saveDateTime(){
        String day = String.valueOf(date_picker.getDayOfMonth());
        String month = String.valueOf(date_picker.getMonth());
        String year = String.valueOf(date_picker.getYear());
        String time = time_picker.getHour() + ":" + time_picker.getMinute();
        String date_time = day + "." + month + "." + year + " - " + time;

        int driverId = sharedPreferences.getInt("LoggedInUserId", -1);
        if(driverId == -1){
            return;
        }
        DriveModel drive = dbHelper.getDrive(driverId);
        drive.setDate_time(date_time);
        dbHelper.updateDrive(drive);

        RedirectToActivity(DriverHomeActivity.class);
    }

    // UTILS
    public void RedirectToActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}