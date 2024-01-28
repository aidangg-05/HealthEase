package com.sp.healthease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Home_patient extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_patient);

        // Retrieve email from Intent
        String userEmail = getIntent().getStringExtra("userEmail");

        // Log userEmail for debugging
        Log.d("Home_patient", "User Email: " + userEmail);

        // Fetch data based on userEmail and update the UI
        fetchDataAndUpdateUI(userEmail);

        CardView appointmentCardView = findViewById(R.id.cardView_appointment);
        CardView dietCardView = findViewById(R.id.cardView_diets);

        appointmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAppointment();
            }
        });

        dietCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigeateToDiets();
            }
        });
    }

    private void navigateToAppointment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.patient_framelayout,new appointmentPatient());
        fragmentTransaction.commit();
    }

    private void navigeateToDiets() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.patient_framelayout,new dietsPatients());
        fragmentTransaction.commit();
    }

    private void fetchDataAndUpdateUI(String userEmail) {
        // Construct the Airtable API endpoint
        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Patient Registration";
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

        // Construct the request body with user data
        RequestBody requestBody = new FormBody.Builder()
                .add("filterByFormula", "{Email}='" + userEmail + "'")
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
                Log.e("Airtable", "Error fetching user data", e);
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
                        if (records.length() > 0) {
                            // Retrieve the first record (assuming there's only one matching record)
                            JSONObject firstRecord = records.getJSONObject(0);
                            JSONObject fields = firstRecord.getJSONObject("fields");

                            // Create a PatientData object with the retrieved data
                            PatientData patientData = new PatientData(
                                    fields.optString("Full_Name", ""),           // Use "Full_Name" for FullName
                                    fields.optString("Email", ""),
                                    fields.optString("Password", ""),
                                    fields.optString("Age", ""),
                                    fields.optString("Telegram", ""),
                                    fields.optString("Blood_Group", ""),         // Use "Blood_Group" for BloodGroup
                                    fields.optString("Medical_History", "")     // Use "Medical_History" for MedicalHistory
                            );


                            // Log retrieved PatientData for debugging
                            Log.d("Home_patient", "Retrieved PatientData: " + patientData.getFullName());
                            Log.d("PatientData", "FullName: " + patientData.getFullName() + ", Email: " + patientData.getEmail() + ", Password: " + patientData.getPassword() + ", Age: " + patientData.getAge() + ", Telegram: " + patientData.getTelegram() + ", BloodGroup: " + patientData.getBloodGroup() + ", MedicalHistory: " + patientData.getMedicalHistory());


                            // Update the UI with the retrieved patientData
                            runOnUiThread(() -> {
                                updateUI(patientData);
                            });
                        } else {
                            // No matching records found, show error toast
                            runOnUiThread(() -> {
                                Toast.makeText(Home_patient.this, "No data found for the user", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } catch (JSONException e) {
                        Log.e("Airtable", "Error parsing JSON response", e);
                    }
                } else {
                    Log.e("Airtable", "Error fetching user data: " + response.code());
                }
            }
        });
    }

    private void updateUI(PatientData patientData) {
        // Find the TextView by ID
        TextView usernameTextView = findViewById(R.id.username_patient);

        // Log whether TextView is found
        Log.d("Home_patient", "TextView found: " + (usernameTextView != null));

        // Log the value obtained from getFullName()
        Log.d("Home_patient", "PatientData Full Name: " + patientData.getFullName());

        // Update the TextView with the fullName from patientData
        if (usernameTextView != null) {
            Log.d("Home_patient", "Setting text to: " + patientData.getFullName());
            usernameTextView.setText(patientData.getFullName());
        } else {
            Log.e("Home_patient", "TextView not found");
        }
    }


}
