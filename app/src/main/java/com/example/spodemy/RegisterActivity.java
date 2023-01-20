package com.example.spodemy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private EditText edtEmail1, edtPassword1, edtFN, edtLN, edtContact, edtDOB;
    private Button btnRegister1;
    private int year,month,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        edtFN = findViewById(R.id.edtFN);
        edtLN = findViewById(R.id.edtLN);
        edtDOB = findViewById(R.id.edtDOB);
        final Calendar calender = Calendar.getInstance();
        edtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = calender.get(Calendar.YEAR);
                month = calender.get(Calendar.MONTH);
                date = calender.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        edtDOB.setText(SimpleDateFormat.getDateInstance().format(calender.getTime()));
                    }
                },year,month,date);
                datePickerDialog.show();
            }
        });
        edtEmail1 = findViewById(R.id.edtEmail1);
        edtPassword1 = findViewById(R.id.edtPassword1);
        btnRegister1 = findViewById(R.id.btnRegister1);

        btnRegister1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register(){
        mAuth.createUserWithEmailAndPassword(edtEmail1.getText().toString(),edtPassword1.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
//                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().push();
//                            Map<String, Object> values = new HashMap<>();
//                            values.put("Name", edtFN.getText().toString()+" "+edtLN.getText().toString());
//                            values.put("DOB", edtDOB.getText().toString());
//                            values.put("Number", edtContact.getText().toString());
//                            ref.setValue(values);
                                    FirebaseDatabase.getInstance().getReference().child("my_users")
                                    .child(task.getResult().getUser().getUid())
                                    .child("username" )
                                    .setValue(edtFN.getText().toString()+" "+edtLN.getText().toString());

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(edtFN.getText().toString()+" "+edtLN.getText().toString())
                                    .build();

                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                            }
                                        }
                                    });
                            transitionToProfilesetup();
                            Toast.makeText(RegisterActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void transitionToProfilesetup(){
        Intent intent = new Intent(RegisterActivity.this,ProfileSetup.class);
        startActivity(intent);
        finish();
    }

}