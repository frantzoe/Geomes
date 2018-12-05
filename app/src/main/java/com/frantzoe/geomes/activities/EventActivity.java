package com.frantzoe.geomes.activities;

import android.os.Bundle;
import android.app.Activity;

import com.frantzoe.geomes.R;

public class EventActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
