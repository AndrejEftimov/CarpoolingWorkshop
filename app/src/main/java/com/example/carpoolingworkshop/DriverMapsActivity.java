package com.example.carpoolingworkshop;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.carpoolingworkshop.databinding.ActivityDriverMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.apache.commons.lang3.StringUtils;

public class DriverMapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {
    private Fragment map;
    private SearchView sv_address;
    private Button save_btn;
    private GoogleMap mMap;
    public LatLng latLng;
    private ActivityDriverMapsBinding binding;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private Intent intent;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDriverMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Location");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initProperties();
        setupEventListeners();

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        if (checkLocationPermissions()) {
//            getCurrentLocation();
//        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void initProperties(){
        map = getSupportFragmentManager().findFragmentById(R.id.map);
        sv_address = findViewById(R.id.sv_address);
        save_btn = findViewById(R.id.save_btn);
        latLng = new LatLng(0, 0);
        dbHelper = new DBHelper(this);
        sharedPreferences = getDefaultSharedPreferences(this);
        intent = getIntent();
    }

    public void setupEventListeners(){
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectToActivity(HomeActivity.class);
            }
        });

        sv_address.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when user submits
                searchAddress(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: Implement autocomplete or real-time suggestions
                return false;
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

    // Check location permissions
    private boolean checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                // Handle permission denied
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Get current location
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // Create Geocoding Helper
                            GeocodingHelper geocodingHelper = new GeocodingHelper(getGoogleMapsApiKey());

                            // Get Address
                            geocodingHelper.getAddress(latitude, longitude, new GeocodingHelper.AddressCallback() {
                                @Override
                                public void onAddressFound(String address) {
                                    // Update UI on main thread
                                    runOnUiThread(() -> {
                                        SearchView sv_address = findViewById(R.id.sv_address);
                                        sv_address.setQuery(address, false);
                                    });
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    // Handle error
                                    Log.e("GeocodeError", errorMessage);
                                    runOnUiThread(() -> {
                                        Toast.makeText(DriverMapsActivity.this,
                                                "Could not retrieve address",
                                                Toast.LENGTH_SHORT).show();
                                    });
                                }
                            });
                        }
                    }
                });
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

        int userId = sharedPreferences.getInt("LoggedInUserId", -1);
        UserModel user = dbHelper.getUser(userId);
        Log.d("DatabaseDebug", "User retrieved: " + (user != null ? user.toString() : "NULL"));

        if(user != null) {
            Log.d("LocationDebug", "Origin Latitude: " + user.getOrigin_latitude());
            Log.d("LocationDebug", "Origin Longitude: " + user.getOrigin_longitude());
            Log.d("LocationDebug", "Destination Latitude: " + user.getDest_latitude());
            Log.d("LocationDebug", "Destination Longitude: " + user.getDest_longitude());
        }

        String originOrDest = intent.getStringExtra("originOrDestination");
        String lat = null;
        String lng = null;
        if(originOrDest.equals("origin")){
            lat = dbHelper.getUser(userId).getOrigin_latitude();
            lng = dbHelper.getUser(userId).getOrigin_longitude();
        } else if(originOrDest.equals("destination")){
            lat = dbHelper.getUser(userId).getDest_latitude();
            lng = dbHelper.getUser(userId).getDest_longitude();
        }

        Log.d("HERE", lat + "," + lng);

        if(lat != null){
            if(!lat.isBlank()){
                latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                mMap.addMarker(new MarkerOptions().position(latLng).title("Location"));
            }
        }else{
            // Add a marker in Skopje Center near 'Soboren Hram'
            latLng = new LatLng(41.998274798664305, 21.42582893371582);
            mMap.addMarker(new MarkerOptions().position(latLng).title("Skopje"));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        Toast.makeText(this, "BEFORE ON MAP LOADED CALLBACK", Toast.LENGTH_SHORT).show();
        mMap.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                saveLocation(latLng);

                // Creating a marker
                MarkerOptions options = new MarkerOptions();

                // Setting the position for the marker
                options.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on tapping the marker
                options.title("Location");

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(options);
            }
        });
    }

    // Should stay empty for now
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    public void saveLocation(LatLng latLng){
        Log.d("IN SAVE LOCATION", "latLng = " + latLng);
        int userId = sharedPreferences.getInt("LoggedInUserId", -1);
        Log.d("LOGGED IN USER ID", "" + userId);
        if(userId == -1){
            return;
        }

        UserModel user = dbHelper.getUser(userId);
        Log.d("LOGGED IN USER", user.toString());

        String originOrDest = intent.getStringExtra("originOrDestination");
        Log.d("originOrDest", "" + originOrDest);
        if(originOrDest.isBlank()){
            return;
        }
        if(originOrDest.equals("origin")){
            user.setOrigin_latitude(String.valueOf(latLng.latitude));
            user.setOrigin_longitude(String.valueOf(latLng.longitude));
            Log.d("setOriginLat", String.valueOf(latLng.latitude));
            Log.d("setOriginLng", String.valueOf(latLng.longitude));
        } else if(originOrDest.equals("destination")){
            user.setDest_latitude(String.valueOf(latLng.latitude));
            user.setDest_longitude(String.valueOf(latLng.longitude));
        }

        int result = dbHelper.updateUser(user);
        Log.d("RESULT", "" + result);
        Log.d("IN SAVE LOCATION", "latLng = " + user.getOrigin_latitude() + " : " + user.getOrigin_longitude());
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

    public void searchAddress(String query){

    }

    public void RedirectToActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    public LatLng getlatLng(){
        return this.latLng;
    }
}
