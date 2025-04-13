package com.example.mobilehealthinformation.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mobilehealthinformation.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class family_details extends AppCompatActivity {

    private EditText familyAadhaar1, familyName1, familyAge1, familyGender1;
    private EditText familyAadhaar2, familyName2, familyAge2, familyGender2;
    private Button submitButton;
    private DatabaseReference databaseReference,databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details);

        databaseReference = FirebaseDatabase.getInstance().getReference("PatientsFamily");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("patients");

        familyAadhaar1 = findViewById(R.id.addpatient1aadhaarnumber);
        familyName1 = findViewById(R.id.addpatient1name);
        familyAge1 = findViewById(R.id.addpatient1age);
        familyGender1 = findViewById(R.id.addpatient1gender);

        familyAadhaar2 = findViewById(R.id.addpatient2aadhaarnumber);
        familyName2 = findViewById(R.id.addpatient2name);
        familyAge2 = findViewById(R.id.addpatient2age);
        familyGender2 = findViewById(R.id.addpatient2gender);

        submitButton = findViewById(R.id.addpatientsubmitbutton);

        final Intent intent = getIntent();
        final String healthCardNumber = intent.getStringExtra("healthCardNumber");

        final Map<String, Object> patientData = new HashMap<>();
        patientData.put("aadhaarNo", intent.getStringExtra("aadhaarNumber"));
        patientData.put("name", intent.getStringExtra("patientName"));
        patientData.put("age", Integer.parseInt(intent.getStringExtra("patientAge")));
        patientData.put("gender", intent.getStringExtra("patientGender"));
        patientData.put("contactNo", intent.getStringExtra("patientContact"));
        patientData.put("address", intent.getStringExtra("patientAddress"));
        patientData.put("password", intent.getStringExtra("password"));

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> familyData = new HashMap<>();

                Map<String, Object> family1 = new HashMap<>();
                family1.put("aadhaarNo", familyAadhaar1.getText().toString());
                family1.put("name", familyName1.getText().toString());
                family1.put("age", familyAge1.getText().toString());
                family1.put("gender", familyGender1.getText().toString());

                Map<String, Object> family2 = new HashMap<>();
                family2.put("aadhaarNo", familyAadhaar2.getText().toString());
                family2.put("name", familyName2.getText().toString());
                family2.put("age", familyAge2.getText().toString());
                family2.put("gender", familyGender2.getText().toString());

                familyData.put("familyMember1", family1);
                familyData.put("familyMember2", family2);

                Map<String, Object> finalData = new HashMap<>();
                //finalData.put("patientDetails", patientData);
                finalData.put("familyDetails", familyData);

                databaseReference.child(healthCardNumber).setValue(finalData)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                databaseReference2.child(healthCardNumber).setValue(patientData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(family_details.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(family_details.this, HospitalHome.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(family_details.this, "Failed to Save Data", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(family_details.this, "Failed to Save Data", Toast.LENGTH_SHORT).show();
                            }
                        });




            }
        });
    }
}
