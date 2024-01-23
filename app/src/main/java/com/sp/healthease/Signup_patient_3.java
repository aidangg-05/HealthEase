package com.sp.healthease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Signup_patient_3 extends AppCompatActivity {

    Spinner bloodgroupp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_upfor_patients_pt3);

        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Patient Registration";

        // Retrieve the PatientData object from the intent
        PatientData patientData = (PatientData) getIntent().getSerializableExtra("patientData");

        // Use the email from the retrieved PatientData object
        String email = patientData.getEmail();

        writeSampleUser(apiKey, baseId, tableName, patientData);

        bloodgroupp = findViewById(R.id.dropdown_bloodgroupPatient);

        Button nextButton = findViewById(R.id.submit_button_patient);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain values from input fields when the button is clicked
                String bloodgroup = bloodgroupp.getSelectedItem().toString();

                // Update the PatientData object with the blood group
                patientData.setBloodGroup(bloodgroup);

                // Log the data for verification (you can remove this in the final version)
                Log.d("PatientData", "FullName: " + patientData.getFullName() + ", Email: " + patientData.getEmail() + ", Password: " + patientData.getPassword() + ", Age: " + patientData.getAge() + ", Telegram: " + patientData.getTelegram() + ", BloodGroup: " + patientData.getBloodGroup() );
            }
        });
    }

    private void writeSampleUser(String apiKey, String baseId, String tableName, PatientData patientData) {
        // Construct the Airtable API endpoint
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

        // Construct the request body with user data
        String name = patientData.getFullName() != null ? patientData.getFullName() : "";
        RequestBody requestBody = new FormBody.Builder()
                .add("fields[Full_Name]", name)
                .build();

        // Make the POST request to Airtable
        Request request = new Request.Builder()
                .url(endpoint)
                .header("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();

        // Execute the request asynchronously (using Retrofit or OkHttp)
        // Note: In a production app, use a library like Retrofit or OkHttp for better handling.
        // This example uses OkHttp for simplicity.
        // Ensure you handle network operations on a separate thread or use AsyncTask.

        // Example with OkHttp (execute the request asynchronously)
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                Log.e("Airtable", "Error writing user", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle success
                if (response.isSuccessful()) {
                    Log.d("Airtable", "User added successfully");
                } else {
                    Log.e("Airtable", "Error writing user: " + response.code());
                }
            }
        });
    }
}