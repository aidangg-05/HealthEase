package com.sp.healthease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class Signup_doctor1 extends AppCompatActivity {
    TextInputEditText emailp;
    TextInputEditText passwordp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_upfor_doc);

        emailp = findViewById(R.id.email_signup_doc);
        passwordp = findViewById(R.id.password_signup_doc);

        Button nextButton = findViewById(R.id.next_button_doc0);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain values from input fields when the button is clicked
                String email = emailp.getText().toString();
                String password = passwordp.getText().toString();


                // Create a PatientData object and set the values
                DoctorData doctorData = new DoctorData(email,password,null, null, null, null);

                // Log the data for verification (you can remove this in the final version)
                Log.d("doctorData", "Email: " + doctorData.getEmail() + ", Password: " + doctorData.getPassword());

                Intent intent = new Intent(Signup_doctor1.this, Signup_doctor2.class);
                intent.putExtra("doctorData", doctorData);
                startActivity(intent);
            }
        });
    }
}
