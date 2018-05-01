package com.android.mechanico.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.mechanico.Adapters.MechanicListAdapter;
import com.android.mechanico.Adapters.MechanicListPickUpAdapter;
import com.android.mechanico.FirebaseTablesPojo.MechanicRegistrationFirebaseTable;
import com.android.mechanico.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuaki on 4/28/2018.
 */

public class MechanicPickListActivity extends AppCompatActivity implements MechanicListPickUpAdapter.onMechPickUpItemClickListener {

    private List<MechanicRegistrationFirebaseTable> mechList = new ArrayList<>();
    private MechanicListPickUpAdapter mechanicListAdapter;
    private ProgressDialog dialog;
    private List<MechanicRegistrationFirebaseTable> nearMechList = new ArrayList<>();
    private LinearLayout llNoData;
    private RecyclerView recmechList;
    private List<Address> addressesFromCurrent;
    private String location;
    private LatLng latLng;
    private double lat,lng;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mechanics");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recmechList = findViewById(R.id.rec);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recmechList.setLayoutManager(layoutManager);
        mechanicListAdapter = new MechanicListPickUpAdapter(nearMechList, this);
        recmechList.setAdapter(mechanicListAdapter);
     /*   lat = getIntent().getDoubleExtra("lat",0);
        lng = getIntent().getDoubleExtra("lon",0);

        LatLng latLng = new LatLng(lat,lng);
        getLocationAdressFromAutoDetect(latLng);*/
        location = getIntent().getStringExtra("location");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        llNoData = findViewById(R.id.llnodata);
        getMechListFirebase();
    }

    /*private void getMechListFirebase() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Mechanic");
        dialog.show();
        dialog.setCancelable(false);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.cancel();
                Iterable<DataSnapshot> mechanicsListObj = dataSnapshot.getChildren();

                for (DataSnapshot data : mechanicsListObj) {
                    MechanicRegistrationFirebaseTable c = data.getValue(MechanicRegistrationFirebaseTable.class);
                    Log.i("type", "" + c.getEmail());
                    mechList.add(c);
                    Log.i("list-size", "" + mechList.size());

                }
                if (mechList != null)
                    mechanicListAdapter.refreshList(mechList, MechanicPickListActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.cancel();
                Toast.makeText(MechanicPickListActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
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
    private void getMechListFirebase() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Mechanic");
        dialog.show();
        dialog.setCancelable(false);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.cancel();
                Iterable<DataSnapshot> mechanicsListObj = dataSnapshot.getChildren();

                for (DataSnapshot data : mechanicsListObj) {
                    MechanicRegistrationFirebaseTable c = data.getValue(MechanicRegistrationFirebaseTable.class);
                    Log.i("type", "" + c.getEmail());
                    mechList.add(c);
                    Log.i("list-size", "" + mechList.size());

                }

                for (MechanicRegistrationFirebaseTable obj:mechList)
                {
                    if(obj.getLocation().equalsIgnoreCase(location))
                    {
                        nearMechList.add(obj);
                    }
                }
                if(nearMechList.isEmpty())
                {
                    llNoData.setVisibility(View.VISIBLE);
                    recmechList.setVisibility(View.INVISIBLE);
                }
                else
                {
                    llNoData.setVisibility(View.INVISIBLE);
                    recmechList.setVisibility(View.VISIBLE);
                    mechanicListAdapter.refreshList(nearMechList,MechanicPickListActivity.this);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.cancel();
                Toast.makeText(MechanicPickListActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClickMechPickUpItem(MechanicRegistrationFirebaseTable mech) {
        Intent intent = new Intent();
        intent.putExtra("mechName",mech.getUsername());
        intent.putExtra("mechNum",mech.getMobileNumber());
        setResult(133,intent);
        finish();
    }
}
