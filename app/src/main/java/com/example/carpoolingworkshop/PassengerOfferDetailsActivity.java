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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.carpoolingworkshop.databinding.ActivityDriverMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Text;

import java.util.List;

public class PassengerOfferDetailsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener{
    private ConstraintLayout cl_driver_details;
    private TextView tv_fullname;
    private TextView tv_email;
    private TextView tv_phone_number;
    private TextView tv_vehicle;
    private TextView tv_rating;
    private TextView tv_date_time;
    private Button make_offer_btn;
    private Fragment map;
    private GoogleMap mMap;
    public LatLng latLng;
    private ActivityDriverMapsBinding binding;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passenger_offer_details);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Offer");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup properties and event listeners
        initProperties();
        fillData();
        setupEventListeners();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }
    }

    public void initProperties() {
        cl_driver_details = findViewById(R.id.cl_driver_details);
        tv_fullname = findViewById(R.id.tv_fullname);
        tv_email = findViewById(R.id.tv_email);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_vehicle = findViewById(R.id.tv_vehicle);
        tv_rating = findViewById(R.id.tv_rating);
        tv_date_time = findViewById(R.id.tv_date_time);
        make_offer_btn = findViewById(R.id.make_offer_btn);
        dbHelper = new DBHelper(this);
        sharedPreferences = getDefaultSharedPreferences(this);
        intent = getIntent();
    }

    public void fillData(){
        int driverId = intent.getIntExtra("driver_id", -1);
        if(driverId == -1){
            return;
        }

        UserModel driver = dbHelper.getUser(driverId);
        if(driver == null){
            return;
        }
        VehicleModel vehicle = null;
        if(driver.getActive_vehicle() >= 0){
            vehicle = dbHelper.getVehicle(driver.getActive_vehicle());
        }
        DriveModel drive = dbHelper.getDrive(driverId);

        tv_fullname.setText(driver.get_fullname());
        tv_email.setText(driver.getEmail());
        tv_phone_number.setText(driver.getPhone_number());
        if(vehicle != null){
            tv_vehicle.setText(vehicle.toString());
        }
        tv_rating.setText(String.valueOf(driver.getRating()));
        tv_date_time.setText(drive.getDate_time());
    }

    public void setupEventListeners(){
        make_offer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOffer();
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

    public void makeOffer(){
        int driverId = intent.getIntExtra("driver_id", -1);
        if(driverId == -1){
            return;
        }
        int passengerId = sharedPreferences.getInt("LoggedInUserId", -1);
        if(passengerId == -1){
            return;
        }

        DriveModel drive = dbHelper.getDrive(driverId);
        if(drive.isAccepted()){
            return;
        }
        drive.setPassenger_id(passengerId);
        dbHelper.updateDrive(drive);

        Intent new_intent = new Intent(this, PassengerPendingOfferActivity.class);
        new_intent.putExtra("driverId", driverId);
        startActivity(new_intent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        int driverId = intent.getIntExtra("driver_id", -1);
        UserModel driver = dbHelper.getUser(driverId);
        Log.d("DatabaseDebug", "User retrieved: " + (driver != null ? driver.toString() : "NULL"));

        if(driver != null) {
            Log.d("LocationDebug", "Origin Latitude: " + driver.getOrigin_latitude());
            Log.d("LocationDebug", "Origin Longitude: " + driver.getOrigin_longitude());
            Log.d("LocationDebug", "Destination Latitude: " + driver.getDest_latitude());
            Log.d("LocationDebug", "Destination Longitude: " + driver.getDest_longitude());
        }


        String originLat = dbHelper.getUser(driverId).getOrigin_latitude();
        String originLng = dbHelper.getUser(driverId).getOrigin_longitude();
        String destLat = dbHelper.getUser(driverId).getDest_latitude();
        String destLng = dbHelper.getUser(driverId).getDest_longitude();

        if(originLat != null && destLat != null){
            if(!originLat.isBlank() && !destLat.isBlank()){
                latLng = new LatLng(Double.parseDouble(originLat), Double.parseDouble(originLng));
                mMap.addMarker(new MarkerOptions().position(latLng).title("Driver Start Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                latLng = new LatLng(Double.parseDouble(destLat), Double.parseDouble(destLng));
                mMap.addMarker(new MarkerOptions().position(latLng).title("Driver End Location"));

                mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(Double.parseDouble(originLat), Double.parseDouble(originLng)))
                        .add(new LatLng(Double.parseDouble(destLat), Double.parseDouble(destLng)))
                );

                mMap.setOnMapLoadedCallback(this);
            }
        }else{
            Toast.makeText(this, "No origin for Driver!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapLoaded() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
            }
        });
    }

    // Should stay empty for now
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
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

    // UTILS ------------------------------------------------------------
    public void RedirectToActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}