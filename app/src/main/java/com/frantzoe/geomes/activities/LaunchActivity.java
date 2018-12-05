package com.frantzoe.geomes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.frantzoe.geomes.R;
import com.frantzoe.geomes.Utilities;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        if (!Utilities.arePermissionsGranted(LaunchActivity.this)) {
            Utilities.requestPermissions(LaunchActivity.this);
            Toast.makeText(this, "Not Granted", Toast.LENGTH_SHORT).show();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                    finish();
                }
            }, 1000);
        }
    }
}
