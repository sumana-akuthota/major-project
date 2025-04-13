package com.example.mobilehealthinformation.view;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mobilehealthinformation.MainActivity;
import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.util.Session;

public class AdminHome extends AppCompatActivity {

    Button b1,b2,b3,b4,b5;
    Button adminLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        b1=(Button) findViewById(R.id.adminhomelisthospitals);
        b2=(Button) findViewById(R.id.adminhomeaddhealthpolicy);
        b3=(Button) findViewById(R.id.adminhomelisthealthpolicies);
        b4=(Button) findViewById(R.id.adminhomelistpolicyclaimrequests);
        //b5=(Button) findViewById(R.id.adminhomesearchfamilydetails);

        adminLogout=(Button) findViewById(R.id.adminlogout);

        final Session s = new Session(getApplicationContext());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AdminListHospitals.class);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddHealthPolicy.class);
                startActivity(i);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListHealthPolicies.class);
                startActivity(i);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListPolicyClaimRequests.class);
                i.putExtra("aadhaarNo","just");
                startActivity(i);
            }
        });

        /*b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),search_family_details.class);
                startActivity(i);
            }
        });*/

        adminLogout.setOnClickListener(new View.OnClickListener() {
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