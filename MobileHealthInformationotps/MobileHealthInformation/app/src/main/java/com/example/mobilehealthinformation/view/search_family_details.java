package com.example.mobilehealthinformation.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilehealthinformation.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class search_family_details extends AppCompatActivity {

    private EditText etHealthCardId;
    private Button btnSearch;
    private TextView tvPatientDetails, tvFamilyDetails;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_family_details);

        etHealthCardId = findViewById(R.id.et_health_card_id);
        btnSearch = findViewById(R.id.btn_search);
        tvPatientDetails = findViewById(R.id.tv_patient_details);
        tvFamilyDetails = findViewById(R.id.tv_family_details);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("PatientsFamily");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String healthCardId = etHealthCardId.getText().toString().trim();
                if (!healthCardId.isEmpty()) {
                    fetchFamilyDetails(healthCardId);
                } else {
                    Toast.makeText(search_family_details.this, "Enter a valid Health Card ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchFamilyDetails(String healthCardId) {
        databaseReference.child(healthCardId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Fetch Patient Details
                    DataSnapshot patientSnapshot = snapshot.child("patientDetails");
                    String patientInfo = "Patient Details:\n";
                    if (patientSnapshot.exists()) {
                        patientInfo += "Aadhaar: " + patientSnapshot.child("aadhaarNo").getValue(String.class) + "\n" +
                                "Name: " + patientSnapshot.child("name").getValue(String.class) + "\n" +
                                "Age: " + patientSnapshot.child("age").getValue(Integer.class) + "\n" +
                                "Gender: " + patientSnapshot.child("gender").getValue(String.class) + "\n" +
                                "Contact: " + patientSnapshot.child("contactNo").getValue(String.class) + "\n" +
                                "Address: " + patientSnapshot.child("address").getValue(String.class) + "\n";
                    }

                    // Fetch Family Details
                    DataSnapshot familySnapshot = snapshot.child("familyDetails");
                    StringBuilder familyInfo = new StringBuilder("Family Members:\n");
                    if (familySnapshot.exists()) {
                        for (DataSnapshot memberSnapshot : familySnapshot.getChildren()) {
                            Map<String, Object> familyMember = (Map<String, Object>) memberSnapshot.getValue();
                            familyInfo.append("Name: ").append(familyMember.get("name")).append("\n")
                                    .append("Aadhaar: ").append(familyMember.get("aadhaarNo")).append("\n")
                                    .append("Age: ").append(familyMember.get("age")).append("\n")
                                    .append("Gender: ").append(familyMember.get("gender")).append("\n\n");
                        }
                    } else {
                        familyInfo.append("No family members found.");
                    }

                    tvPatientDetails.setText(patientInfo);
                    tvFamilyDetails.setText(familyInfo.toString());
                } else {
                    tvPatientDetails.setText("");
                    tvFamilyDetails.setText("");
                    new AlertDialog.Builder(search_family_details.this)
                            .setMessage("No data found for this Health Card ID.")
                            .setPositiveButton("OK",null)
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(search_family_details.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
