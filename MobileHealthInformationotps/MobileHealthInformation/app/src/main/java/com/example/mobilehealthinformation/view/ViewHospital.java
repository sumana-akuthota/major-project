package com.example.mobilehealthinformation.view;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.HealthRecord;
import com.example.mobilehealthinformation.form.Hospital;
import com.example.mobilehealthinformation.form.Hospital2;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewHospital extends AppCompatActivity {

    TextView t1,t2,t3,t4;

    Button back;
    Button update;

    String hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hospital);

        back=(Button) findViewById(R.id.viewhospitalback);
        update=(Button) findViewById(R.id.viewhospitalupdatestatus);

        Intent i=getIntent();
        savedInstanceState=i.getExtras();

        hospital=savedInstanceState.getString("hospital");

        t1=(TextView) findViewById(R.id.viewhospitalname);
        t2=(TextView)findViewById(R.id.viewhospitallocation);
        t3=(TextView) findViewById(R.id.viewhospitalcontact);
        t4=(TextView) findViewById(R.id.viewhospitaltisactive);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 100);
            }
        }

        final Session session=new Session(getApplicationContext());

        if(!session.getusername().equals("admin"))
        {
            update.setEnabled(false);
        }

        getTheData();

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

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("hospitals").child(hospital);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DAO d=new DAO();
                //DAO.getDBReference(Constants.HOSPITAL_DB)
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Hospital hospital  =dataSnapshot.getValue(Hospital.class);

                        if(hospital!=null)
                        {
                            if(hospital.isActive()==true)
                            {
                                //SmsManager sms=SmsManager.getDefault();
                                //sms.sendTextMessage(hospital.getContactNo(), null, "Your Account Status is Updated to false", null,null);
                                databaseReference.child("active").setValue(false);
                                getTheData();
                                Toast.makeText(ViewHospital.this, "Your Account Status is Updated to false", Toast.LENGTH_SHORT).show();
                                //hospital.setActive(false);
                                //ViewHospital.this.recreate();
                                sendSMS(hospital.getContactNo());

                            }
                            else {
                                //SmsManager sms=SmsManager.getDefault();
                                //sms.sendTextMessage(hospital.getContactNo(), null, "Your Account Status is Updated to true", null,null);
                                databaseReference.child("active").setValue(true);
                                getTheData();
                                Toast.makeText(ViewHospital.this, "Your Account Status is Updated to true", Toast.LENGTH_SHORT).show();
                                //hospital.setActive(true);
                                //ViewHospital.this.recreate();
                                sendSMS(hospital.getContactNo());

                            }

                            /*new DAO().addObject(Constants.HOSPITAL_DB, hospital,hospital.getHospitalId());

                            DAO.getDBReference(Constants.HOSPITAL_DB).child(hospital.getHospitalId()).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Hospital hospital =dataSnapshot.getValue(Hospital.class);

                                    if (hospital != null) {

                                        //Intent i = new Intent(getApplicationContext(),AdminHome.class);
                                        //startActivity(i);
                                        //ViewHospital.this.recreate();
                                        Toast.makeText(ViewHospital.this, "Your Account Status is Updated", Toast.LENGTH_SHORT).show();
                                        //PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, i,PendingIntent.FLAG_IMMUTABLE);


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });*/
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void sendSMS(String contactNo){
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(contactNo, null, "Your Account Status is Updated", null,null);
    }

    public void getTheData(){
        DAO.getDBReference(Constants.HOSPITAL_DB).child(hospital).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Hospital hospital=dataSnapshot.getValue(Hospital.class);

                if(hospital!=null)
                {
                    t1.setText("Hospital Name:"+hospital.getName());
                    t2.setText("Location:"+hospital.getLocation());
                    t3.setText("mobile:"+hospital.getContactNo());
                    t4.setText("Is Active Account?:"+hospital.isActive());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
