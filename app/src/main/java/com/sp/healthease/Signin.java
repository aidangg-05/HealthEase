package com.sp.healthease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Signin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        MaterialButton doctorbutton = findViewById(R.id.Doc_signin);
        MaterialButton patientbutton = findViewById(R.id.Patient_signin);

        doctorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this,Signin_doctor.class);
                startActivity(intent);
            }
        });

        patientbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this,Signin_Patient.class);
                startActivity(intent);
            }
        });
    }
}
