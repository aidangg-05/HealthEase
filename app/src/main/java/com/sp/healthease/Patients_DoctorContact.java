package com.sp.healthease;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Patients_DoctorContact extends Fragment {
    private List<Doctorcontact> doctorcontacts;
    private RecyclerView list1;
    private DoctorcontactAdapter adapter;

    public Patients_DoctorContact() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            new FetchDoctorcontactTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
            doctorcontacts = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patients__doctor_contact, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        list1 = view.findViewById(R.id.patientDOCcontactRecyclerView);
        list1.setHasFixedSize(true);
        list1.setLayoutManager(layoutManager);
        list1.setItemAnimator(new DefaultItemAnimator());
        list1.setAdapter(adapter);
        return view;
    }

    public class Doctorcontact {
        private String DocMail;
        private String DocPassword;
        private String DocField;
        private String DocClinic;
        private String DocFullName;
        private String DocTelegram;

        void setDoctorField(String DocField){
            this.DocField = DocField;
        }
        void setDoctorFullName(String DocFullName){
            this.DocFullName = DocFullName;
        }
        void setDoctorTelegram(String DocTelegram){
            this.DocTelegram = DocTelegram;
        }
        String getDoctorField(){return DocField;}
        String getDoctorFullName(){return DocFullName;}
        String getDoctorTelegram(){return DocTelegram;}
    }

    private List<Doctorcontact> fetchDataFromAirtable() throws IOException, JSONException {
        List<Doctorcontact> doctorcontacts = new ArrayList<>();

        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Doctor Registration";
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
            String DoctorField = fields.getString("Field");
            String DoctorFullName = fields.getString("Full_Name");
            String DoctorTelegram = fields.getString("Telegram");


            // Create contact object and add to list
            Doctorcontact doctorcontact = new Doctorcontact();
            doctorcontact.setDoctorField(DoctorField);
            doctorcontact.setDoctorFullName(DoctorFullName);
            doctorcontact.setDoctorTelegram(DoctorTelegram);

            doctorcontacts.add(doctorcontact);
        }

        return doctorcontacts;
    }

    private class FetchDoctorcontactTask extends AsyncTask<Void, Void, List<Doctorcontact>> {
        @Override
        protected List<Doctorcontact> doInBackground(Void... voids) {
            try {
                return fetchDataFromAirtable();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Doctorcontact> result) {
            super.onPostExecute(result);
            if (result != null) {
                doctorcontacts = result;
                if (adapter == null) {
                    adapter = new DoctorcontactAdapter();
                    list1.setAdapter(adapter); // Set adapter for the RecyclerView
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    public class DoctorcontactAdapter extends RecyclerView.Adapter<DoctorcontactAdapter.DoctorcontactViewHolder> {
        @NonNull
        @Override
        public DoctorcontactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate your item layout here
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_doctorcontactrow, parent, false);
            return new DoctorcontactViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DoctorcontactViewHolder holder, int position) {
            Doctorcontact doctorcontact = doctorcontacts.get(position);
            holder.DoctorNameTextView.setText("Name: Dr. " + doctorcontact.getDoctorField());
            holder.DoctorTeleTextView.setText("Telegram ID: " + doctorcontact.getDoctorTelegram());
            holder.DoctorFieldTextView.setText("Field: " + doctorcontact.getDoctorField());
        }

        @Override
        public int getItemCount() {
            return doctorcontacts.size();
        }

        public class DoctorcontactViewHolder extends RecyclerView.ViewHolder {

            TextView DoctorNameTextView;
            TextView DoctorTeleTextView;
            TextView DoctorFieldTextView;
            MaterialButton TelegramPatientbtn;

            public DoctorcontactViewHolder(@NonNull View itemView) {
                super(itemView);
                DoctorNameTextView = itemView.findViewById(R.id.DocContactName);
                DoctorTeleTextView = itemView.findViewById(R.id.DocTelegramID);
                DoctorFieldTextView = itemView.findViewById(R.id.DocFieldstxt);
                TelegramPatientbtn = itemView.findViewById(R.id.ContactTeleDocButton);
                TelegramPatientbtn.setOnClickListener(onOpen);
            }
            private View.OnClickListener onOpen = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTelegram();
                }
            };
            // Method to open Telegram app
            private void openTelegram() {
                // Check if Telegram is installed on the device
                if (isTelegramInstalled()) {
                    // If Telegram is installed, create an Intent to launch it
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://telegram.me/aidngg"));
                    startActivity(intent);
                } else {
                    // If Telegram is not installed, display a toast message
                    Toast.makeText(getActivity(), "Telegram is not installed on your device", Toast.LENGTH_SHORT).show();
                }
            }
            // Method to check if Telegram is installed on the device
            private boolean isTelegramInstalled() {
                PackageManager pm = getActivity().getPackageManager();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://telegram.me/"));

                // Check if there's any app that can handle the Intent
                return intent.resolveActivity(pm) != null;
            }
        }
    }
}
