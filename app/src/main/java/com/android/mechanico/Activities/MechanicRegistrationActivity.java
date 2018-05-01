package com.android.mechanico.Activities;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.mechanico.FirebaseTablesPojo.MechanicRegistrationFirebaseTable;
import com.android.mechanico.R;
import com.android.mechanico.utils.CommonUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuaki on 4/27/2018.
 */

public class MechanicRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText etUsername, etEmail, etMobileNum, etLicenseNumber, etLocation;
    private FirebaseAuth auth;
    private ImageView ivLocation;
    private Toolbar toolbar;
    private Button btnSubmit;
    private ProgressDialog dialog;
    List<Address> addressesFromCurrent;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private ImageView ivCurrenntLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_registration);
        getLocationPermission();
        viewsIntialization();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewsIntialization() {
        etUsername = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etMobileNum = findViewById(R.id.etMobileNum);

        etLicenseNumber = findViewById(R.id.etLicenseNumber);
        etLocation = findViewById(R.id.etLocation);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        auth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Registration");
        //toolbar.setNavigationIcon(R.drawable.a);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("loading");
        dialog.setCancelable(true);
        ivCurrenntLocation = findViewById(R.id.ivCurrenntLocation);
        ivCurrenntLocation.setOnClickListener(this);

    }

    private void registerByValidation() {
        if (TextUtils.isEmpty(etUsername.getText().toString().trim())) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etMobileNum.getText().toString().trim())) {
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (etMobileNum.getText().toString().trim().length() < 10) {
            Toast.makeText(this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etLicenseNumber.getText().toString().trim())) {
            Toast.makeText(this, "Enter License Number", Toast.LENGTH_SHORT).show();
        } else if (etLicenseNumber.getText().toString().length() < 16 ) {
            Toast.makeText(this, "Enter valid License Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etLocation.getText().toString().trim())) {
            Toast.makeText(this, "Enter Location", Toast.LENGTH_SHORT).show();
        } else {
            registerMechanic();
        }
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


    private void registerMechanic() {
        dialog.show();
        String mechId = CommonUtils.generateMechanicID();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Mechanic");

        MechanicRegistrationFirebaseTable mech = new MechanicRegistrationFirebaseTable();
        mech.setUsername(etUsername.getText().toString().trim());
        mech.setEmail(etEmail.getText().toString().trim());
        mech.setMobileNumber(etMobileNum.getText().toString().trim());
        mech.setLicenseNumber(etLicenseNumber.getText().toString().trim());
        mech.setLocation(etLocation.getText().toString().trim());

        LatLng latLng = getLocationLatLng(etLocation.getText().toString().trim());
        if (latLng != null) {
            mech.setLocationLat(latLng.latitude);
            mech.setLocationLng(latLng.longitude);
        }
        mDatabase.child(mechId).setValue(mech).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    dialog.dismiss();
                    dialog.cancel();
                    Toast.makeText(MechanicRegistrationActivity.this, "Registered Success", Toast.LENGTH_SHORT).show();
                    finish();
                    setEmpty();
                } else {
                    dialog.dismiss();
                    dialog.cancel();
                    Toast.makeText(MechanicRegistrationActivity.this, "Registered Failed", Toast.LENGTH_SHORT).show();
                    finish();
                    setEmpty();
                }

            }
        });
    }

    private LatLng getLocationLatLng(String location) {
        if (mLocationPermissionGranted) {
            if (Geocoder.isPresent()) {
                try {
                    Geocoder gc = new Geocoder(this);
                    List<Address> addresses = gc.getFromLocationName(location, 1); // get the found Address Objects

                    List<LatLng> latLngs = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                    for (Address a : addresses) {
                        if (a.hasLatitude() && a.hasLongitude()) {
                            latLngs.add(new LatLng(a.getLatitude(), a.getLongitude()));
                        }
                    }
                    Log.i("latlng", "" + latLngs.isEmpty());
                    if (latLngs.isEmpty()) {
                        Toast.makeText(this, "Please Enter Valid Location", Toast.LENGTH_SHORT).show();
                        return null;
                    } else {
                        return latLngs.get(0);
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // handle the exception
                }
            }
        } else {
            Toast.makeText(this, "Please Give Location Permission Inorder To USe The App", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void getDeviceLocation() {

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

                            LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            getLocationAdressFromAutoDetect(latLng);
                        } else {
                            Log.d("location", "Current location is null. Using defaults.");
                            Log.e("Exception", "Exception: %s", task.getException());

                        }
                    }
                });
            } else {
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }

    private void getLocationAdressFromAutoDetect(LatLng latLng) {

        if (Geocoder.isPresent()) {
            try {
                Geocoder gc = new Geocoder(this);
                addressesFromCurrent = gc.getFromLocation(latLng.latitude, latLng.longitude, 1); // get the found Address Objects
                List<LatLng> latLngs = new ArrayList<LatLng>(addressesFromCurrent.size()); // A list to save the coordinates if they are available
                for (Address a : addressesFromCurrent) {
                    Log.i("locality", "" + a.getSubLocality());
                    Log.i("locality", "" + a.getPremises());
                    Log.i("locality", "" + a.getThoroughfare());
                    Log.i("locality", "" + a.getSubThoroughfare());
                    Log.i("locality", "" + a.getAddressLine(0));
                    etLocation.setText(a.getSubLocality());
                }
                Log.i("latlng", "" + latLngs.isEmpty());

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

    private void setEmpty() {
        etUsername.setText("");
        etMobileNum.setText("");
        etLocation.setText("");
        etLicenseNumber.setText("");
        etEmail.setText("");
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit)
            registerByValidation();
        else if (view.getId() == R.id.ivCurrenntLocation)
            getDeviceLocation();

    }
}
