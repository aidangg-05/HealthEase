package com.sp.healthease;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Patients_BookAppointment extends Fragment {
    private TextView clinicNameTextView;
    public Patients_BookAppointment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patients__book_appointment, container, false);
        clinicNameTextView = view.findViewById(R.id.clinicName);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String markerTitle = bundle.getString("markerTitle");
            if (markerTitle != null) {
                clinicNameTextView.setText("Clinic: " + markerTitle);
            }
        }
        return view;
    }


}