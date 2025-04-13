package com.example.mobilehealthinformation.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.HealthPolicy;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.MapUtil;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListHealthPolicies extends AppCompatActivity {

    ListView listView;
    String patient="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_health_policies);

        listView=(ListView) findViewById(R.id.HealthPolicysList);
        final Session s=new Session(getApplicationContext());

        final Map<String,Object> map=new HashMap<>();
        final Map<String,String> viewMap=new HashMap<String,String>();

        if(s.getRole().equals("hospital"))
        {
            Intent i=getIntent();
            savedInstanceState=i.getExtras();
            patient=savedInstanceState.getString("patient");
        }

        DAO.getDBReference(Constants.HEALTH_POLICY_DB).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int j = 1;

                for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {

                    String key = snapshotNode.getKey();
                    HealthPolicy healthpolicy = snapshotNode.getValue(HealthPolicy.class);

                    if(healthpolicy!=null)
                    {
                        viewMap.put(j + "_" + healthpolicy.getPolicyName(), key);
                        j++;
                    }
                }

                ArrayList<String> al = new ArrayList<String>(viewMap.keySet());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(listView.getContext(),
                        android.R.layout.simple_list_item_1, (al.toArray(new String[al.size()])));

                listView.setAdapter(adapter);

                final Session s = new Session(getApplicationContext());
                s.setViewMap(MapUtil.mapToString(viewMap));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String item = listView.getItemAtPosition(i).toString();
                String id= MapUtil.stringToMap(s.getViewMap()).get(item);

                String role= s.getRole();

                if(role.equals("admin"))
                {
                    Intent intent = new Intent(getApplicationContext(), AdminViewHealthPolicy.class);
                    intent.putExtra("healthpolicy", id);
                    startActivity(intent);
                }
                else if(role.equals("hospital"))
                {
                    Intent intent = new Intent(getApplicationContext(),HospitalViewHealthPolicy.class);
                    intent.putExtra("healthpolicy", id);
                    intent.putExtra("patient", patient);
                    startActivity(intent);
                }
            }
        });
    }
}
