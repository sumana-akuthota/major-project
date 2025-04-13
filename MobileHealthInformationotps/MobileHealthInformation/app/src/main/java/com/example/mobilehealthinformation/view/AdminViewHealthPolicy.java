package com.example.mobilehealthinformation.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.HealthPolicy;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AdminViewHealthPolicy extends AppCompatActivity {

    TextView t1,t2,t3;

    Button back;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_health_policy);

        back=(Button) findViewById(R.id.adminviewhealthpolicyback);
        delete=(Button) findViewById(R.id.adminviewhealthpolicydelete);

        Intent i=getIntent();
        savedInstanceState=i.getExtras();

        final String healthpolicy=savedInstanceState.getString("healthpolicy");

        t1=(TextView) findViewById(R.id.adminviewhealthpolicyname);
        t2=(TextView)findViewById(R.id.adminviewhealthpolicydescription);
        t3=(TextView) findViewById(R.id.adminviewhealthpolicycoverageamount);

        final Session session=new Session(getApplicationContext());

        DAO.getDBReference(Constants.HEALTH_POLICY_DB).child(healthpolicy).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HealthPolicy healthpolicy=dataSnapshot.getValue(HealthPolicy.class);

                if(healthpolicy!=null)
                {
                    t1.setText("Health Policy Name:"+healthpolicy.getPolicyName());
                    t2.setText("Description:"+healthpolicy.getDescription());
                    t3.setText("Coverage Amount:"+healthpolicy.getCoverageAmount());
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DAO dao=new DAO();
                dao.deleteObject(Constants.HEALTH_POLICY_DB,healthpolicy);

                Intent i= new Intent(getApplicationContext(),AdminHome.class);
                startActivity(i);
            }
        });
    }
}
