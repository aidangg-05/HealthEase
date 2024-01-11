package com.sp.healthease;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.FirebaseDatabase;


import android.os.Bundle;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}