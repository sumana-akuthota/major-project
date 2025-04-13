package com.example.mobilehealthinformation.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.HealthRecord;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.Session;

public class AddHealthRecord extends AppCompatActivity {

    EditText e1,e2,e3;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_health_record);

        final Session s=new Session(getApplicationContext());

        Intent i=getIntent();
        savedInstanceState=i.getExtras();

        final String claimRequest=savedInstanceState.getString("claimrequest");

        e1=(EditText)findViewById(R.id.addhealthrecorddoctor);
        e2=(EditText)findViewById(R.id.addhealthrecordmedicine);
        e3=(EditText)findViewById(R.id.addhealthrecordtreatment);

        b1=(Button)findViewById(R.id.addhealthrecordsubmitbutton);
        //==================================================================================

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String doctor=e1.getText().toString();
                final String medicine=e2.getText().toString();
                final String treatment=e3.getText().toString();

                if(doctor.isEmpty() || medicine.isEmpty() || treatment.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    HealthRecord healthRecord=new HealthRecord();

                    healthRecord.setClaimrequestid(claimRequest);
                    healthRecord.setTreatment(treatment);
                    healthRecord.setDoctorName(doctor);
                    healthRecord.setMedicine(medicine);
                    healthRecord.setRecordId(new DAO().getUnicKey(Constants.HEALTH_RECORD_DB));

                    DAO dao=new DAO();

                    try
                    {
                        dao.addObject(Constants.HEALTH_RECORD_DB,healthRecord,healthRecord.getRecordId());
                        Toast.makeText(getApplicationContext(),"Health Record Added Successfully",Toast.LENGTH_SHORT).show();

                        Intent i=new Intent(getApplicationContext(),HospitalHome.class);
                        startActivity(i);
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                        Log.v("HealthRecord Adding  Ex", ex.toString());
                        ex.printStackTrace();
                    }

                }
            }
        });
    }
}
