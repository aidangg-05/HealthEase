package com.sp.healthease;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        appointments = new ArrayList<>();
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
        View rootView = inflater.inflate(R.layout.fragment_doctor_requests, container, false);
        list = rootView.findViewById(R.id.requestsRecycle);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setItemAnimator(new DefaultItemAnimator());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    new FetchAppointmentsTask().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    appointments = new ArrayList<>();
                }
            }
        });

        return rootView;
    }

    public class Appointment {
        private String recordId; // Add new field for Record_ID
        private String doctorName;
        private String patientName;
        private String time;
        private String clinicName;
        private String date;

        // Constructor
        public Appointment(String recordId, String doctorName, String patientName, String time, String clinicName, String date) {
            this.recordId = recordId;
            this.doctorName = doctorName;
            this.patientName = patientName;
            this.time = time;
            this.clinicName = clinicName;
            this.date = date;
        }

        // Getter and Setter for Record_ID
        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public String getPatientName() {
            return patientName;
        }

        public String getTime() {
            return time;
        }

        public String getClinicName() {
            return clinicName;
        }

        public String getDate() {
            return date;
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
            String recordId = record.getString("id"); // Assuming the Record_ID field is named "id"

            JSONObject fields = record.getJSONObject("fields");

            // Extract data from Airtable fields
            String doctorName = fields.getString("Doctor");
            String reqDate = fields.getString("Date");
            String patientName = fields.getString("Patient");
            String time = fields.getString("Time");
            String clinicName = fields.getString("Clinic");

            // Create Appointment object and add to list
            Appointment appointment = new Appointment(recordId, doctorName, patientName, time, clinicName, reqDate);
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
                appointments.clear(); // Clear the existing list
                appointments.addAll(result); // Add fetched appointments to the list
                adapter = new AppointmentAdapter(); // Initialize the adapter
                list.setAdapter(adapter); // Set the adapter to the RecyclerView
                adapter.notifyDataSetChanged(); // Notify adapter that data set has changed
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

            MaterialButton acceptbtn;
            MaterialButton rejectbtn;

            public AppointmentViewHolder(@NonNull View itemView) {
                super(itemView);
                patientNameTextView = itemView.findViewById(R.id.requestsPatientName);
                timeTextView = itemView.findViewById(R.id.requestsTime);
                dateTextView = itemView.findViewById(R.id.requestsdate);
                acceptbtn = itemView.findViewById(R.id.requestsAcceptbtn);
                rejectbtn = itemView.findViewById(R.id.requestsrejectbtn);

                // Set click listeners for accept and reject buttons
                acceptbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition(); // Get the adapter position of the clicked item
                        if (position != RecyclerView.NO_POSITION) {
                            Appointment appointment = appointments.get(position);
                            Log.d("AcceptButton", "Accept button clicked at position: " + position);
                            Log.d("AcceptButton", "Doctor Name: " + appointment.getDoctorName());
                            Log.d("AcceptButton", "Patient Name: " + appointment.getPatientName());

                            // Add the appointment record
                            new AddRecordTask().execute(appointment.getDoctorName(), appointment.getPatientName(),
                                    appointment.getTime(), appointment.getClinicName(),
                                    appointment.getDate(), appointment.getRecordId());
                        } else {
                            Log.e("AcceptButton", "Invalid position");
                        }
                    }
                });

                rejectbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the appointment at this position
                        Appointment appointment = appointments.get(getAdapterPosition());

                        // Delete the appointment record
                        new DeleteRecordTask().execute(appointment.getRecordId());
                    }
                });
            }
        }
    }

    private class AddRecordTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String doctorName = params[0];
            String patientName = params[1];
            String time = params[2];
            String clinicName = params[3];
            String date = params[4];
            String recordId = params[5];

            String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
            String baseId = "appDrji84bd55oOkv";
            String tableName = "Appointments";
            String appointmentsUrl = "https://api.airtable.com/v0/" + baseId + "/" + tableName;

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
                int responseCode = addResponse.code();
                Log.d("AddRecordTask", "Response code: " + responseCode); // Log response code
                if (addResponse.isSuccessful()) {
                    Log.d("AddRecordTask", "Record added successfully"); // Log success message
                    // Record successfully added to Appointment table
                    // Notify adapter that data set has changed
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Notify adapter that data set has changed
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    // Handle unsuccessful addition to Appointment table
                    Log.e("AddRecordTask", "Failed to add record. Response code: " + responseCode); // Log failure message
                    Log.e("AddRecordTask", "Error response body: " + addResponse.body().string()); // Log error response body
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("AddRecordTask", "IOException: " + e.getMessage()); // Log exception
                // Handle exception
            }
            return null;
        }
    }



    private class DeleteRecordTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String recordId = params[0];

            String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
            String baseId = "appDrji84bd55oOkv";
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
                    Log.d("DELETE", "Record with ID " + recordId + " deleted successfully");
                } else {
                    // Handle unsuccessful deletion
                    Log.e("DELETE", "Failed to delete record with ID " + recordId);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle exception
                Log.e("DELETE", "Exception while deleting record with ID " + recordId + ": " + e.getMessage());
            }
            return null;
        }
    }
}
