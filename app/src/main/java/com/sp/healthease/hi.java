package com.sp.healthease;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class hi extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_upfor_patients);

        // Assuming you have set up your Airtable base and obtained the API key, base ID, and table name
        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Patient Registration";

        writeSampleUser(apiKey, baseId, tableName);
    }

    private void writeSampleUser(String apiKey, String baseId, String tableName) {
        // Construct the Airtable API endpoint
        String endpoint = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

        // Construct the request body with user data
        RequestBody requestBody = new FormBody.Builder()
                .add("fields[Email]", "johndoe@email.com")
                .add("fields[Password]", "userpassword")
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
