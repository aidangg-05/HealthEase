package com.sp.healthease;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import com.google.android.material.button.MaterialButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.util.Log;

public class Patients_BookAppointment extends Fragment {
    private TextView clinicNameTextView;
    private Spinner doctorDropdown;
    private Spinner appointmentTime;
    private List<Map<String, String>> doctors;

    public Patients_BookAppointment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patients__book_appointment, container, false);
        MaterialButton submitButton = view.findViewById(R.id.booking_submitbtn);
        clinicNameTextView = view.findViewById(R.id.clinicName);
        doctorDropdown = view.findViewById(R.id.dropdowndocselection);
        appointmentTime =view.findViewById(R.id.dropdowntimeselection);
        doctors = new ArrayList<>();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //string for doc
                String selectedDoctor = doctorDropdown.getSelectedItem().toString();
                //string for time selected
                String selectedTime = appointmentTime.getSelectedItem().toString();

                // Get selected date from calendar
                CalendarView calendarView = view.findViewById(R.id.dateOfappoint);
                long selectedDateMillis = calendarView.getDate();
                Date selectedDate = new Date(selectedDateMillis);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                //string for date
                String selectedDateString = dateFormat.format(selectedDate);

                // Concatenate doctor and date into a single string
                String userInput = "Selected Doctor: " + selectedDoctor + "\nSelected Date: " + selectedDateString + "\nSelected Time:" + selectedTime;

                // Log or use the userInput as needed
                Log.d("UserInput", userInput);
            }
        });

        // Get marker title from arguments bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String markerTitle = bundle.getString("markerTitle");
            if (markerTitle != null) {
                clinicNameTextView.setText("Clinic: " + markerTitle);
                // Fetch data from Airtable based on marker title
                fetchDataFromAirtable(markerTitle);
            }
        }

        // You can also retrieve patientData if it was passed
        PatientData patientData = bundle.getParcelable("patientData");
        if (patientData != null) {
            // Log the contents of patientData
            Log.d("Patients_BookAppt", "Patient Data: " + patientData.getFullName() + ", " + patientData.getEmail() + ", " + patientData.getAge());
        } else {
            Log.d("Patients_BookAppt", "Patient Data is null");
        }

        return view;
    }



    private void fetchDataFromAirtable(String clinicName) {
        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Doctor Registration";
        String url = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    try {
                        doctors = parseDoctors(jsonData);
                        getActivity().runOnUiThread(() -> populateDropdown());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private List<Map<String, String>> parseDoctors(String jsonData) throws JSONException {
        List<Map<String, String>> doctors = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray records = jsonObject.getJSONArray("records");
        for (int i = 0; i < records.length(); i++) {
            JSONObject record = records.getJSONObject(i);
            JSONObject fields = record.getJSONObject("fields");
            String fullName = fields.getString("Full_Name");
            Map<String, String> doctor = new HashMap<>();
            doctor.put("fullName", fullName);
            doctors.add(doctor);
        }
        return doctors;
    }

    private void populateDropdown() {
        List<String> doctorNames = new ArrayList<>();
        for (Map<String, String> doctor : doctors) {
            doctorNames.add(doctor.get("fullName"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, doctorNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorDropdown.setAdapter(adapter);
    }

}
