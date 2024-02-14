package com.sp.healthease;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.sp.healthease.databinding.HomepagePatientBinding;
import android.util.Log;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.sp.healthease.databinding.HomepagePatientBinding;

public class Home_patient extends AppCompatActivity {

    private HomepagePatientBinding binding; // Declare binding here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomepagePatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve PatientData and AppointmentData from Intent
        PatientData patientData = getIntent().getParcelableExtra("patientData");
        AppointmentData appointmentData = getIntent().getParcelableExtra("appointmentData");

        if (patientData == null) {
            Log.e("Home_patient", "PatientData is null");
        }

// Check if appointmentData is null
        if (appointmentData == null) {
            Log.e("Home_patient", "AppointmentData is null");
        }

        // Pass PatientData and AppointmentData to HomeMain_patient fragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("patientData", patientData);
        bundle.putParcelable("appointmentData", appointmentData);

        HomeMain_patient homeMainPatientFragment = new HomeMain_patient();
        MapsFragment mapsFragment = new MapsFragment();
        mapsFragment.setArguments(bundle);
        homeMainPatientFragment.setArguments(bundle);


        // Replace the fragment
        replaceFragment(homeMainPatientFragment);

        // Bottom navigation setup
        binding.bottomNavigationViewPatient.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home_patient) {
                replaceFragment(homeMainPatientFragment);
            } else if (itemId == R.id.menu_book_patient) {
                mapsFragment.setArguments(bundle); // Pass the bundle to MapsFragment
                replaceFragment(mapsFragment); // Replace the current fragment with mapsFragment;
            } else if (itemId == R.id.menu_doc_patient) {
                replaceFragment(new Patients_DoctorContact());
            } else if (itemId == R.id.menu_profile_patient) {
                replaceFragment(new Patients_About());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.patient_framelayout, fragment);
        fragmentTransaction.commit();
    }
}
