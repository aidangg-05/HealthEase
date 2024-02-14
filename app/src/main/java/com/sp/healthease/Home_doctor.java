package com.sp.healthease;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;

import com.sp.healthease.databinding.HomepageDoctorBinding;

public class Home_doctor extends AppCompatActivity {
    HomepageDoctorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomepageDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve DoctorData from Intent
        DoctorData doctorData = getIntent().getParcelableExtra("doctorData");

        // Check if doctorData is null
        if (doctorData == null) {
            Log.e("Home_doctor", "DoctorData is null");
            // Optionally handle this case, for example by showing an error message to the user
        } else {
            // Pass DoctorData to fragments
            Bundle bundle = new Bundle();
            bundle.putParcelable("doctorData", doctorData);

            // Create the HomeMainDoctor fragment and set its arguments
            HomeMainDoctor homeMainDoctorFragment = new HomeMainDoctor();
            homeMainDoctorFragment.setArguments(bundle);

            // Replace the fragment
            replaceFragment(homeMainDoctorFragment);

            // Bottom navigation setup
            binding.bottomNavigationViewDoc.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_home_doc) {
                    replaceFragment(new HomeMainDoctor());
                } else if (itemId == R.id.menu_patient_doc) {
                    DoctorPatientsDetails doctorPatientsDetails = new DoctorPatientsDetails();
                    doctorPatientsDetails.setArguments(bundle); // Pass the bundle to the fragment
                    replaceFragment(doctorPatientsDetails);
                } else if (itemId == R.id.menu_requests_doc) {
                    replaceFragment(new DoctorRequests());
                } else if (itemId == R.id.menu_profile_doc) {
                    replaceFragment(new DoctorAbout());
                }
                return true;
            });
        }
    }



    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.doctor_framelayout, fragment);
        fragmentTransaction.commit();
    }
}
