package com.sp.healthease;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class appointmentPatient extends Fragment {
    TextView doctorTextView;
    TextView dateTextView;
    TextView timeTextView;
    TextView locationTextView;
    CardView appointmentCardView;

    private Appointment appointment;

    public appointmentPatient() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            new FetchAppointmentsTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_patient, container, false);

        doctorTextView = view.findViewById(R.id.appointment_doc);
        dateTextView = view.findViewById(R.id.appointment_date);
        timeTextView = view.findViewById(R.id.appointment_time);
        locationTextView = view.findViewById(R.id.appointment_location);
        appointmentCardView = view.findViewById(R.id.appoint_patient);

        return view;
    }

    private class FetchAppointmentsTask extends AsyncTask<Void, Void, Appointment> {
        @Override
        protected Appointment doInBackground(Void... voids) {
            try {
                return fetchDataFromAirtable();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Appointment result) {
            super.onPostExecute(result);
            if (result != null) {
                appointment = result;
                // Update the UI with the appointment details
                updateUI(appointment);
            }
        }
    }

    private Appointment fetchDataFromAirtable() throws IOException, JSONException {
        // Replace these placeholders with your actual Airtable API key, base ID, and table name
        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Appointments";
        String url = "https://api.airtable.com/v0/" + baseId + "/" + tableName + "?maxRecords=1"; // Get only one record
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + apiKey)
                .build();
        Response response = client.newCall(request).execute();
        String responseData = response.body().string();
        JSONObject jsonObject = new JSONObject(responseData);
        JSONArray records = jsonObject.getJSONArray("records");

        // If there are no records, return null
        if (records.length() == 0) {
            return null;
        }

        JSONObject record = records.getJSONObject(0);
        JSONObject fields = record.getJSONObject("fields");
        String doctorName = fields.getString("Doctor");
        String date = fields.getString("Date");
        String time = fields.getString("Time");
        String location = fields.getString("Location");

        return new Appointment(doctorName, date, time, location);
    }

    private void updateUI(Appointment appointment) {
        doctorTextView.setText("Doctor: " + appointment.getDoctorName());
        dateTextView.setText("Date: " + appointment.getDate());
        timeTextView.setText("Time: " + appointment.getTime());
        locationTextView.setText("Location: " + appointment.getLocation());
        // Show the card view once data is loaded
        appointmentCardView.setVisibility(View.VISIBLE);
    }

    public class Appointment {
        private String doctorName;
        private String date;
        private String time;
        private String location;

        public Appointment(String doctorName, String date, String time, String location) {
            this.doctorName = doctorName;
            this.date = date;
            this.time = time;
            this.location = location;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getLocation() {
            return location;
        }
    }
}
