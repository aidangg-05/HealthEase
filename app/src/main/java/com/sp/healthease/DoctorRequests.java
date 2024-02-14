package com.sp.healthease;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctorRequests extends Fragment {
    private List<Appointment> appointments;
    private RecyclerView list;
    private AppointmentAdapter adapter;

    public DoctorRequests() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            new FetchAppointmentsTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
            appointments = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_requests, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        list = view.findViewById(R.id.requestsRecycle);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(adapter);

        return view;
    }

    public class Appointment {
        private String doctorName;
        private String patientName;
        private String time;
        private String clinicName;
        private String Date;

        void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        void setPatientName(String patientName){
            this.patientName = patientName;
        }

        void setTime(String time){
            this.time = time;
        }

        void setClinicName(String clinicName){
            this.clinicName = clinicName;
        }
        void setDate(String reqDate){
            this.Date = reqDate;
        }

        String getPatientName() {
            return patientName;
        }

        String getTime(){
            return time;
        }

        String getDate(){
            return Date;
        }
    }

    private List<Appointment> fetchDataFromAirtable() throws IOException, JSONException {
        List<Appointment> appointments = new ArrayList<>();

        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Requests";
        String url = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + apiKey)
                .build();

        Response response = client.newCall(request).execute();
        String responseData = response.body().string();

        JSONObject jsonObject = new JSONObject(responseData);
        JSONArray records = jsonObject.getJSONArray("records");

        for (int i = 0; i < records.length(); i++) {
            JSONObject record = records.getJSONObject(i);
            JSONObject fields = record.getJSONObject("fields");

            // Extract data from Airtable fields
            String doctorName = fields.getString("Doctor");
            String reqDate = fields.getString("Date");
            String patientName = fields.getString("Patient");
            String time = fields.getString("Time");
            String clinicName = fields.getString("Clinic");

            // Create Appointment object and add to list
            Appointment appointment = new Appointment();
            appointment.setDoctorName(doctorName);
            appointment.setPatientName(patientName);
            appointment.setTime(time);
            appointment.setClinicName(clinicName);
            appointment.setDate(reqDate);

            appointments.add(appointment);
        }

        return appointments;
    }

    private class FetchAppointmentsTask extends AsyncTask<Void, Void, List<Appointment>> {
        @Override
        protected List<Appointment> doInBackground(Void... voids) {
            try {
                return fetchDataFromAirtable();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Appointment> result) {
            super.onPostExecute(result);
            if (result != null) {
                appointments = result;
                if (adapter == null) {
                    adapter = new AppointmentAdapter();
                    list.setAdapter(adapter); // Set adapter for the RecyclerView
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
        @NonNull
        @Override
        public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requestsrow, parent, false);
            return new AppointmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
            Appointment appointment = appointments.get(position);
            holder.patientNameTextView.setText("Name: " + appointment.getPatientName());
            holder.timeTextView.setText("Time: " + appointment.getTime());
            holder.dateTextView.setText("Date: " + appointment.getDate());

        }

        @Override
        public int getItemCount() {
            return appointments.size();
        }

        public class AppointmentViewHolder extends RecyclerView.ViewHolder {
            TextView patientNameTextView;
            TextView timeTextView;
            TextView dateTextView;

            public AppointmentViewHolder(@NonNull View itemView) {
                super(itemView);
                patientNameTextView = itemView.findViewById(R.id.requestsPatientName);
                timeTextView = itemView.findViewById(R.id.requestsTime);
                dateTextView = itemView.findViewById(R.id.requestsdate);
            }
        }
    }

    private void deleteRecordFromAirtable(String recordId) {
        String apiKey = "YOUR_API_KEY";
        String baseId = "YOUR_BASE_ID";
        String tableName = "Requests";
        String url = "https://api.airtable.com/v0/" + baseId + "/" + tableName + "/" + recordId;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // Record successfully deleted
            } else {
                // Handle unsuccessful deletion
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    // Method to add a record to Airtable
    private void addRecordToAirtable(String doctorName, String patientName, String time, String clinicName, String date) {
        String apiKey = "YOUR_API_KEY";
        String baseId = "YOUR_BASE_ID";
        String tableName = "Appointment";
        String url = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

        OkHttpClient client = new OkHttpClient();

        // Create JSON body for the new record
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String jsonBody = "{\"fields\": {\"Doctor\": \"" + doctorName + "\", \"Patient\": \"" + patientName + "\", \"Time\": \"" + time + "\", \"Clinic\": \"" + clinicName + "\", \"Date\": \"" + date + "\"}}";
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // Record successfully added
            } else {
                // Handle unsuccessful addition
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private void addRecordToAirtableAndDeleteFromRequests(String doctorName, String patientName, String time, String clinicName, String date, String recordId) {
        String apiKey = "YOUR_API_KEY";
        String baseId = "YOUR_BASE_ID";
        String requestsTableName = "Requests";
        String appointmentsTableName = "Appointment";
        String requestsUrl = "https://api.airtable.com/v0/" + baseId + "/" + requestsTableName;
        String appointmentsUrl = "https://api.airtable.com/v0/" + baseId + "/" + appointmentsTableName;

        OkHttpClient client = new OkHttpClient();

        // Create JSON body for the new record in the Appointment table
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String jsonBody = "{\"fields\": {\"Doctor\": \"" + doctorName + "\", \"Patient\": \"" + patientName + "\", \"Time\": \"" + time + "\", \"Clinic\": \"" + clinicName + "\", \"Date\": \"" + date + "\"}}";
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        // Create request to add record to Appointment table
        Request addRequest = new Request.Builder()
                .url(appointmentsUrl)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            // Execute request to add record to Appointment table
            Response addResponse = client.newCall(addRequest).execute();
            if (addResponse.isSuccessful()) {
                // Record successfully added to Appointment table
                // Now delete the record from the Requests table
                deleteRecordFromAirtable(recordId);
            } else {
                // Handle unsuccessful addition to Appointment table
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

}
