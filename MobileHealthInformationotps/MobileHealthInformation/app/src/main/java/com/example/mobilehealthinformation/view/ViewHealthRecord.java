package com.example.mobilehealthinformation.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.HealthRecord;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ViewHealthRecord extends AppCompatActivity {

    TextView t1,t2,t3;

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_health_record);

        back=(Button) findViewById(R.id.viewhealthrecordback);

        Intent i=getIntent();
        savedInstanceState=i.getExtras();

        final String healthrecord=savedInstanceState.getString("healthrecord");

        t1=(TextView) findViewById(R.id.viewhealthrecorddoctor);
        t2=(TextView)findViewById(R.id.viewhealthrecordtreatment);
        t3=(TextView) findViewById(R.id.viewhealthrecordmedicine);

        final Session session=new Session(getApplicationContext());

        DAO.getDBReference(Constants.HEALTH_RECORD_DB).child(healthrecord).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HealthRecord healthRecord=dataSnapshot.getValue(HealthRecord.class);

                if(healthRecord!=null)
                {
                    t1.setText("Doctor Name:"+healthRecord.getDoctorName());
                    t2.setText("Treatment:"+healthRecord.getTreatment());
                    t3.setText("Medicine:"+healthRecord.getMedicine());
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
    }
}
