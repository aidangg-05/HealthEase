package com.sp.healthease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Signin_doctor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_infor_doctor);

        // Initialize UI elements
        TextInputEditText emailEditText = findViewById(R.id.email_doc);
        TextInputEditText passwordEditText = findViewById(R.id.password_doc);
        MaterialButton signInButton = findViewById(R.id.signin_button_doc);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain user-entered email and password
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validate email and password
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Signin_doctor.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Perform authentication with Airtable
                authenticateWithAirtable(email, password);
            }
        });
    }

    private void authenticateWithAirtable(String email, String password) {
        // Construct the Airtable API endpoint
        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Doctor Registration";
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

        // Construct the request body with filter formula for email
        String filterFormula = "{Email}='" + email + "'";
        RequestBody requestBody = new FormBody.Builder()
                .add("filterByFormula", filterFormula)
                .build();

        // Make the GET request to Airtable
        Request request = new Request.Builder()
                .url(endpoint + "?filterByFormula=" + filterFormula)
                .header("Authorization", "Bearer " + apiKey)
                .get() // Change POST to GET
                .build();

        // Execute the request asynchronously
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                Log.e("Airtable", "Error authenticating user", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle success
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("Airtable", "Response body: " + responseBody);

                    try {
                        // Parse the JSON response
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        // Check if the response contains any records
                        JSONArray records = jsonResponse.getJSONArray("records");
                        if (records.length() == 0) {
                            // No matching records, show error toast
                            runOnUiThread(() -> {
                                Toast.makeText(Signin_doctor.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            // Get the first record (assuming unique email)
                            JSONObject record = records.getJSONObject(0);
                            JSONObject fields = record.getJSONObject("fields");

                            // Extract password from the record
                            String recordPassword = fields.optString("Password");

                            // Check if the entered password matches the password from the record
                            if (password.equals(recordPassword)) {
                                // Passwords match, proceed with authentication
                                String fullName = fields.optString("Full_Name");
                                String field = fields.optString("Field");
                                String clinic = fields.optString("Clinic");
                                String telegram = fields.optString("Telegram");

                                // Create DoctorData object
                                DoctorData doctorData = new DoctorData(email, password, field, clinic, fullName, telegram);

                                // Inflate data
                                inflateData(doctorData);

                                // Finish the current activity
                                finish();
                            } else {
                                // Incorrect password
                                runOnUiThread(() -> {
                                    Toast.makeText(Signin_doctor.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("Airtable", "Error parsing JSON response", e);
                    }
                } else {
                    Log.e("Airtable", "Error authenticating user: " + response.code());
                }
            }
        });
    }





    private void inflateData(DoctorData doctorData) {
        // Log doctor data
        Log.d("DoctorData", "Doctor Data: " +
                "Full Name: " + doctorData.getFullName() + ", " +
                "Email: " + doctorData.getEmail() + ", " +
                "Password: " + doctorData.getPassword() + ", " +
                "Field: " + doctorData.getField() + ", " +
                "Clinic: " + doctorData.getClinic() + ", " +
                "Telegram: " + doctorData.getTelegram());

        // Start new activity with inflated data
        Intent intent = new Intent(Signin_doctor.this, Home_doctor.class);
        // Pass doctor data to the new activity
        intent.putExtra("doctorData", doctorData);
        startActivity(intent);
        // Finish the current activity
        finish();
    }

}
