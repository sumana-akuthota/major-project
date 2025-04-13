package com.example.mobilehealthinformation.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
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

public class HospitalViewPolicyClaimRequest extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14;

    Button back;
    Button addhealthrecords;
    Button viewhealthrecords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_view_policy_claim_request);

        back=(Button) findViewById(R.id.hospitalviewpolicyclaimrequestback);
        addhealthrecords=(Button) findViewById(R.id.hospitalviewpolicyclaimrequestaddhealthrecord);
        viewhealthrecords=(Button) findViewById(R.id.hospitalviewpolicyclaimrequestviewhealthrecords);


        Intent i=getIntent();
        savedInstanceState=i.getExtras();

        final String claimRequest=savedInstanceState.getString("claimrequest");

        t1=(TextView) findViewById(R.id.hospitalviewpolicyclaimrequestaadhaarno);
        t2=(TextView)findViewById(R.id.hospitalviewpolicyclaimrequestpatientname);
        t3=(TextView) findViewById(R.id.hospitalviewpolicyclaimrequestage);
        t4=(TextView) findViewById(R.id.hospitalviewpolicyclaimrequestgender);

        t5=(TextView) findViewById(R.id.hospitalviewpolicyclaimrequestpatientcontactno);
        t6=(TextView)findViewById(R.id.hospitalviewpolicyclaimrequestaddress);
        t7=(TextView) findViewById(R.id.hospitalviewpolicyclaimrequesthospitalname);
        t8=(TextView) findViewById(R.id.hospitalviewpolicyclaimrequestlocation);

        t9=(TextView) findViewById(R.id.hospitalviewpolicyclaimrequesthospitalcontactno);
        t10=(TextView)findViewById(R.id.hospitalviewpolicyclaimrequestpolicyname);
        t11=(TextView) findViewById(R.id.hospitalviewpolicyclaimrequestpolicydescription);
        t12=(TextView) findViewById(R.id.hospitalviewpolicyclaimrequestcoverageamount);

        t13=(TextView) findViewById(R.id.hospitalviewpolicyclaimrequestdescription);
        t14=(TextView)findViewById(R.id.hospitalviewpolicyclaimrequeststatus);

        final Session session=new Session(getApplicationContext());

        DAO.getDBReference(Constants.CLAIM_REQUEST_DB).child(claimRequest).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ClaimRequest claimRequest=dataSnapshot.getValue(ClaimRequest.class);

                if(claimRequest!=null)
                {
                    DAO.getDBReference(Constants.PATIENT_DB).child(claimRequest.getPatientAadhaarNo()).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Patient patient=dataSnapshot.getValue(Patient.class);

                            if(patient!=null)
                            {
                                t1.setText("Patient Aadhaar NO:"+patient.getAadhaarNo());
                                t2.setText("Patient Name:"+patient.getName());
                                t3.setText("Patient Age:"+patient.getAge());
                                t4.setText("Patient Gender:"+patient.getGender());
                                t5.setText("Patient Mobile:"+patient.getContactNo());
                                t6.setText("Patient Address:"+patient.getAddress());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    DAO.getDBReference(Constants.HOSPITAL_DB).child(claimRequest.getHospitalId()).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Hospital hospital=dataSnapshot.getValue(Hospital.class);

                            if(hospital!=null)
                            {
                                t7.setText("Hospital Name:"+hospital.getName());
                                t8.setText("Hospital Mobile:"+hospital.getContactNo());
                                t9.setText("Hospital Address:"+hospital.getLocation());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    DAO.getDBReference(Constants.HEALTH_POLICY_DB).child(claimRequest.getPolicyId()).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            HealthPolicy healthpolicy=dataSnapshot.getValue(HealthPolicy.class);

                            if(healthpolicy!=null)
                            {
                                t10.setText("Policy Name:"+healthpolicy.getPolicyName());
                                t11.setText("Policy Description:"+healthpolicy.getDescription());
                                t12.setText("Coverage Amount:"+healthpolicy.getCoverageAmount());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    t13.setText("Request Description:"+claimRequest.getDescription());
                    t14.setText("Request Status:"+claimRequest.getStatus());
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

        addhealthrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(), AddHealthRecord.class);
                i.putExtra("claimrequest", claimRequest);
                startActivity(i);
            }
        });

        viewhealthrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getApplicationContext(), ListHealthRecords.class);
                i.putExtra("claimrequest", claimRequest);
                startActivity(i);
            }
        });
    }
}
