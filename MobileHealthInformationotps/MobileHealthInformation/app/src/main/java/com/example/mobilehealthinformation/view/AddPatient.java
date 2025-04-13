package com.example.mobilehealthinformation.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mobilehealthinformation.R;

public class AddPatient extends AppCompatActivity {

    private EditText healthCardNumber, aadhaarNumber, patientName, patientAge, patientGender, patientContact, patientAddress,password;
    private Button nextButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        healthCardNumber = findViewById(R.id.addhealthcardnumber);
        aadhaarNumber = findViewById(R.id.addpatientaadhaarnumber);
        patientName = findViewById(R.id.addpatientname);
        patientAge = findViewById(R.id.addpatientage);
        patientGender = findViewById(R.id.addpatientgender);
        patientContact = findViewById(R.id.addpatientcontactnumber);
        patientAddress = findViewById(R.id.addpatientaddress);
        password = findViewById(R.id.password);

        nextButton = findViewById(R.id.addfamilynumbersnextbutton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage();
            }
        });
    }

    private void goToNextPage() {
        Intent intent = new Intent(AddPatient.this, family_details.class);
        intent.putExtra("healthCardNumber", healthCardNumber.getText().toString());
        intent.putExtra("aadhaarNumber", aadhaarNumber.getText().toString());
        intent.putExtra("patientName", patientName.getText().toString());
        intent.putExtra("patientAge", patientAge.getText().toString());
        intent.putExtra("patientGender", patientGender.getText().toString());
        intent.putExtra("patientContact", patientContact.getText().toString());
        intent.putExtra("patientAddress", patientAddress.getText().toString());
        intent.putExtra("password", password.getText().toString());
        startActivity(intent);
        finish();
    }
}
