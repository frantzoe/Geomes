package com.frantzoe.geomes.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.frantzoe.geomes.Utilities;
import com.frantzoe.geomes.fragments.RequestConfirmFragment;
import com.frantzoe.geomes.helpers.EventDatabase;
import com.frantzoe.geomes.models.Event;
import com.frantzoe.geomes.views.EventViewPager;
import com.frantzoe.geomes.R;
import com.frantzoe.geomes.fragments.RequestContactFragment;
import com.frantzoe.geomes.fragments.RequestDateFragment;
import com.frantzoe.geomes.fragments.RequestMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class RequestActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private EventViewPager eventViewPager;

    private Button btnBack;
    private Button btnNext;

    private String dateString;
    private LatLng latLng;
    private Set<String> numbers;

    private RequestMapFragment mapFragment;
    private RequestDateFragment dateFragment;
    private RequestContactFragment contactFragment;
    private RequestConfirmFragment confirmFragment;

    private List<Event> events;
    private EventDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        eventViewPager = findViewById(R.id.container);
        eventViewPager.setSwipable(false);
        eventViewPager.enableSmoothScroll();
        eventViewPager.setOffscreenPageLimit(3);
        eventViewPager.setAdapter(sectionsPagerAdapter);

        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);

        btnNext.setText("Location");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int current = eventViewPager.getCurrentItem();
                if (current > 0) {
                    eventViewPager.setCurrentItem(current - 1);
                    if (current == 1) {
                        btnNext.setText("Location");
                        btnBack.setVisibility(View.GONE);
                    } else if (current == 2) {
                        btnNext.setText("Contact");
                        btnBack.setText("Date");
                    } else if (current == 3) {
                        btnNext.setText("Valider");
                        btnBack.setText("Location");
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int current = eventViewPager.getCurrentItem();
                if (current == 0 && dateFragment.isDateSelected()) {
                    dateString = dateFragment.getDateString();
                    eventViewPager.setCurrentItem(current + 1);
                    btnBack.setVisibility(View.VISIBLE);
                    btnNext.setText("Contact");
                    btnBack.setText("Date");
                } else if (current == 1 && mapFragment.isLocaSelected()) {
                    latLng = mapFragment.getLatLng();
                    eventViewPager.setCurrentItem(current + 1);
                    btnNext.setText("Valider");
                    btnBack.setText("Location");
                } else if (current == 2 && contactFragment.isContSelected()) {
                    numbers = contactFragment.getNumbers();
                    eventViewPager.setCurrentItem(current + 1);
                    btnNext.setText("Confirmer");
                    btnBack.setText("Contacts");
                    Toast.makeText(RequestActivity.this, numbers.toString(), Toast.LENGTH_SHORT).show();
                } else if (current == 3) {
                    database = new EventDatabase(RequestActivity.this);
                    events = new ArrayList<>();
                    for (String number : numbers) {
                        events.add(new Event("og", latLng.latitude, latLng.longitude, false, number, null, dateString));
                    }
                    sendRequest();
                } else {
                    Toast.makeText(RequestActivity.this, "Veuillez faire votre selection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return dateFragment = new RequestDateFragment();
            } else if (position == 1) {
                return mapFragment = new  RequestMapFragment();
            } else if (position == 2){
                return contactFragment = new RequestContactFragment();
            } else {
                return confirmFragment = new RequestConfirmFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    private void sendRequest() {
        database = new EventDatabase(RequestActivity.this);
        for (Event event : events) {
            Utilities.sendSMS(event.getPhone(), "GeoMesReq|" + event.getUid() + "|" + event.getLatitude() + "|" + event.getLongitude() + "|" + event.getDate());
            database.addEvent(event.getUid(), event.getLatitude(), event.getLongitude(), event.getDirection(), event.isConfirmed(), event.getPhone(), event.getDate());
        }
        database.close();
        finish();
    }

    public List<Event> getEvents() {
        return events;
    }
}
