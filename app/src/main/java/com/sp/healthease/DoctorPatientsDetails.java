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

public class DoctorPatientsDetails extends Fragment {
    private List<PatientContacts> patientContactsList;
    private RecyclerView recyclerView;
    private PatientContactsAdapter adapter;

    public DoctorPatientsDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            new FetchPatientContactsTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
            patientContactsList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_patients_details, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.patientsdetailrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    public class PatientContacts {
        private String fullName;
        private String age;
        private String bloodGroup;
        private String medicalHistory;

        public String getFullName() {
            return fullName;
        }

        public String getAge() {
            return age;
        }

        public String getBloodGroup() {
            return bloodGroup;
        }

        public String getMedicalHistory() {
            return medicalHistory;
        }
    }

    private List<PatientContacts> fetchDataFromAirtable() throws IOException, JSONException {
        List<PatientContacts> patientContactsList = new ArrayList<>();

        String apiKey = "pat9g6F7LvXbnFNcC.dde538f123da8f01fd5b9b83ac243b1f283f37500f887a0f6975767f562a62fb";
        String baseId = "appDrji84bd55oOkv";
        String tableName = "Patient Registration";
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
            String fullName = fields.getString("Full_Name");
            String age = fields.getString("Age");
            String bloodGroup = fields.getString("Blood_Group");
            String medicalHistory = fields.getString("Medical_History");

            // Create PatientContacts object and add to list
            PatientContacts patientContacts = new PatientContacts();
            patientContacts.fullName = fullName;
            patientContacts.age = age;
            patientContacts.bloodGroup = bloodGroup;
            patientContacts.medicalHistory = medicalHistory;

            patientContactsList.add(patientContacts);
        }

        return patientContactsList;
    }

    private class FetchPatientContactsTask extends AsyncTask<Void, Void, List<PatientContacts>> {
        @Override
        protected List<PatientContacts> doInBackground(Void... voids) {
            try {
                return fetchDataFromAirtable();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<PatientContacts> result) {
            super.onPostExecute(result);
            if (result != null) {
                patientContactsList = result;
                if (adapter == null) {
                    adapter = new PatientContactsAdapter();
                    recyclerView.setAdapter(adapter); // Set adapter for the RecyclerView
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    public class PatientContactsAdapter extends RecyclerView.Adapter<PatientContactsAdapter.PatientContactsViewHolder> {
        @Override
        public PatientContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_patientcontactrow, parent, false);
            return new PatientContactsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PatientContactsViewHolder holder, int position) {
            PatientContacts patientContacts = patientContactsList.get(position);
            holder.fullNameTextView.setText("Name: " + patientContacts.getFullName());
            holder.ageTextView.setText("Age: " + patientContacts.getAge());
            holder.bloodGroupTextView.setText("Blood Grp: " + patientContacts.getBloodGroup());
            holder.medicalHistoryTextView.setText("Medical Hist: " + patientContacts.getMedicalHistory());
        }

        @Override
        public int getItemCount() {
            return patientContactsList.size();
        }

        public class PatientContactsViewHolder extends RecyclerView.ViewHolder {
            TextView fullNameTextView;
            TextView ageTextView;
            TextView bloodGroupTextView;
            TextView medicalHistoryTextView;
            MaterialButton PatientsContactbtn;

            public PatientContactsViewHolder(View itemView) {
                super(itemView);
                fullNameTextView = itemView.findViewById(R.id.PatientContactName);
                ageTextView = itemView.findViewById(R.id.PatientAge);
                bloodGroupTextView = itemView.findViewById(R.id.PatientBloodGrp);
                medicalHistoryTextView = itemView.findViewById(R.id.PatientMedicalHist);
                PatientsContactbtn = itemView.findViewById(R.id.ContactPatientButton);
                PatientsContactbtn.setOnClickListener(onTele);
            }
            private View.OnClickListener onTele = new View.OnClickListener() {
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
                    intent.setData(Uri.parse("https://telegram.me/"));
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
