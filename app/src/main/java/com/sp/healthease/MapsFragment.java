package com.sp.healthease;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private Marker marker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }



    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String markerTitle = marker.getTitle();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.patient_framelayout,new Patients_BookAppointment());
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng Clinic0 = new LatLng(1.3919080907160248, 103.74346730396219);
        Marker marker0 = mMap.addMarker(new MarkerOptions()
                .position(Clinic0)
                .title("Pheonix Medical Group")
        );
        marker0.showInfoWindow();
        LatLng Clinic1 = new LatLng(1.44613644706944, 103.76955983095337);
        Marker marker1 = mMap.addMarker(new MarkerOptions()
                .position(Clinic1)
                .title("Raffles Medical")
        );
        marker1.showInfoWindow();
        LatLng Clinic2 = new LatLng(1.35758569866878, 103.68578908640269);
        Marker marker2 = mMap.addMarker(new MarkerOptions()
                .position(Clinic2)
                .title("Tom&Jerry Medical")
        );
        marker2.showInfoWindow();
        LatLng Clinic3 = new LatLng(1.3472888854779945, 103.75994679469345);
        Marker marker3 = mMap.addMarker(new MarkerOptions()
                .position(Clinic3)
                .title("Advent Time Medical")
        );
        marker3.showInfoWindow();
        LatLng Clinic4 = new LatLng(1.4022047114733798, 103.83616443932563);
        Marker marker4 = mMap.addMarker(new MarkerOptions()
                .position(Clinic4)
                .title("Healthy U Medical")
        );
        marker4.showInfoWindow();
        LatLng mid = new LatLng(1.3751315913986857, 103.81247420076069);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mid));
    }
}