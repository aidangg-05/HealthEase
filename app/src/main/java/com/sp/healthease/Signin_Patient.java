package com.sp.healthease;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Signin_Patient extends AppCompatActivity {
    private PatientData patientData;
    private AppointmentData appointmentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_infor_patients);

        // Initialize UI elements
        TextInputEditText emailEditText = findViewById(R.id.email_patient);
        TextInputEditText passwordEditText = findViewById(R.id.password_patient);
        MaterialButton signInButton = findViewById(R.id.signin_button_patient);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain user-entered email and password
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validate email and password
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Signin_Patient.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
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
        String tableName = "Patient Registration";
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

        // Construct the request body with user data
        RequestBody requestBody = new FormBody.Builder()
                .add("filterByFormula", "AND({Email}='" + email + "', {Password}='" + password + "', NOT({Email}='', {Password}=''))") // Filter by email and password, ensuring they are not empty
                .build();

        // Make the GET request to Airtable
        Request request = new Request.Builder()
                .url(endpoint)
                .header("Authorization", "Bearer " + apiKey)
                .get()
                .build();

        // Execute the request asynchronously
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                Log.e("Airtable", "Error authenticating user", e);
                Log.e("Airtable", "Error message: " + e.getMessage());

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
                                Toast.makeText(Signin_Patient.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            boolean found = false; // Flag to indicate if a matching email is found
                            // Iterate over records and find matching email
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject record = records.getJSONObject(i);
                                JSONObject fields = record.getJSONObject("fields");

                                // Extract necessary fields
                                String recordEmail = fields.optString("Email");

                                // Check if the email matches the user-entered email
                                if (email.equals(recordEmail)) {
                                    String fullName = fields.optString("Full_Name");
                                    String airtablePassword = fields.optString("Password");
                                    String age = fields.optString("Age");
                                    String telegram = fields.optString("Telegram");
                                    String bloodGroup = fields.optString("Blood_Group");
                                    String medicalHistory = fields.optString("Medical_History");

                                    // Check if the password matches
                                    if (password.equals(airtablePassword)) {
                                        // Create PatientData object and populate it with retrieved data
                                        patientData = new PatientData(fullName, email, airtablePassword, age, telegram, bloodGroup, medicalHistory);

                                        // Inflate data to PatientData class
                                        inflateData(patientData);

                                        // Fetch appointments for the patient
                                        fetchAppointmentsForPatient(fullName);

                                        found = true; // Indicate that a matching email and password are found
                                        break; // Exit the loop once the correct record is found
                                    }
                                }
                            }
                            // If the loop finishes without finding a matching email or password
                            if (!found) {
                                runOnUiThread(() -> {
                                    Toast.makeText(Signin_Patient.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
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

    private void fetchAppointmentsForPatient(String fullName) {
        // Construct the Airtable API endpoint for Appointments table
        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Appointments";
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

        // Construct the request body with filter condition
        RequestBody requestBody = new FormBody.Builder()
                .add("filterByFormula", "{Patients}='" + fullName + "'") // Filter by patient name
                .build();

        // Make the GET request to Airtable
        Request request = new Request.Builder()
                .url(endpoint)
                .header("Authorization", "Bearer " + apiKey)
                .get()
                .build();

        // Execute the request asynchronously
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                Log.e("Airtable", "Error fetching appointments for patient", e);
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
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject record = records.getJSONObject(i);
                            JSONObject fields = record.getJSONObject("fields");

                            // Extract necessary fields from Airtable
                            String doctorName = fields.optString("Doctor");
                            String clinicName = fields.optString("Clinic");
                            String appointmentDate = fields.optString("Date");
                            String appointmentTime = fields.optString("Time");
                            String patientName = fields.optString("Patient");

                            // Compare patientName with fullName
                            if (fullName.equals(patientName)) {
                                // Create AppointmentData object
                                appointmentData = new AppointmentData(doctorName, clinicName, appointmentDate, appointmentTime, patientName);

                                // Log appointment data
                                Log.d("AppointmentData", "Appointment Data: " +
                                        appointmentData.getDoctorName() + ", " +
                                        appointmentData.getClinicName() + ", " +
                                        appointmentData.getAppointmentDate() + ", " +
                                        appointmentData.getAppointmentTime() + ", " +
                                        appointmentData.getPatientName());

                                // Perform any action with appointment data here, such as displaying it or storing it
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("Airtable", "Error parsing JSON response", e);
                    }

                } else {
                    Log.e("Airtable", "Error fetching appointments for patient: " + response.code());
                }

                // Inside onResponse method after retrieving patient data
                inflateData(patientData);
            }
        });
    }

    // Method to inflate data to PatientData class
    private void inflateData(PatientData patientData) {
        // Log patient data
        Log.d("PatientData", "Patient Data: " + patientData.getFullName() + ", " +
                patientData.getEmail() + ", " +
                patientData.getPassword() + ", " +
                patientData.getAge() + ", " +
                patientData.getTelegram() + ", " +
                patientData.getBloodGroup() + ", " +
                patientData.getMedicalHistory());

        // Start new activity with inflated data
        Intent intent = new Intent(Signin_Patient.this, Home_patient.class);
        // Pass patient data to the new activity
        intent.putExtra("patientData", patientData);
        // Pass appointment data to the new activity if available
        if (appointmentData != null) {
            intent.putExtra("appointmentData", appointmentData);
        }
        startActivity(intent);
        // Finish the current activity
        finish();
    }
}
