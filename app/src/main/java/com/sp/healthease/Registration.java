package com.sp.healthease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        MaterialButton doctorbutton = findViewById(R.id.Doc_signup);
        MaterialButton patientbutton = findViewById(R.id.Patient_signup);
        MaterialButton signinbutton = findViewById(R.id.signin_layout_button);

        doctorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this,Signup_doctor1.class);
                startActivity(intent);
            }
        });

        patientbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this,Signup_patient_1.class);
                startActivity(intent);
            }
        });

        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, Signin.class);
                startActivity(intent);
            }
        });
    }

}
