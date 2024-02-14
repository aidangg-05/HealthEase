package com.sp.healthease;

import android.content.Intent;
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


public class HomeMainDoctor extends Fragment {
    CardView telegramBtn;
    public HomeMainDoctor() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_main_doctor, container, false);
        telegramBtn = view.findViewById(R.id.telegramCardView);
        telegramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTelegram();
            }
        });
        return view;

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