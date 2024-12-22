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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {
    private Button driver_btn;
    private Button passenger_btn;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Home");
        setSupportActionBar(appbar);

        // setup properties and event listeners
        initProperties();
        setupEventListeners();

        // redirect to LoginActivity if not logged in
        RedirectIfLoggedOut();
    }

    public void initProperties(){
        driver_btn = findViewById(R.id.driver_btn);
        passenger_btn = findViewById(R.id.passenger_btn);
        dbHelper = new DBHelper(this);
        sharedPreferences = getDefaultSharedPreferences(this);
    }

    public void setupEventListeners(){
        driver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectToActivity(DriverHomeActivity.class);
            }
        });

        passenger_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectToActivity(PassengerHomeActivity.class);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.driver_item){
            RedirectToActivity(DriverHomeActivity.class);
        }else if(itemId == R.id.passenger_item){
            RedirectToActivity(PassengerHomeActivity.class);
        }else if(itemId == R.id.logout_item){
            RedirectToActivity(LogoutActivity.class);
        }else if(itemId == R.id.register_vehicle_item){
            RedirectToActivity(RegisterVehicleActivity.class);
        }else if(itemId == R.id.admin_item){
            RedirectToActivity(AdminHomeActivity.class);
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