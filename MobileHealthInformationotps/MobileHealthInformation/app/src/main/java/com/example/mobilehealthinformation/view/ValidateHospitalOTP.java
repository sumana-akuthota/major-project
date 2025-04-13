package com.example.mobilehealthinformation.view;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;

import com.example.mobilehealthinformation.form.Hospital;
import com.example.mobilehealthinformation.form.Hospital2;
import com.example.mobilehealthinformation.util.Constants;

public class ValidateHospitalOTP extends AppCompatActivity {

    EditText e1;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_hospital_otp);

        e1=(EditText)findViewById(R.id.hospitalotp);
        b1=(Button)findViewById(R.id.hospitalotpsubmit);

        Intent i=getIntent();
        savedInstanceState=i.getExtras();

        final String serverotp=savedInstanceState.getString("hospitalotp");
        final String data=savedInstanceState.getString("hospitaldata");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String hospitalotp=e1.getText().toString();

                Toast.makeText(getApplicationContext(),hospitalotp+" "+serverotp,Toast.LENGTH_LONG).show();

                if(hospitalotp.equals(serverotp)) {

                    String[] tokens = data.split("_");

                    Hospital hospital = new Hospital();

                    hospital.setHospitalId(tokens[0]);
                    hospital.setName(tokens[1]);
                    hospital.setLocation(tokens[2]);
                    hospital.setContactNo(tokens[3]);
                    hospital.setPassword(tokens[4]);

                    DAO dao = new DAO();

                    try {

                        dao.addObject(Constants.HOSPITAL_DB, hospital,hospital.getHospitalId());
                        Toast.makeText(getApplicationContext(), "Registred Successfully", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(),HospitalLogin.class);
                        startActivity(i);
                        finish();

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                        Log.v("Hospital Adding  Ex", ex.toString());
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
