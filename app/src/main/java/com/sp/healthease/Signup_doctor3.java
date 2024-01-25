package com.sp.healthease;

import android.content.Intent;
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

public class Signup_doctor3 extends AppCompatActivity {

    TextInputEditText bloodgroupp;
    TextInputEditText medicalhistoryp;

    DoctorData doctorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_upfor_doc_pt3);

        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Doctor Registration";

        // Retrieve the PatientData object from the intent
        doctorData = (DoctorData) getIntent().getSerializableExtra("doctorData");

        bloodgroupp = findViewById(R.id.fullname_signup_doc);
        medicalhistoryp = findViewById(R.id.telegramid_signup_doc);

        Button nextButton = findViewById(R.id.submit_button_doc);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain values from input fields when the button is clicked
                String bloodgroup = bloodgroupp.getText().toString();
                String medicalhistory = medicalhistoryp.getText().toString();

                // Update the PatientData object with the blood group
                doctorData.setFullName(bloodgroup);
                doctorData.setTelegram(medicalhistory);

                // Log the data for verification (you can remove this in the final version)
                Log.d("doctorData", "Email: " + doctorData.getEmail() + ", Password: " + doctorData.getPassword() + ", Field: " + doctorData.getField() + ", Clinic: " + doctorData.getClinic() + ", FullName: " + doctorData.getFullName() + ", Telegram: " + doctorData.getTelegram());
                // Call the function to write data to Airtable
                writeSampleUser(apiKey, baseId, tableName, doctorData);

                Intent intent = new Intent(Signup_doctor3.this, SplashScreen.class);
                startActivity(intent);
            }
        });
    }

    private void writeSampleUser(String apiKey, String baseId, String tableName, DoctorData doctorData) {
        // Construct the Airtable API endpoint
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

        // Construct the request body with user data
        String email = doctorData.getEmail() != null ? doctorData.getEmail() : "";
        String password = doctorData.getPassword() != null ? doctorData.getPassword() : "";
        String name = doctorData.getFullName() != null ? doctorData.getFullName() : "";
        String telegram = doctorData.getTelegram() != null ? doctorData.getTelegram() : "";
        String bloodgroup = doctorData.getClinic() != null ? doctorData.getClinic() : "";
        String medicalhistory = doctorData.getField() != null ? doctorData.getField() : "";

        RequestBody requestBody = new FormBody.Builder()
                .add("fields[Email]", email)
                .add("fields[Password]", password)
                .add("fields[Full_Name]", name)
                .add("fields[Telegram]", telegram)
                .add("fields[Clinic]", bloodgroup)
                .add("fields[Field]", medicalhistory)
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
                    Log.e("Airtable", "Response body: " + response.body().string()); // Log the response body
                }
            }
        });
    }
}