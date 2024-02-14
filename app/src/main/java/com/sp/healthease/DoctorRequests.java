package com.sp.healthease;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DoctorRequests extends Fragment {
    private List<Appointment> appointments;
    Context context;
    Inflater layoutInflater;
    Activity view;
    public DoctorRequests() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            try {
                appointments = fetchDataFromAirtable();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_requests, container, false);

        return view;
    }
    public class Appointment {
        private String doctorName;
        private String patientName;
        private String time;
        private String clinicName;

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

        String getPatientName() {
            return patientName;
        }
        String getTime(){
            return time;
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
                .url(url + baseId + "/" + tableName)
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
            String patientName = fields.getString("Patient");
            String time = fields.getString("Time");
            String clinicName = fields.getString("Clinic");

            // Create Appointment object and add to list
            Appointment appointment = new Appointment();
            appointment.setDoctorName(doctorName);
            appointment.setPatientName(patientName);
            appointment.setTime(time);
            appointment.setClinicName(clinicName);

            appointments.add(appointment);
        }

        return appointments;
    }

}
