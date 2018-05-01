package com.android.mechanico.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.mechanico.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akhil on 26/4/18.
 */

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback, MaterialSearchView.OnQueryTextListener, GoogleMap.OnMarkerClickListener, SearchView.OnQueryTextListener, View.OnClickListener {

    private GeoDataClient mGeoDataClient;

    private PlaceDetectionClient mPlaceDetectionClient;
    private AlertDialog alertDialog;

   // MaterialSearchView searchView;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private GoogleMap map;
    private Toolbar toolbar;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private String currentUser;
    private SearchView searchView;
    List<Address> addressesFromCurrent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_mechanic);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapfrag);
        mapFragment.getMapAsync(this);

        // searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView = findViewById(R.id.searchLoc);
         toolbar = findViewById(R.id.toolbar);
         toolbar.setTitle(R.string.title);
         toolbar.setTitleTextColor(Color.WHITE);
         setSupportActionBar(toolbar);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("enter street name");
        searchView.setIconified(false);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();

        currentUser = getIntent().getStringExtra("currentUser");
        setUserLoginPreferences(currentUser);
        Button btnPickUpLater = findViewById(R.id.btnpickuplater);
        btnPickUpLater.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        exitApptoHome();
    }

    private void exitApptoHome(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_find_mechanic_activity, menu);
        return true;
    }

    private void setUserLoginPreferences(String currentUser){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",true);
        editor.putString("currentuser",currentUser);
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menuloc:
                getDeviceLocation();
                break;

            case R.id.menulogout:
                 showDialog();
                 break;
        }

        return super.onOptionsItemSelected(item);

    }


    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do You Want To Logout")
                .setTitle("Mechanico")
                .setPositiveButton("SignOut", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                        alertDialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                    }
                })
                .setCancelable(true)
                .show();

        alertDialog = builder.create();
    }

    private void logout(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin",false);
        editor.putString("currentuser",null);
        editor.apply();

        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    55);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case 55: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        //updateLocationUI();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        map.setOnMarkerClickListener(this);
        // Do other setup activities here too, as described elsewhere in this tutorial.

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

       /* LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

    }

    private void getDeviceLocation() {
        map.clear();
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            LatLng location = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                            map.addMarker(new MarkerOptions().position(location));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 15));
                            LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                            getLocationAdressFromAutoDetect(latLng);
                        } else {
                            Log.d("location", "Current location is null. Using defaults.");
                            Log.e("Exception", "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 15));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }

    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getLocationLatLng(query.trim());
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    private void getLocationAdressFromAutoDetect(LatLng latLng){

        if(Geocoder.isPresent()){
            try {
                Geocoder gc = new Geocoder(this);
                addressesFromCurrent = gc.getFromLocation(latLng.latitude,latLng.longitude,1); // get the found Address Objects
                List<LatLng> latLngs = new ArrayList<LatLng>(addressesFromCurrent.size()); // A list to save the coordinates if they are available
                for(Address a : addressesFromCurrent){
                    Log.i("locality",""+a.getSubLocality());
                    Log.i("locality",""+a.getPremises());
                    Log.i("locality",""+a.getThoroughfare());
                    Log.i("locality",""+a.getSubThoroughfare());
                    Log.i("locality",""+a.getAddressLine(0));
                    searchView.setQuery(a.getSubLocality(),false);
                }
                Log.i("latlng",""+latLngs.isEmpty());

              /*  if(latLngs.isEmpty())
                {
                    Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT).show();
                    map.clear();

                }
                else {
                    for(LatLng obj:latLngs)
                    {

                        Log.i("latlng",""+obj);
                        Log.i("location",""+gc.getFromLocation(obj.latitude,obj.longitude,1));
                        map.addMarker(new MarkerOptions().position(obj));
                        map.moveCamera(CameraUpdateFactory.newLatLng(obj));

                    }
                }*/

            } catch (IOException e) {
                // handle the exception
            }
        }
    }

    private void getLocationLatLng(String location){
        if(Geocoder.isPresent()){
            map.clear();
            try {
                Geocoder gc = new Geocoder(this);
                List<Address> addresses= gc.getFromLocationName(location, 1); // get the found Address Objects
                List<LatLng> latLngs = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        latLngs.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
                Log.i("latlng",""+latLngs.isEmpty());

                if(latLngs.isEmpty())
                {
                    Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT).show();
                    map.clear();

                }
                else {
                    for(LatLng obj:latLngs)
                    {

                        Log.i("latlng",""+obj);
                        Log.i("location",""+gc.getFromLocation(obj.latitude,obj.longitude,1));
                    map.addMarker(new MarkerOptions().position(obj));
                    map.moveCamera(CameraUpdateFactory.newLatLng(obj));

                    }
                }

            } catch (IOException e) {
                // handle the exception
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
         LatLng latLng = marker.getPosition();
         double lat = latLng.latitude;
         double lon = latLng.longitude;
        Intent intent = new Intent(this,MechanicListActivity.class);
        intent.putExtra("lat",lat);
        intent.putExtra("lon",lon);
        startActivity(intent);
        return false;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,PickupActivity.class);
        startActivity(intent);
    }
}
