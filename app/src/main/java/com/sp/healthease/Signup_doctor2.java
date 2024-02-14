package com.sp.healthease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Spinner;

public class Signup_doctor2 extends AppCompatActivity {
    Spinner fieldp;
    Spinner clinicp;
    DoctorData doctorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_upfor_doc_pt2);

        doctorData = getIntent().getParcelableExtra("doctorData");

        // Ensure that doctorData is not null before using it
        if (doctorData != null) {
            // Your existing code here
        } else {
            Log.e("Signup_doctor2", "DoctorData object is null");
        }

        fieldp = findViewById(R.id.dropdown_fieldDoc);
        clinicp = findViewById(R.id.dropdown_clinicDoc);

        Button nextButton = findViewById(R.id.next_button_doc1);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain values from input fields when the button is clicked
                String field = fieldp.getSelectedItem().toString();
                String clinic = clinicp.getSelectedItem().toString();

                // Update the existing doctorData object with the new values
                doctorData.setField(field);
                doctorData.setClinic(clinic);

                // Log the data for verification (you can remove this in the final version)
                Log.d("doctorData", "Email: " + doctorData.getEmail() + ", Password: " + doctorData.getPassword() + ", Field: " + doctorData.getField() + ", Clinic: " + doctorData.getClinic());

                Intent intent = new Intent(Signup_doctor2.this, Signup_doctor3.class);
                intent.putExtra("doctorData", doctorData);
                startActivity(intent);
            }
        });
    }
}
