package com.frantzoe.geomes.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.frantzoe.geomes.adapters.EventAdapter;
import com.frantzoe.geomes.R;
import com.frantzoe.geomes.fragments.EventsCancelableFragment;
import com.frantzoe.geomes.fragments.EventsRecievedFragment;
import com.frantzoe.geomes.models.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton floatingActionButton;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        coordinatorLayout = findViewById(R.id.coordinator_layout);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RequestActivity.class));
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_SELECTED);
        navigation.setNestedScrollingEnabled(true);

        fragmentManager.beginTransaction().add(R.id.container, getCancellableFragment("confirmed")).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            final int itemid = item.getItemId();

            if (itemid == R.id.navigation_confirmed || itemid == R.id.navigation_requested) {
                fragmentManager.beginTransaction().replace(R.id.container, getCancellableFragment(itemid == R.id.navigation_confirmed ? "confirmed" : "requested")).commit();
                return true;
            } else if (itemid == R.id.navigation_recieved) {
                fragmentManager.beginTransaction().replace(R.id.container, new EventsRecievedFragment()).commit();
                return true;
            }
            return false;
        }
    };

    private EventsCancelableFragment getCancellableFragment(String navigation) {
        Bundle argument = new Bundle();
        argument.putString("navigation", navigation);
        EventsCancelableFragment eventsCancelableFragment = new EventsCancelableFragment();
        eventsCancelableFragment.setArguments(argument);
        return eventsCancelableFragment;
    }

    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }
}
