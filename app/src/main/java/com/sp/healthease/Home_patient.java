package com.sp.healthease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.sp.healthease.databinding.HomepagePatientBinding;

public class Home_patient extends AppCompatActivity {
    HomepagePatientBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomepagePatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeMain_patient());

        binding.bottomNavigationViewPatient.setOnItemSelectedListener(item -> {
            int itemId =item.getItemId();
            if(itemId == R.id.menu_home_patient){
                replaceFragment(new HomeMain_patient());
            } else if (itemId == R.id.menu_book_patient) {
                replaceFragment(new Patients_BookAppointment());
            } else if (itemId == R.id.menu_doc_patient) {
                replaceFragment(new Patients_DoctorContact());
            } else if (itemId == R.id.menu_profile_patient) {
                replaceFragment(new Patients_About());
            } return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.patient_framelayout,fragment);
        fragmentTransaction.commit();
    }
}
