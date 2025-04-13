package com.example.mobilehealthinformation.view;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mobilehealthinformation.MainActivity;

import java.util.Random;
import java.util.UUID;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.Hospital;
import com.example.mobilehealthinformation.form.Hospital2;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HospitalRegistration extends AppCompatActivity {

    EditText e1,e2,e3,e4,e5,e6;
    Button b1;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hospital_registration);

        final Session s=new Session(getApplicationContext());

        e1=(EditText)findViewById(R.id.hospitalregistrationusername);
        e2=(EditText)findViewById(R.id.hospitalregistrationname);
        e3=(EditText)findViewById(R.id.hospitalregistrationlocation);
        e4=(EditText)findViewById(R.id.hospitalregistrationcontactnumber);
        e5=(EditText)findViewById(R.id.hospitalregistrationpassword);
        e6=(EditText)findViewById(R.id.hospitalregistrationconformpassword);

        b1=(Button)findViewById(R.id.hospitalregistrationregisterbutton);
        //==================================================================================

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 100);
            }
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username=e1.getText().toString();
                final String name=e2.getText().toString();
                final String location=e3.getText().toString();
                final String mobile=e4.getText().toString();
                final String password=e5.getText().toString();
                final String conformpassword=e6.getText().toString();

                if(username.isEmpty() || name.isEmpty() || location.isEmpty() || mobile.isEmpty() || password.isEmpty() || conformpassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(conformpassword))
                {
                    Toast.makeText(getApplicationContext(), "Password and Conform Password Should be Same", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseReference = FirebaseDatabase.getInstance().getReference("hospitals").child(username);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            /*if (dataSnapshot.exists()){
                                Toast.makeText(getApplicationContext(),"User All ready Registred with this ID",Toast.LENGTH_SHORT).show();
                            }else {

                            }*/

                            Hospital2 hospital = new Hospital2(username,name,location,mobile,false,password);

                            databaseReference.setValue(hospital);


                            /*if(hospital==null)
                            {

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"User All ready Registred with this ID",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(getApplicationContext(),HospitalRegistration.class);
                                startActivity(i);
                            }*/
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Random r=new Random();
                    String pattern="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789";

                    String otp="";

                    for(int i=0;i<4;i++)
                    {
                        otp=otp+pattern.charAt(r.nextInt(60));
                    }

                    String data=username+"_"+name+"_"+location+"_"+mobile+"_"+password;

                    Intent i = new Intent(getApplicationContext(),ValidateHospitalOTP.class);
                    i.putExtra("hospitaldata",data);
                    i.putExtra("hospitalotp", otp);
                    startActivity(i);
                    finish();

                    //PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, i,PendingIntent.FLAG_IMMUTABLE);

                    //SmsManager sms=SmsManager.getDefault();
                    //sms.sendTextMessage(mobile,null, "Otp:"+otp, pi,null);

                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(mobile, null, "Otp:"+otp, null, null);
                        Toast.makeText(HospitalRegistration.this, "SMS sent.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(HospitalRegistration.this, "SMS failed, please try again.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
