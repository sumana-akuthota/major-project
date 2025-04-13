package com.example.mobilehealthinformation.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.Patient;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.MapUtil;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchPatient extends AppCompatActivity {

    ListView listView;
    EditText aadhaarno;
    Button search;
    private TextView tvPatientDetails, tvFamilyDetails;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_patient);

        aadhaarno=(EditText) findViewById(R.id.searchpatientaadhaarno);
        listView=(ListView) findViewById(R.id.SearchPatientList);

        //tvPatientDetails = findViewById(R.id.tv_patient_details1);
        //tvFamilyDetails = findViewById(R.id.tv_family_details1);


        final Map<String,String> viewMap=new HashMap<String,String>();

        search=(Button) findViewById(R.id.searchpatientsearchbutton);

        final Session s=new Session(getApplicationContext());

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("patients");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> al = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    //Patient patient = dataSnapshot1.getValue(Patient.class);
                    al.add(key);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(listView.getContext(),
                        android.R.layout.simple_list_item_1, al);

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String aadher = aadhaarno.getText().toString();
                if (!aadher.isEmpty()) {
                    fetchFamilyDetails(aadher);
                } else {
                    Toast.makeText(SearchPatient.this, "Enter a valid Health Card ID", Toast.LENGTH_SHORT).show();
                }

                /*//DAO.getDBReference(Constants.PATIENT_DB)
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patients");
                databaseReference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ArrayList<String> al = new ArrayList<>();

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            String key = dataSnapshot1.getKey();
                            Patient patient = dataSnapshot1.getValue(Patient.class);
                            al.add(key);
                        }



                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listView.getContext(),
                                android.R.layout.simple_list_item_1, al);

                        listView.setAdapter(adapter);

                        *//*int j = 1;

                        for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {

                            String key = snapshotNode.getKey();
                            Patient patient = snapshotNode.getValue(Patient.class);

                            if (patient != null) {
                                // Check if the attributes are null before logging them
                                Log.v("aadhaarNo:", patient.getAadhaarNo() != null ? patient.getAadhaarNo() : "null" + "\t" + aadhaarno.getText().toString());
                                Log.v("name:", patient.getName() != null ? patient.getName() : "null");
                                Log.v("age:", patient.getAge()+"" != null ? patient.getAge()+"" : "null");
                                Log.v("gender:", patient.getGender() != null ? patient.getGender() : "null");
                                Log.v("contactNo:", patient.getContactNo() != null ? patient.getContactNo() : "null");
                                Log.v("address:", patient.getAddress() != null ? patient.getAddress() : "null");
                                Log.v("password:", patient.getPassword() != null ? patient.getPassword() : "null");

                                if (patient.getAadhaarNo() != null && patient.getAadhaarNo().equals(aadhaarno.getText().toString())) {
                                    viewMap.put(j + "_" + patient.getName(), key);
                                    j++;
                                }
                            }
                        }

                        ArrayList<String> al = new ArrayList<String>(viewMap.keySet());

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listView.getContext(),
                                android.R.layout.simple_list_item_1, (al.toArray(new String[al.size()])));

                        listView.setAdapter(adapter);

                        final Session s = new Session(getApplicationContext());
                        s.setViewMap(MapUtil.mapToString(viewMap));*//*
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });*/
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String item = listView.getItemAtPosition(i).toString();
                //String id= MapUtil.stringToMap(s.getViewMap()).get(item);

                Intent intent = new Intent(getApplicationContext(), ViewPatient.class);
                intent.putExtra("patient", item);
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("patients");

    }

    private void fetchFamilyDetails(String healthCardId) {
        databaseReference.child(healthCardId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Fetch Patient Details
                    //DataSnapshot patientSnapshot = snapshot.child("patientDetails");
                    String patientInfo = "Patient Details:\n";
                    if (snapshot.exists()) {
                        patientInfo += "Aadhaar: " + snapshot.child("aadhaarNo").getValue(String.class) + "\n" +
                                "Name: " + snapshot.child("name").getValue(String.class) + "\n" +
                                "Age: " + snapshot.child("age").getValue(Integer.class) + "\n" +
                                "Gender: " + snapshot.child("gender").getValue(String.class) + "\n" +
                                "Contact: " + snapshot.child("contactNo").getValue(String.class) + "\n" +
                                "Address: " + snapshot.child("address").getValue(String.class) + "\n";
                    }

                    // Fetch Family Details
                    /*DataSnapshot familySnapshot = snapshot.child("familyDetails");
                    StringBuilder familyInfo = new StringBuilder("Family Members:\n");
                    if (familySnapshot.exists()) {
                        for (DataSnapshot memberSnapshot : familySnapshot.getChildren()) {
                            Map<String, Object> familyMember = (Map<String, Object>) memberSnapshot.getValue();
                            familyInfo.append("Name: ").append(familyMember.get("name")).append("\n")
                                    .append("Aadhaar: ").append(familyMember.get("aadhaarNumber")).append("\n")
                                    .append("Age: ").append(familyMember.get("age")).append("\n")
                                    .append("Gender: ").append(familyMember.get("gender")).append("\n\n");
                        }
                    } else {
                        familyInfo.append("No family members found.");
                    }*/

                    new AlertDialog.Builder(SearchPatient.this)
                            .setMessage(patientInfo)
                                    .setPositiveButton("OK",null)
                                            .show();

                    //tvPatientDetails.setText(patientInfo);
                    //tvFamilyDetails.setText(familyInfo.toString());
                } else {
                    new AlertDialog.Builder(SearchPatient.this)
                            .setMessage("No data found for this Health Card ID.")
                            .setPositiveButton("OK",null)
                            .show();
                    //tvPatientDetails.setText("");
                    //tvFamilyDetails.setText("No data found for this Health Card ID.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchPatient.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
