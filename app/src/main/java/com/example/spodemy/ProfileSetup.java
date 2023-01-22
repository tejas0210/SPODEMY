package com.example.spodemy;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.heartbeatinfo.HeartBeatInfo;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProfileSetup extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, View.OnClickListener, LocationListener {

    private FirebaseAuth mAuth;
    private EditText edtName2, edtAge, edtHeight, edtWeight, edtCountry, edtState, edtDistrict, edtWaterIntake, edtFoodTracker;
    private TextView edtBMI;
    private Button btnSave;
    private LocationManager locationManager;
    private String gender1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

//        int ageYear = getIntent().getIntExtra("ageyear", 1);
//        int ageY = Integer.parseInt(ageYear);
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);

        mAuth = FirebaseAuth.getInstance();

        edtName2 = findViewById(R.id.edtName2);
        edtAge = findViewById(R.id.edtAge);
        edtHeight = findViewById(R.id.edtHeight);
        edtWeight = findViewById(R.id.edtWeight);
        edtBMI = findViewById(R.id.edtBMI);
        edtCountry = findViewById(R.id.edtCountry);
        edtState = findViewById(R.id.edtState);
        edtDistrict = findViewById(R.id.edtDistrict);
        edtWaterIntake = findViewById(R.id.edtWaterIntake);
        edtFoodTracker = findViewById(R.id.edtFoodTracker);


        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        edtBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(edtHeight.getText().toString() == "" && edtWeight.getText().toString()==""){
//
//                }
//                else{
//
//                }
                try {
                    float h = Float.parseFloat(edtHeight.getText().toString());
                    int w = Integer.parseInt(edtWeight.getText().toString());
                    float bmi = w/(h*h);
                    edtBMI.setText(String.valueOf(bmi));
                }
                catch (Exception e){
                    Toast.makeText(ProfileSetup.this,"Enter height and weight",Toast.LENGTH_SHORT).show();
                }
            }
        });

        FirebaseDatabase.getInstance().getReference().child("my_users").child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().exists()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    String name = String.valueOf(dataSnapshot.child("name").getValue());
                    edtName2.setText(name);
                    String age = String.valueOf(dataSnapshot.child("age").getValue());
                    edtAge.setText(age);
                    gender1 = String.valueOf(dataSnapshot.child("gender").getValue());
                }
            }
        });

        grantUriPermission();
        checkLoctaionEnabledOrNot();
        getLocation();
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, this);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void checkLoctaionEnabledOrNot() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(!gpsEnabled && !networkEnabled){
            new AlertDialog.Builder(ProfileSetup.this).setTitle("Enable GPS Service").setCancelable(false).setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                       startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }).setNegativeButton("Cancel",null).show();
        }
    }

    private void grantUriPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);

        }
    }

    public void save(){

        String name = edtName2.getText().toString().trim();
        String age = edtAge.getText().toString().trim();
        String height = edtHeight.getText().toString().trim();
        String weight = edtWeight.getText().toString().trim();
        String gender = gender1;
        String bmi = edtBMI.getText().toString().trim();
        String country = edtCountry.getText().toString().trim();
        String state = edtState.getText().toString().trim();
        String dist = edtDistrict.getText().toString().trim();
        String waterin = edtWaterIntake.getText().toString().trim();
        String foodTrack = edtFoodTracker.getText().toString().trim();

        Dataholder obj = new Dataholder(name,age, height, weight, gender, bmi,country,state,dist,waterin,foodTrack);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference node = db.getReference("my_users");

        node.child(mAuth.getUid()).setValue(obj);

        Toast.makeText(ProfileSetup.this,"Saved successful",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profilesetup_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        mAuth.signOut();
        transitionToMainActivity();
    }
    private void transitionToMainActivity(){
        Intent intent = new Intent(ProfileSetup.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            edtCountry.setText(addresses.get(0).getCountryName());
            edtState.setText(addresses.get(0).getAdminArea());
            edtDistrict.setText(addresses.get(0).getLocality());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }


}