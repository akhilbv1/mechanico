package com.android.mechanico.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.mechanico.Manifest;
import com.android.mechanico.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by anuaki on 4/28/2018.
 */

public class PickupActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText etCustName, etContNum, etPickupTime, etProbType, etLocation, etMechList, etVehicleType;

    private EditText etPickupDate;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String number;
    private CharSequence probarr[];
    private String problem;
    List<Address> addressesFromCurrent;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private boolean mSmsPermissionGranted;
    private ImageView ivCurrenntLocation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_later);
        viewIntialization();
        getLocationPermission();
        requestSmsPermission();
    }

    private void viewIntialization() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("PickUp");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etCustName = findViewById(R.id.etCustName);
        etContNum = findViewById(R.id.etContNum);
        etPickupDate = findViewById(R.id.etPickDate);
        etPickupTime = findViewById(R.id.etTime);
        etProbType = findViewById(R.id.etType);
        etLocation = findViewById(R.id.etLocation);
        etMechList = findViewById(R.id.etMech);
        etVehicleType = findViewById(R.id.etVehType);
        ivCurrenntLocation = findViewById(R.id.ivCurrenntLocation);

        Button button = findViewById(R.id.btnPickUP);
        button.setOnClickListener(this);
        etMechList.setOnClickListener(this);
        etProbType.setOnClickListener(this);
        etPickupDate.setOnClickListener(this);
        etPickupTime.setOnClickListener(this);
        ivCurrenntLocation.setOnClickListener(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }

    private void openMechList() {
        if(!TextUtils.isEmpty(etLocation.getText().toString().trim()))
        {
        Intent intent = new Intent(this, MechanicPickListActivity.class);
        intent.putExtra("location",etLocation.getText().toString().trim());
        startActivityForResult(intent, 133);
    }
    else {
            Toast.makeText(this, "Please Select Location First", Toast.LENGTH_SHORT).show();
        }}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 133 && data!=null) {
            etMechList.setText(data.getStringExtra("mechName"));
            number = data.getStringExtra("mechNum");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        etPickupDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void openTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        etPickupTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    private void setProbType() {
        final CharSequence items[] = new CharSequence[]{"Flat Typre", "Jumper Start", "Electrical Problem", "BreakDown", "Met With Accident",
                "Towing Help", "No-Fuel Petrol", "No-Fuel Diesel"};
        final boolean isCheck[] = new boolean[]{false, false, false, false, false, false, false, false};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                Log.i("position", "" + i);
                Log.i("boolean", "" + b);
                isCheck[i] = b;
            }
        })
                .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (i = 0; i < isCheck.length; i++) {
                            boolean check = isCheck[i];
                            if (check) {
                                etProbType.setText(etProbType.getText().toString() + items[i] + ",");
                            }
                        }

                    }
                })
                .setCancelable(true);


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.etPickDate:
                openDatePicker();
                break;

            case R.id.etType:
                setProbType();
                break;

            case R.id.etTime:
                openTimePickerDialog();
                break;

            case R.id.etMech:
                openMechList();
                break;

            case R.id.btnPickUP:
                 checkValidations();
                break;

            case R.id.ivCurrenntLocation:
                getDeviceLocation();
                break;
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

            case 122:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mSmsPermissionGranted = true;
                } else {
                    mSmsPermissionGranted = false;
                }
        }
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

    private void requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.SEND_SMS},
                    122);
        } else {
            // permission already granted run sms send

        }
    }

    private void openSms() {
        String msg = "Hii,Please PickUp My Vehicle Located At " + etLocation.getText().toString().trim() + "and Problem is " + etProbType.getText().toString().trim() + "On " + etPickupDate.getText().toString().trim() + "On " + etPickupTime.getText().toString().trim() + " \n Customer Name:- " + etCustName.getText().toString().trim() + " Contact Number:- " + etContNum.getText().toString().trim();
        Uri uri = Uri.parse("smsto:" + number);
        try {

            Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
            sendIntent.putExtra("sms_body", "" + msg);
            // sendIntent.setType("vnd.android-dir/mms-sms");
            startActivity(sendIntent);
            setEmpty();

        } catch (Exception e) {
            Log.i("error", "" + e.getMessage());
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            setEmpty();
            e.printStackTrace();
        }

    }

    private void checkValidations() {
        if (TextUtils.isEmpty(etCustName.getText().toString().trim())) {
            Toast.makeText(this, "Enter Customer Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etContNum.getText().toString().trim())) {
            Toast.makeText(this, "Enter Cutomer Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etPickupDate.getText().toString().trim())) {
            Toast.makeText(this, "Enter Pickup Date", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etPickupTime.getText().toString().trim())) {
            Toast.makeText(this, "Enter PickupTime", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etProbType.getText().toString().trim())) {
            Toast.makeText(this, "Enter Problem Type", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etLocation.getText().toString().trim())) {
            Toast.makeText(this, "Enter Location", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etMechList.getText().toString().trim())) {
            Toast.makeText(this, "click mechanic list", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etVehicleType.getText().toString().trim())) {
            Toast.makeText(this, "Enter Vehicle Type", Toast.LENGTH_SHORT).show();
        }
        else {
            openSms();
        }
    }

    private void setEmpty(){
        etContNum.setText("");
        etCustName.setText("");
        etPickupDate.setText("");
        etPickupTime.setText("");
        etProbType.setText("");
        etLocation.setText("");
        etMechList.setText("");
        etVehicleType.setText("");
    }


    public void sendSMS() {
        if (mSmsPermissionGranted) {
            SmsManager sm = SmsManager.getDefault();
            String number = this.number;
            Log.i("number", "" + number);

            String msg = "Hii,Please PickUp My Vehicle Located At " + etLocation.getText().toString().trim() + "and Problem is " + etProbType.getText().toString().trim() + "On " + etPickupDate.getText().toString().trim() + "On " + etPickupTime.getText().toString().trim() + " \n Customer Name:- " + etCustName.getText().toString().trim() + " Contact Number:- " + etContNum.getText().toString().trim();
            Log.i("msg", "" + msg);
            sm.sendTextMessage(number, null, msg, null, null);
        } else {
            requestSmsPermission();
        }
    }

}
