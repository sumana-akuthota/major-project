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
import com.example.mobilehealthinformation.form.Hospital;
import com.example.mobilehealthinformation.form.Hospital2;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.MapUtil;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminListHospitals extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_hospitals);

        listView=(ListView) findViewById(R.id.HospitalsList);
        final Session s=new Session(getApplicationContext());

        final Map<String,Object> map=new HashMap<>();
        final Map<String,String> viewMap=new HashMap<String,String>();

        DAO.getDBReference(Constants.HOSPITAL_DB).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int j = 1;

                for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {

                    String key = snapshotNode.getKey();
                    Hospital hospital = snapshotNode.getValue(Hospital.class);

                    if(hospital!=null)
                    {
                        viewMap.put(j + "_" + hospital.getName(), key);
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

                Intent intent = new Intent(getApplicationContext(), ViewHospital.class);
                intent.putExtra("hospital", id);
                startActivity(intent);
                finish();
            }
        });
    }
}
