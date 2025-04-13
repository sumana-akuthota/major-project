package com.example.mobilehealthinformation;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mobilehealthinformation.view.AddPatient;
import com.example.mobilehealthinformation.view.HospitalLogin;
import com.example.mobilehealthinformation.view.HospitalRegistration;
import com.example.mobilehealthinformation.view.PatientLogin;

public class MainActivity extends AppCompatActivity{

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.mainactivityhospitalregistration);
        Button b2 = (Button) findViewById(R.id.mainactivityhospitallogin);
        Button b3 = (Button) findViewById(R.id.mainactivitypatientlogin);
        //Button b4 = (Button) findViewById(R.id.addPatientBtn);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HospitalRegistration.class);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HospitalLogin.class);
                startActivity(i);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PatientLogin.class);
                startActivity(i);
            }
        });



    }
}