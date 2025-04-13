package com.example.mobilehealthinformation.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mobilehealthinformation.MainActivity;
import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.util.Session;

public class HospitalHome extends AppCompatActivity {

    Button b1,b2,familySearch;
    Button logout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_home);

        b1=(Button) findViewById(R.id.hospitalhomeaddpatient);
        b2=(Button) findViewById(R.id.hospitalhomesearchpatient);
        familySearch=(Button) findViewById(R.id.hospitalhomesearchfamily);

        logout=(Button) findViewById(R.id.hospitallogout);

        final Session s = new Session(getApplicationContext());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddPatient.class);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),SearchPatient.class);
                startActivity(i);
            }
        });

        familySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),FamilySearchInPatient.class);
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
                finish();
            }
        });
    }
}