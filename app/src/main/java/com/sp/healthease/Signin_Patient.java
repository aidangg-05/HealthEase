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

public class Signin_Patient extends AppCompatActivity {

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
                .add("filterByFormula", "AND({Email}='" + email + "', {Password}='" + password + "')")
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
                            // Iterate over records and check email and password
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject record = records.getJSONObject(i);
                                JSONObject fields = record.getJSONObject("fields");

                                String recordEmail = fields.getString("Email");
                                String recordPassword = fields.getString("Password");

                                if (email.equals(recordEmail) && password.equals(recordPassword)) {
                                    // Authentication successful, navigate to the next activity
                                    runOnUiThread(() -> {
                                        Intent intent = new Intent(Signin_Patient.this, Home_patient.class);
                                        startActivity(intent);
                                        finish(); // Finish the current activity
                                    });
                                    return;
                                }
                            }

                            // No matching email and password found, show error toast
                            runOnUiThread(() -> {
                                Toast.makeText(Signin_Patient.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            });
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


}
