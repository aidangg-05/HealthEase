package com.sp.healthease;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Button;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;


public class HomeMainDoctor extends Fragment {
    private static final String PREF_DOCTOR_NAME = "doctor_name";
    CardView telegramBtn;
    private DoctorData doctorData;

    public HomeMainDoctor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_main_doctor, container, false);

        // Retrieve doctorData from arguments
        Bundle args = getArguments();
        if (args != null) {
            doctorData = args.getParcelable("doctorData");

            // Check if doctorData is not null
            if (doctorData != null) {
                // Example usage: Set the full name of the doctor in a TextView
                TextView doctorNameTextView = rootView.findViewById(R.id.username_doctor);
                doctorNameTextView.setText(doctorData.getFullName());


            } else {
                Log.e("HomeMainDoctor", "DoctorData is null");
            }
        } else {
            Log.e("HomeMainDoctor", "Arguments bundle is null");
        }

        // Set up click listener for CardView
        CardView telegramCardView = rootView.findViewById(R.id.telegramCardView);
        telegramCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTelegram();
            }
        });


        return rootView;
    }


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
