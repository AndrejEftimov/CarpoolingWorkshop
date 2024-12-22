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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class RegisterVehicleActivity extends AppCompatActivity {
    private TextView tv_errors;
    private EditText et_manufacturer;
    private EditText et_model;
    private EditText et_color;
    private EditText et_license_plate;
    private Button register_btn;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_vehicle);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Register Vehicle");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup properties and event listeners
        initProperties();
        setupEventListeners();

        // redirect to LoginActivity if not logged in
        RedirectIfLoggedOut();
    }

    public void initProperties(){
        tv_errors = findViewById(R.id.tv_errors);
        et_manufacturer = findViewById(R.id.et_manufacturer);
        et_model = findViewById(R.id.et_model);
        et_color = findViewById(R.id.et_color);
        et_license_plate = findViewById(R.id.et_license_plate);
        register_btn = findViewById(R.id.register_btn);
        dbHelper = new DBHelper(this);
        sharedPreferences = getDefaultSharedPreferences(this);
    }

    public void setupEventListeners(){
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerVehicle();
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
            RedirectToActivity(DriverVehicleSelectActivity.class);
        }else if(itemId == R.id.passenger_item){
            RedirectToActivity(PassengerHomeActivity.class);
        }else if(itemId == R.id.logout_item){
            RedirectToActivity(LogoutActivity.class);
        }else if(itemId == R.id.register_vehicle_item){
            RedirectToActivity(RegisterVehicleActivity.class);
        }
        else if(itemId == R.id.admin_item){
            RedirectToActivity(AdminHomeActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    public void registerVehicle(){
        VehicleModel vehicle = null;
        try{
            vehicle = new VehicleModel(

                    et_manufacturer.getText().toString(),
                    et_model.getText().toString(),
                    et_color.getText().toString(),
                    et_license_plate.getText().toString(),
                    sharedPreferences.getInt("LoggedInUserId", -1)
            );
        }catch(Exception e){
            Toast.makeText(this, "Exception: " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        List<String> errors = vehicle.checkErrors();
        if (!errors.isEmpty()) {
            tv_errors.setText(errors.toString());
            Toast.makeText(this, "Invalid input!", Toast.LENGTH_LONG).show();
            return;
        }

        boolean result = dbHelper.createVehicle(vehicle);
        if(!result){
            Toast.makeText(this, "Couldn't create Vehicle.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Successfully added Vehicle.", Toast.LENGTH_SHORT).show();
        RedirectToActivity(HomeActivity.class);
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
