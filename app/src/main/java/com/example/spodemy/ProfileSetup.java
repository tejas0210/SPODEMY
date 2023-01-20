package com.example.spodemy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.heartbeatinfo.HeartBeatInfo;

public class ProfileSetup extends AppCompatActivity implements MenuItem.OnMenuItemClickListener , View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText edtName2, edtAge, edtHeight, edtWeight, edtCountry, edtState, edtDistrict, edtWaterIntake, edtFoodTracker;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        mAuth = FirebaseAuth.getInstance();

        edtName2 = findViewById(R.id.edtName2);
        edtName2.setText(mAuth.getCurrentUser().getDisplayName());
        edtAge = findViewById(R.id.edtAge);
        edtHeight = findViewById(R.id.edtHeight);
        edtWeight = findViewById(R.id.edtWeight);
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

    }

    public void save(){

        String name = edtName2.getText().toString().trim();
        String age = edtAge.getText().toString().trim();
        String height = edtHeight.getText().toString().trim();
        String weight = edtWeight.getText().toString().trim();
        String country = edtCountry.getText().toString().trim();
        String state = edtState.getText().toString().trim();
        String dist = edtDistrict.getText().toString().trim();
        String waterin = edtWaterIntake.getText().toString().trim();
        String foodTrack = edtFoodTracker.getText().toString().trim();

        Dataholder obj = new Dataholder(name,age, height,weight,country,state,dist,waterin,foodTrack);

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
}