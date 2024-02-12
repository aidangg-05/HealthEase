package com.sp.healthease;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sp.healthease.databinding.HomepageDoctorBinding;
import com.sp.healthease.databinding.HomepagePatientBinding;

public class Home_doctor extends AppCompatActivity {
    HomepageDoctorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomepageDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeMainDoctor());

        binding.bottomNavigationViewDoc.setOnItemSelectedListener(item -> {
            int itemId =item.getItemId();
            if(itemId == R.id.menu_home_doc){
                replaceFragment(new HomeMainDoctor());
            } else if (itemId == R.id.menu_patient_doc) {
                replaceFragment(new DoctorPatientsDetails());
            } else if (itemId == R.id.menu_requests_doc) {
                replaceFragment(new DoctorRequests());
            } else if (itemId == R.id.menu_profile_doc) {
                replaceFragment(new DoctorAbout());
            } return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.doctor_framelayout,fragment);
        fragmentTransaction.commit();
    }
}
