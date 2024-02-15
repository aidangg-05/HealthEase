package com.sp.healthease;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DoctorUpcomingAppointment extends Fragment {

    private List<Appointment> appointments;
    private RecyclerView list;
    private AppointmentAdapter adapter;

    public DoctorUpcomingAppointment() {
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
        View rootView = inflater.inflate(R.layout.fragment_doctor_upcoming_appointment, container, false);
        list = rootView.findViewById(R.id.docupcomingappointmentsrecycler);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setItemAnimator(new DefaultItemAnimator());
        adapter = new AppointmentAdapter();
        list.setAdapter(adapter);
        return rootView;
    }

    private List<Appointment> fetchDataFromAirtable() throws IOException, JSONException {
        List<Appointment> appointments = new ArrayList<>();
        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Appointments";
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
            String doctorName = fields.getString("Doctor");
            String reqDate = fields.getString("Date");
            String patientName = fields.getString("Patient");
            String time = fields.getString("Time");
            String clinicName = fields.getString("Clinic");
            Appointment appointment = new Appointment(doctorName, patientName, time, clinicName, reqDate);
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
                appointments.clear();
                appointments.addAll(result);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
        @NonNull
        @Override
        public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.docupcomingappointmentrow, parent, false);
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
                patientNameTextView = itemView.findViewById(R.id.appointpatientnamedoc);
                timeTextView = itemView.findViewById(R.id.appointTimedoc);
                dateTextView = itemView.findViewById(R.id.appointdatedoc);
            }
        }
    }

    public class Appointment {
        private String doctorName;
        private String patientName;
        private String time;
        private String clinicName;
        private String date;

        public Appointment(String doctorName, String patientName, String time, String clinicName, String date) {
            this.doctorName = doctorName;
            this.patientName = patientName;
            this.time = time;
            this.clinicName = clinicName;
            this.date = date;
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
}
