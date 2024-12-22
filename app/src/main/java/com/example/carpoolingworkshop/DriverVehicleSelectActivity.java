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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class DriverVehicleSelectActivity extends AppCompatActivity {
    private TextView tv_choose_vehicle;
    private ListView lv_vehicle_list;
    private ArrayAdapter<VehicleModel> adapter;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_vehicle_select);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Driver");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup properties
        initProperties();

        // setup event listeners
        setupEventListeners();

        // redirect to LoginActivity if not logged in
        RedirectIfLoggedOut();
    }

    public void initProperties(){
        tv_choose_vehicle = findViewById(R.id.tv_choose_vehicle);
        lv_vehicle_list = findViewById(R.id.lv_vehicle_list);
        dbHelper = new DBHelper(this);
        sharedPreferences = getDefaultSharedPreferences(this);

        updateVehicleList();
    }

    public void setupEventListeners(){
        lv_vehicle_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VehicleModel vehicle = (VehicleModel) parent.getItemAtPosition(position);
                UserModel user = dbHelper.getUser(sharedPreferences.getInt("LoggedInUserId", -1));
                if(user != null){
                    Log.d("HERE on select vehicle", "vehicle_id = " + vehicle.getId());
                    user.setActive_vehicle(vehicle.getId());
                    dbHelper.updateUser(user);
                    Log.d("HERE again", "" + user.getActive_vehicle());
                    Log.d("HERE again", "" + dbHelper.getUser(user.getId()).getActive_vehicle());
                    RedirectToActivity(DriverHomeActivity.class);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateVehicleList();
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
        }else if(itemId == R.id.choose_vehicle_item){
            RedirectToActivity(DriverVehicleSelectActivity.class);
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

    public void updateVehicleList(){
        List<VehicleModel> list = new ArrayList<>();

        int userId = sharedPreferences.getInt("LoggedInUserId", -1);
        if(userId >= 0){
            list = dbHelper.getVehiclesForUser(userId);
        }

        adapter = new ArrayAdapter<VehicleModel>(this, android.R.layout.simple_list_item_1, list);
        lv_vehicle_list.setAdapter(adapter);
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