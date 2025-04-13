package com.example.mobilehealthinformation.view;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mobilehealthinformation.MainActivity;
import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.util.Session;

public class PatientHome extends AppCompatActivity {

    Button b1;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        b1=(Button) findViewById(R.id.patienthomelistpolicyclaimrequests);
        logout=(Button) findViewById(R.id.patientlogout);

        final Session s = new Session(getApplicationContext());

        String aadhar = getIntent().getStringExtra("aadhaar").toString();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListPolicyClaimRequests.class);
                i.putExtra("aadhaarNo",aadhar);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s.loggingOut();
                s.setRole("");
                s.setusername("");
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}