package com.example.carpoolingworkshop;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.google.android.libraries.places.api.Places;

public class PassengerHomeActivity extends AppCompatActivity {
    private TextView tv_available_offers;
    private RecyclerView rv_users_nearby;
    private DrivesAdapter adapter;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passenger_home);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Passenger");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup properties and event listeners
        initProperties();
        setupEventListeners();

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(this, getGoogleMapsApiKey());
        }

        // redirect to LoginActivity if not logged in
        RedirectIfLoggedOut();
    }

    public void initProperties() {
        tv_available_offers = findViewById(R.id.tv_available_offers);

        rv_users_nearby = findViewById(R.id.rv_users_nearby);

        dbHelper = new DBHelper(this);
        sharedPreferences = getDefaultSharedPreferences(this);

        rv_users_nearby.setLayoutManager(new LinearLayoutManager(this));
        int userId = sharedPreferences.getInt("LoggedInUserId", -1);
        UserModel loggedInUser = dbHelper.getUser(userId);

        List<DriveModel> drivesInRadius = dbHelper.getDrivesInRadius(userId, 5000);

        for (DriveModel drive : drivesInRadius) {
            LocationHelper.getAddressFromLocation(Double.parseDouble(drive.getOrigin_latitude()), Double.parseDouble(drive.getOrigin_longitude()), new LocationHelper.OnAddressFetchedListener() {
                @Override
                public void onAddressFetched(String address) {
                    // Handle the fetched address
                    runOnUiThread(() -> {
                        Log.d("PassengerHomeActivity", "Address: " + address);
                        Toast.makeText(PassengerHomeActivity.this, "Address: " + address, Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    // Handle errors
                    runOnUiThread(() -> {
                        Log.e("PassengerHomeActivity", "ERROR: " + errorMessage);
                        //Toast.makeText(PassengerHomeActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    });
                }
            });
        }

        adapter = new DrivesAdapter(drivesInRadius, R.layout.offer_layout, this, position -> {
            // Handle the click event
            DriveModel clickedDrive = drivesInRadius.get(position);
            Log.d("RecyclerView", "Clicked User: " + dbHelper.getUser(clickedDrive.getPassenger_id()).get_fullname());

            Intent intent = new Intent(this, PassengerOfferDetailsActivity.class);
            intent.putExtra("driver_id", clickedDrive.getId());
            startActivity(intent);
        });
        rv_users_nearby.setAdapter(adapter);
    }

    public void setupEventListeners(){
        rv_users_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            Intent intent = new Intent(this, PassengerPendingOfferActivity.class);
//            intent.putExtra("driverId", );
            startActivity(intent);
        }else if(itemId == R.id.logout_item){
            RedirectToActivity(LogoutActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    public String getGoogleMapsApiKey() {
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(
                    getPackageName(),
                    PackageManager.GET_META_DATA
            );
            Bundle bundle = ai.metaData;
            return bundle.getString("com.google.android.geo.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("API_KEY_ERROR", "Failed to load meta-data", e);
            return null;
        }
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