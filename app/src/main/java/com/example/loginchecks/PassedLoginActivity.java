package com.example.loginchecks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class PassedLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passed_login);
        Intent intent = getIntent();
        String batteryPercentage = intent.getStringExtra("BATTERY_PERCENTAGE");
        String azimuth = intent.getStringExtra("AZIMUTH");
        String time = intent.getStringExtra("HOUR");

        Log.d("DEBUG", "All checks passed:\nBattery Percentage: " + batteryPercentage + "\nDirection: " + azimuth + "\nTime: " + time);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

