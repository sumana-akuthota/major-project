package com.example.mobilehealthinformation.view;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.Patient;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PatientLogin extends AppCompatActivity {

    private Session session;
    EditText e1,e2;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getApplicationContext());

        setContentView(R.layout.activity_patient_login);

        e1=(EditText)findViewById(R.id.patientloginusername);
        e2=(EditText)findViewById(R.id.patientloginpassword);
        b1=(Button)findViewById(R.id.patientloginsubmitbutton);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username=e1.getText().toString();
                final String password=e2.getText().toString();

                if (username.equals("admin") && password.equals("admin")) {

                    session.setusername("admin");
                    session.setRole("admin");

                    Intent i = new Intent(getApplicationContext(), AdminHome.class);
                    startActivity(i);

                } else {

                    DAO.getDBReference(Constants.PATIENT_DB).child(username).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Patient patient = (Patient) dataSnapshot.getValue(Patient.class);

                            if (patient == null) {
                                Toast.makeText(getApplicationContext(), "Invalid UserName ", Toast.LENGTH_SHORT).show();
                            } else if (patient.getPassword().equals(password)) {

                                session.setusername(patient.getAadhaarNo());
                                session.setRole("patient");

                                Intent i = new Intent(getApplicationContext(),PatientHome.class);
                                i.putExtra("aadhaar",username);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "In valid Password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}