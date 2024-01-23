package com.sp.healthease;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Signup_patient_2 extends AppCompatActivity {

    TextInputEditText fullnamep;
    TextInputEditText telegramidp;
    TextInputEditText agep;
    PatientData patientData; // Declare it here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_upfor_patients_pt2);

        // Retrieve the PatientData object from the intent
        patientData = (PatientData) getIntent().getSerializableExtra("patientData");

        fullnamep = findViewById(R.id.fullname_signup_patient);
        telegramidp = findViewById(R.id.telegramid_signup_patient);
        agep = findViewById(R.id.age_signup_patient);

        Button nextButton = findViewById(R.id.next_button_patient1);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain values from input fields when the button is clicked
                String fullname = fullnamep.getText().toString();
                String telegramid = telegramidp.getText().toString();
                String age = agep.getText().toString();

                // Update the existing PatientData object with new values
                patientData.setFullName(fullname);
                patientData.setTelegram(telegramid);
                patientData.setAge(age);

                // Log the data for verification (you can remove this in the final version)
                Log.d("PatientData", "FullName: " + patientData.getFullName() + ", Email: " + patientData.getEmail() + ", Password: " + patientData.getPassword() + ", Age: " + patientData.getAge() + ", Telegram: " + patientData.getTelegram() );

                Intent intent = new Intent(Signup_patient_2.this, Signup_patient_3.class);
                intent.putExtra("patientData", patientData);
                startActivity(intent);
            }
        });
    }
}

