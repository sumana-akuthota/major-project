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
import com.example.mobilehealthinformation.form.HealthPolicy;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.Session;

public class AddHealthPolicy extends AppCompatActivity {

    EditText e1,e2,e3;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_health_policy);

        final Session s=new Session(getApplicationContext());

        e1=(EditText)findViewById(R.id.addhealthpolicypolicyname);
        e2=(EditText)findViewById(R.id.addhealthpolicydescription);
        e3=(EditText)findViewById(R.id.addhealthpolicycoverageamount);

        b1=(Button)findViewById(R.id.addhealthpolicysubmitbutton);
        //==================================================================================

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name=e1.getText().toString();
                final String description=e2.getText().toString();
                final String coverageamount=e3.getText().toString();

                if(name.isEmpty() || description.isEmpty() || coverageamount.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    HealthPolicy healthPolicy=new HealthPolicy();

                    healthPolicy.setPolicyName(name);
                    healthPolicy.setDescription(description);
                    healthPolicy.setCoverageAmount(Integer.parseInt(coverageamount));
                    healthPolicy.setPolicyId(new DAO().getUnicKey(Constants.HEALTH_POLICY_DB));

                    DAO dao=new DAO();

                    try
                    {
                        dao.addObject(Constants.HEALTH_POLICY_DB,healthPolicy,healthPolicy.getPolicyId());
                        Toast.makeText(getApplicationContext(),"Health Policy Added Successfully",Toast.LENGTH_SHORT).show();

                        Intent i=new Intent(getApplicationContext(),AdminHome.class);
                        startActivity(i);
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                        Log.v("HealthPolicy Adding  Ex", ex.toString());
                        ex.printStackTrace();
                    }

                }
            }
        });
    }
}
