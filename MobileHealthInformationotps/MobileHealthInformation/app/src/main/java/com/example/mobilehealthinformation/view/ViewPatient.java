package com.example.mobilehealthinformation.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.Patient;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ViewPatient extends AppCompatActivity {

    TextView t1, t2, t3, t4, t5, t6;
    Button back, sendRequest, viewRequests, uploadDocument;
    Uri selectedFileUri;

    private static final int PICK_FILE_REQUEST = 1;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);

        back = findViewById(R.id.viewpatientback);
        sendRequest = findViewById(R.id.viewpatientsendrequest);
        viewRequests = findViewById(R.id.viewpatientviewclaimrequests);
        uploadDocument = findViewById(R.id.viewpatientuploaddocument);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        final String patientId = (extras != null) ? extras.getString("patient") : null;

        t1 = findViewById(R.id.viewpatientaadhaarno);
        t2 = findViewById(R.id.viewpatientpatientname);
        t3 = findViewById(R.id.viewpatientage);
        t4 = findViewById(R.id.viewpatientgender);
        t5 = findViewById(R.id.viewpatientpatientcontactno);
        t6 = findViewById(R.id.viewpatientaddress);

        final Session session = new Session(getApplicationContext());
        if (!session.getRole().equals("hospital")) {
            sendRequest.setEnabled(false);
        }

        // Initialize Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference("patient_documents");
        storageReference = FirebaseStorage.getInstance().getReference("documents");

        // Fetch patient data from Firebase Realtime Database
        if (patientId != null) {
            //DAO.getDBReference(Constants.PATIENT_DB)
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("patients");
            databaseReference1.child(patientId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Patient patient = dataSnapshot.getValue(Patient.class);
                    if (patient != null) {
                        t1.setText("Patient Aadhaar NO: " + patient.getAadhaarNo());
                        t2.setText("Patient Name: " + patient.getName());
                        t3.setText("Patient Age: " + patient.getAge());
                        t4.setText("Patient Gender: " + patient.getGender());
                        t5.setText("Patient Mobile: " + patient.getContactNo());
                        t6.setText("Patient Address: " + patient.getAddress());

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ViewPatient.this, "Failed to load patient details!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Error: Patient ID is missing!", Toast.LENGTH_SHORT).show();
        }

        // Navigate back based on role
        back.setOnClickListener(view -> navigateBack(session.getRole()));

        // Send Request to Health Policies
        sendRequest.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ListHealthPolicies.class);
            intent.putExtra("patient", patientId);
            startActivity(intent);
        });

        // View Claim Requests
        viewRequests.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ListPolicyClaimRequests.class);
            intent.putExtra("aadhaarNo", patientId);
            startActivity(intent);
        });

        // Upload document button
        uploadDocument.setOnClickListener(view -> openFileChooser());
    }

    // Navigate back to the correct home screen based on user role
    private void navigateBack(String role) {
        Intent intent;
        if ("admin".equals(role)) {
            intent = new Intent(getApplicationContext(), AdminHome.class);
        } else if ("hospital".equals(role)) {
            intent = new Intent(getApplicationContext(), HospitalHome.class);
        } else {
            intent = new Intent(getApplicationContext(), PatientHome.class);
        }
        startActivity(intent);
    }

    // Open file chooser to select a document
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    // Handle file selection and upload to Firebase
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedFileUri = data.getData();
            uploadFileToFirebase();
        }
    }

    // Upload file to Firebase Storage
    private void uploadFileToFirebase() {
        if (selectedFileUri != null) {
            String fileName = System.currentTimeMillis() + getFileExtension(selectedFileUri);
            StorageReference fileRef = storageReference.child(fileName);
            fileRef.putFile(selectedFileUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String fileUrl = uri.toString();
                        saveFileUrlToDatabase(fileUrl);
                    }))
                    .addOnFailureListener(e -> Toast.makeText(ViewPatient.this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    // Get file extension from Uri
    private String getFileExtension(Uri uri) {
        String path = uri.getPath();
        if (path != null && path.contains(".")) {
            return path.substring(path.lastIndexOf("."));
        }
        return ".pdf"; // Default fallback extension
    }

    // Save file URL to Firebase Realtime Database
    private void saveFileUrlToDatabase(String fileUrl) {
        String documentId = databaseReference.push().getKey();
        String patientAadhaar = t1.getText().toString().replace("Patient Aadhaar NO: ", "");

        Map<String, Object> documentData = new HashMap<>();
        documentData.put("patientId", patientAadhaar);
        documentData.put("fileUrl", fileUrl);
        documentData.put("timestamp", System.currentTimeMillis());

        databaseReference.child(documentId).setValue(documentData)
                .addOnSuccessListener(aVoid -> Toast.makeText(ViewPatient.this, "File uploaded & saved!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ViewPatient.this, "Failed to save file info!", Toast.LENGTH_SHORT).show());
    }
}
