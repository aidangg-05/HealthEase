package com.sp.healthease;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import androidx.annotation.Nullable;


public class Signup_doctor3 extends AppCompatActivity {

    private static final int SELECT_IMAGE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    ImageView selectIDbtn;
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
        doctorData = getIntent().getParcelableExtra("doctorData");

        selectIDbtn = findViewById(R.id.doc_id_img);
        bloodgroupp = findViewById(R.id.fullname_signup_doc);
        medicalhistoryp = findViewById(R.id.telegramid_signup_doc);

        selectIDbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
            }
        });

        Button nextButton = findViewById(R.id.submit_button_doc);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the submission here
            }
        });
    }

    private void requestCameraPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        // Create an array of options
        String[] options = {"Upload from Local", "Open Camera"};

        // Create a dialog to display the options
        AlertDialog.Builder builder = new AlertDialog.Builder(Signup_doctor3.this);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Upload from local option selected
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE);
                        break;
                    case 1:
                        // Open camera option selected
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(cameraIntent, SELECT_IMAGE);
                        } else {
                            Log.e("Camera", "No camera app found");
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            // Handle the selected image
            // You may implement your logic here to handle the selected image
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, open the camera
                openCamera();
            } else {
                // Camera permission denied
                // You may inform the user or take appropriate action
            }
        }
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
