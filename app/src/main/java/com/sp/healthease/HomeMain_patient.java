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
            userEmail = args.getString("userEmail");
        } else {
            Log.e("Home_patient", "Arguments are null");
        }

        // Log userEmail for debugging
        Log.d("Home_patient", "User Email: " + userEmail);

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
