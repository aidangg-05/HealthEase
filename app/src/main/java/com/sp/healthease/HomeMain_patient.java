package com.sp.healthease;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeMain_patient extends Fragment {
    private String fullName;
    private String doctorName;
    private String clinicName;
    private String appointmentDate;
    private String appointmentTime;


    public HomeMain_patient() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_main_patient, container, false);

        // Retrieve email from arguments
        String userEmail = "";
        Bundle args = getArguments();
        if (args != null) {
            PatientData patientData = args.getParcelable("patientData");
            AppointmentData appointmentData = args.getParcelable("appointmentData");

            // Check if patientData and appointmentData are not null
            if (patientData != null && appointmentData != null) {
                // Set the full name of the patient in the username_patient TextView
                TextView usernameTextView = rootView.findViewById(R.id.username_patient);
                usernameTextView.setText(patientData.getFullName());

                // Set appointment data in the corresponding TextViews
                TextView doctorNameTextView = rootView.findViewById(R.id.appoint_doctorName_preview);
                doctorNameTextView.setText(appointmentData.getDoctorName());

                TextView clinicNameTextView = rootView.findViewById(R.id.appoint_clinic_preview);
                clinicNameTextView.setText(appointmentData.getClinicName());

                TextView appointmentDateTextView = rootView.findViewById(R.id.appoint_date_preview);
                appointmentDateTextView.setText(appointmentData.getAppointmentDate());

                TextView appointmentTimeTextView = rootView.findViewById(R.id.appoint_time_preview);
                appointmentTimeTextView.setText("Time:" + appointmentData.getAppointmentTime());
            } else {
                Log.e("HomeMain_patient", "PatientData or AppointmentData is null");
            }
        } else {
            Log.e("HomeMain_patient", "Arguments bundle is null");
        }

        // Log userEmail for debugging
        Log.d("HomeMain_patient", "User Email: " + userEmail);

        // Set up click listeners for CardViews
        CardView appointmentCardView = rootView.findViewById(R.id.cardView_appointment);
        CardView dietCardView = rootView.findViewById(R.id.cardView_diets);

        appointmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAppointment();
            }
        });

        dietCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDiets();
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the data to the bundle
        outState.putString("fullName", fullName);
        outState.putString("doctorName", doctorName);
        outState.putString("clinicName", clinicName);
        outState.putString("appointmentDate", appointmentDate);
        outState.putString("appointmentTime", appointmentTime);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Restore the data from the bundle
        if (savedInstanceState != null) {
            fullName = savedInstanceState.getString("fullName");
            doctorName = savedInstanceState.getString("doctorName");
            clinicName = savedInstanceState.getString("clinicName");
            appointmentDate = savedInstanceState.getString("appointmentDate");
            appointmentTime = savedInstanceState.getString("appointmentTime");
        }
    }

    private void navigateToAppointment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.patient_framelayout, new appointmentPatient());
        fragmentTransaction.commit();
    }

    private void navigateToDiets() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.patient_framelayout, new dietsPatients());
        fragmentTransaction.commit();
    }
}
