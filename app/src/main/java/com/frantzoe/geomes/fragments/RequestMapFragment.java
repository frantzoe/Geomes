package com.frantzoe.geomes.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.frantzoe.geomes.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class RequestMapFragment extends Fragment implements OnMapReadyCallback {

    private LatLng latLng;
    private boolean locaSelected;

    private GoogleMap mMap;
    private TextView txtPositionPick;

    private static final int MIN_ZOOM = 16;
    private static final int PICK_LOCATION = 2;

    public RequestMapFragment() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_request_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        txtPositionPick = rootView.findViewById(R.id.txtPositionPick);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtPositionPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {startActivityForResult(builder.build(getActivity()), PICK_LOCATION);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_LOCATION && resultCode == Activity.RESULT_OK && data != null) {
            final Place place = PlacePicker.getPlace(getContext(), data);
            String toastMsg = String.format("Place: %s", place.getName());
            Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
            latLng = place.getLatLng();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Point de Rencontre"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.setMinZoomPreference(MIN_ZOOM);
            locaSelected = true;
        }
    }

    public boolean isLocaSelected() {
        return locaSelected;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
