package com.example.mobilehealthinformation.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mobilehealthinformation.R;
import com.example.mobilehealthinformation.dao.DAO;
import com.example.mobilehealthinformation.form.ClaimRequest;
import com.example.mobilehealthinformation.util.Constants;
import com.example.mobilehealthinformation.util.MapUtil;
import com.example.mobilehealthinformation.util.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ListPolicyClaimRequests extends AppCompatActivity {

    ListView listView;
    String patient="";

    DatabaseReference databaseReference;

    String aa;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_policy_claim_requests);

        listView=(ListView) findViewById(R.id.ClaimRequestList);
        Session s=new Session(getApplicationContext());


        final Map<String,Object> map=new HashMap<>();
        final Map<String,String> viewMap=new HashMap<String,String>();

        if(s.getRole().equals("hospital"))
        {
            Intent i=getIntent();
            savedInstanceState=i.getExtras();
            patient=savedInstanceState.getString("patient");
        }

        aa = getIntent().getStringExtra("aadhaarNo").toString();

        databaseReference = FirebaseDatabase.getInstance().getReference("claimrequests");

        if (aa.equals("just")){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    ArrayList<String> al = new ArrayList<String>(viewMap.keySet());

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        ClaimRequest claimRequest = dataSnapshot.getValue(ClaimRequest.class);
                        String key = dataSnapshot.getKey();
                        al.add(key);
                    }
                    Collections.reverse(al);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(listView.getContext(),
                            android.R.layout.simple_list_item_1, (al.toArray(new String[al.size()])));

                    listView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    ArrayList<String> al = new ArrayList<String>(viewMap.keySet());

                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        ClaimRequest claimRequest = dataSnapshot.getValue(ClaimRequest.class);
                        if (claimRequest.getPatientAadhaarNo().equals(aa)){
                            String key = dataSnapshot.getKey();
                            al.add(key);
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(listView.getContext(),
                            android.R.layout.simple_list_item_1, (al.toArray(new String[al.size()])));

                    listView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        //DAO.getDBReference(Constants.CLAIM_REQUEST_DB)
        /*databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int j = 1;

                ArrayList<String> al = new ArrayList<String>(viewMap.keySet());

                for (DataSnapshot snapshotNode : dataSnapshot.getChildren()) {

                    String key = snapshotNode.getKey();
                    ClaimRequest claimRequest = snapshotNode.getValue(ClaimRequest.class);

                    al.add(key);

                    if(claimRequest!=null)
                    {
                        if(s.getRole().equals("hospital"))
                        {
                            if(claimRequest.getPatientAadhaarNo().equals(patient))
                            {
                                viewMap.put(j + "_" + claimRequest.getDescription(), key);
                                j++;
                            }
                        }
                        else if(s.getRole().equals("patient"))
                        {
                            if(claimRequest.getPatientAadhaarNo().equals(s.getusername()))
                            {
                                viewMap.put(j + "_" + claimRequest.getDescription(), key);
                                j++;
                            }
                        }
                        else
                        {
                            viewMap.put(j + "_" + claimRequest.getPatientAadhaarNo()+"_"+claimRequest.getHospitalId(), key);
                            j++;
                        }
                    }
                }



                ArrayAdapter<String> adapter = new ArrayAdapter<String>(listView.getContext(),
                        android.R.layout.simple_list_item_1, (al.toArray(new String[al.size()])));

                listView.setAdapter(adapter);

                final Session s = new Session(getApplicationContext());
                s.setViewMap(MapUtil.mapToString(viewMap));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String item = listView.getItemAtPosition(i).toString();
                //String id= MapUtil.stringToMap(s.getViewMap()).get(item);

                if(s.getRole().equals("hospital"))
                {
                    Intent intent = new Intent(getApplicationContext(), HospitalViewPolicyClaimRequest.class);
                    intent.putExtra("claimrequest", item);
                    startActivity(intent);
                }
                else if(s.getRole().equals("admin")){

                    Intent intent = new Intent(getApplicationContext(), AdminViewPolicyClaimRequest.class);
                    intent.putExtra("claimrequest", item);
                    startActivity(intent);
                }
                else if(s.getRole().equals("patient")){

                    Intent intent = new Intent(getApplicationContext(), PatientViewPolicyClaimRequest.class);
                    intent.putExtra("claimrequest", item);
                    startActivity(intent);
                }
            }
        });
    }
}
