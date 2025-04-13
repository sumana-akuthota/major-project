package com.example.mobilehealthinformation.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.ClaimRequest;
import com.example.mobilehealthinformation.form.HealthPolicy;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class HospitalViewHealthPolicy extends AppCompatActivity {

    TextView t1,t2,t3;
    EditText description;

    Button back;
    Button send;
    private DatabaseReference databaseReference;
    String patient;
    int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_health_policy);

        back=(Button) findViewById(R.id.hospitalviewhealthpolicyback);
        send=(Button) findViewById(R.id.hospitalviewhealthpolicysendrequest);
        description=(EditText) findViewById(R.id.hospitalviewhealthpolicyclaimrequestdescription);

        Intent i=getIntent();
        savedInstanceState=i.getExtras();

        final String healthpolicy=savedInstanceState.getString("healthpolicy");
        patient=savedInstanceState.getString("patient");

        t1=(TextView) findViewById(R.id.hospitalviewhealthpolicyname);
        t2=(TextView)findViewById(R.id.hospitalviewhealthpolicydescription);
        t3=(TextView) findViewById(R.id.hospitalviewhealthpolicycoverageamount);

        final Session session=new Session(getApplicationContext());

        databaseReference = FirebaseDatabase.getInstance().getReference();

        calculateTotalClaimedAmount();

        DAO.getDBReference(Constants.HEALTH_POLICY_DB).child(healthpolicy).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HealthPolicy healthpolicy=dataSnapshot.getValue(HealthPolicy.class);

                if(healthpolicy!=null)
                {
                    t1.setText("Health Policy Name:"+healthpolicy.getPolicyName());
                    t2.setText("Description:"+healthpolicy.getDescription());
                    t3.setText("Coverage Amount:"+healthpolicy.getCoverageAmount());
                    //amount= (int) healthpolicy.getCoverageAmount();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String role= session.getRole();

                if(role.equals("admin"))
                {
                    Intent i=new Intent(getApplicationContext(), AdminHome.class);
                    startActivity(i);
                }
                else if(role.equals("hospital"))
                {
                    Intent i=new Intent(getApplicationContext(),HospitalHome.class);
                    startActivity(i);
                }
                else if(role.equals("patient"))
                {
                    Intent i=new Intent(getApplicationContext(),PatientHome.class);
                    startActivity(i);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClaimRequest claimRequest=new ClaimRequest();

                claimRequest.setHospitalId(session.getusername());
                claimRequest.setPatientAadhaarNo(patient);
                claimRequest.setStatus("Pending");
                claimRequest.setPolicyId(healthpolicy);
                claimRequest.setClaimId(new DAO().getUnicKey(Constants.CLAIM_REQUEST_DB));
                claimRequest.setDescription(description.getText().toString());

                //Toast.makeText(getApplicationContext(),"Amount Covered: "+amount,Toast.LENGTH_LONG).show();

                String aa = t3.getText().toString();
                int total = aa.indexOf(":");
                String substring = aa.substring(total+1,aa.length()-2).toLowerCase();

                //Toast.makeText(HospitalViewHealthPolicy.this, ""+substring, Toast.LENGTH_SHORT).show();

                int am = Integer.parseInt(substring);

                if(am>=200000)
                {
                    //Toast.makeText(HospitalViewHealthPolicy.this, "a"+amount, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Patient Reached to Maximum Amount Limit",Toast.LENGTH_LONG).show();
                }
                else {

                    final DAO dao = new DAO();
                    dao.addObject(Constants.CLAIM_REQUEST_DB, claimRequest, claimRequest.getClaimId());

                    Intent i = new Intent(getApplicationContext(), HospitalHome.class);
                    startActivity(i);

                    //Toast.makeText(HospitalViewHealthPolicy.this, "ea"+amount, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void calculateTotalClaimedAmount() {
        databaseReference.child("claimrequests").orderByChild("patientAadhaarNo").equalTo(patient)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, Integer> policyAmounts = new HashMap<>();

                        for (DataSnapshot claimSnapshot : snapshot.getChildren()) {
                            String policyId = claimSnapshot.child("policyId").getValue(String.class);

                            if (policyId != null) {
                                policyAmounts.put(policyId, 0); // Initialize with zero
                            }
                        }

                        //fetchPolicyAmounts(policyAmounts);

                        if (!policyAmounts.isEmpty()) {
                            fetchPolicyAmounts(policyAmounts);
                        } else {
                           amount=0;
                            //Toast.makeText(HospitalViewHealthPolicy.this, "set to 0", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.v("Error: " ,error.getMessage());
                    }
                });
    }

    private void fetchPolicyAmounts(Map<String, Integer> policyAmounts) {
        databaseReference.child("healthpolicies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalAmount = 0;
                for (String policyId : policyAmounts.keySet()) {
                    if (snapshot.hasChild(policyId)) {
                        int amount = snapshot.child(policyId).child("coverageAmount").getValue(Integer.class);
                        totalAmount += amount;
                    }
                }
                amount=totalAmount;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("Error: ",error.getMessage());
            }
        });
    }
}
