package com.example.mobilehealthinformation.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.ClaimRequest;
import com.example.mobilehealthinformation.form.HealthPolicy;
import com.example.mobilehealthinformation.form.Hospital;
import com.example.mobilehealthinformation.form.Patient;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AdminViewPolicyClaimRequest extends AppCompatActivity {

    TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, documentView;
    Button back, approve, reject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_policy_claim_request);

        back = findViewById(R.id.adminviewpolicyclaimrequestback);
        approve = findViewById(R.id.adminviewpolicyclaimrequestapprove);
        reject = findViewById(R.id.adminviewpolicyclaimrequestreject);
        documentView = findViewById(R.id.adminviewpolicyclaimrequestdocument); // Initialize document view

        Intent i = getIntent();
        savedInstanceState = i.getExtras();
        final String claimRequest = savedInstanceState.getString("claimrequest");

        t1 = findViewById(R.id.adminviewpolicyclaimrequestaadhaarno);
        t2 = findViewById(R.id.adminviewpolicyclaimrequestpatientname);
        t3 = findViewById(R.id.adminviewpolicyclaimrequestage);
        t4 = findViewById(R.id.adminviewpolicyclaimrequestgender);
        t5 = findViewById(R.id.adminviewpolicyclaimrequestpatientcontactno);
        t6 = findViewById(R.id.adminviewpolicyclaimrequestaddress);
        t7 = findViewById(R.id.adminviewpolicyclaimrequesthospitalname);
        t8 = findViewById(R.id.adminviewpolicyclaimrequestlocation);
        t9 = findViewById(R.id.adminviewpolicyclaimrequesthospitalcontactno);
        t10 = findViewById(R.id.adminviewpolicyclaimrequestpolicyname);
        t11 = findViewById(R.id.adminviewpolicyclaimrequestpolicydescription);
        t12 = findViewById(R.id.adminviewpolicyclaimrequestcoverageamount);
        t13 = findViewById(R.id.adminviewpolicyclaimrequestdescription);
        t14 = findViewById(R.id.adminviewpolicyclaimrequeststatus);


        final Session session = new Session(getApplicationContext());

        if (!session.getusername().equals("admin")) {
            approve.setEnabled(false);
            reject.setEnabled(false);
        }

        // Fetch Claim Request Details
        DAO.getDBReference(Constants.CLAIM_REQUEST_DB).child(claimRequest).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ClaimRequest claimRequest = dataSnapshot.getValue(ClaimRequest.class);

                if (claimRequest != null) {
                    // Fetch Patient Data
                    DAO.getDBReference(Constants.PATIENT_DB).child(claimRequest.getPatientAadhaarNo()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        }
                    });

                    // Fetch Hospital Data
                    DAO.getDBReference(Constants.HOSPITAL_DB).child(claimRequest.getHospitalId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Hospital hospital = dataSnapshot.getValue(Hospital.class);
                            if (hospital != null) {
                                t7.setText("Hospital Name: " + hospital.getName());
                                t8.setText("Hospital Location: " + hospital.getLocation());
                                t9.setText("Hospital Contact: " + hospital.getContactNo());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    // Fetch Policy Data
                    DAO.getDBReference(Constants.HEALTH_POLICY_DB).child(claimRequest.getPolicyId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HealthPolicy policy = dataSnapshot.getValue(HealthPolicy.class);
                            if (policy != null) {
                                t10.setText("Policy Name: " + policy.getPolicyName());
                                t11.setText("Policy Description: " + policy.getDescription());
                                t12.setText("Coverage Amount: " + policy.getCoverageAmount());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    t13.setText("Request Description: " + claimRequest.getDescription());
                    t14.setText("Request Status: " + claimRequest.getStatus());

                    // Show Document if available
                    if (claimRequest.getDocumentUrl() != null && !claimRequest.getDocumentUrl().isEmpty()) {
                        documentView.setVisibility(View.VISIBLE);
                        documentView.setOnClickListener(view -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(claimRequest.getDocumentUrl()));
                            startActivity(intent);
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        back.setOnClickListener(view -> {
            String role = session.getRole();
            Intent intent;
            if (role.equals("admin")) {
                intent = new Intent(getApplicationContext(), AdminHome.class);
            } else if (role.equals("hospital")) {
                intent = new Intent(getApplicationContext(), HospitalHome.class);
            } else {
                intent = new Intent(getApplicationContext(), PatientHome.class);
            }
            startActivity(intent);
        });


        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DAO d=new DAO();
                DAO.getDBReference(Constants.CLAIM_REQUEST_DB).child(claimRequest).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ClaimRequest request= dataSnapshot.getValue(ClaimRequest.class);

                        if(request!=null)
                        {
                            request.setStatus("Approved");
                            new DAO().addObject(Constants.CLAIM_REQUEST_DB,request,request.getClaimId());

                            Log.v("Patient Aadhaar no:",request.getPatientAadhaarNo());

                            DAO.getDBReference(Constants.PATIENT_DB).child(request.getPatientAadhaarNo()).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Patient patient=dataSnapshot.getValue(Patient.class);

                                    if(patient!=null)
                                    {
                                        Intent i = new Intent(getApplicationContext(),AdminHome.class);
                                        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, i,PendingIntent.FLAG_IMMUTABLE);

                                        SmsManager sms=SmsManager.getDefault();
                                        sms.sendTextMessage(patient.getContactNo(), null, "Your Policy Request is Approved", pi,null);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DAO d=new DAO();
                DAO.getDBReference(Constants.CLAIM_REQUEST_DB).child(claimRequest).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ClaimRequest request= dataSnapshot.getValue(ClaimRequest.class);

                        if(request!=null)
                        {
                            request.setStatus("Rejected");
                            new DAO().addObject(Constants.CLAIM_REQUEST_DB,request,request.getClaimId());

                            DAO.getDBReference(Constants.PATIENT_DB).child(request.getPatientAadhaarNo()).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Patient patient=dataSnapshot.getValue(Patient.class);

                                    if(patient!=null)
                                    {
                                        Intent i = new Intent(getApplicationContext(),AdminHome.class);
                                        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, i,PendingIntent.FLAG_IMMUTABLE);

                                        SmsManager sms=SmsManager.getDefault();
                                        sms.sendTextMessage(patient.getContactNo(), null, "Your Policy Request is Rejected", pi,null);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
