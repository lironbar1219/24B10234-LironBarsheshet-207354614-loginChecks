package com.example.loginchecks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private Button loginButton;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private float[] gravity;
    private float[] geomagnetic;
    private float azimuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Register the sensors
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    private void findViews() {
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            int batteryPercentage = getBatteryPercentage();
            String password = passwordEditText.getText().toString();

            // Check if the password matches the battery percentage, device points North, and time is within range
            if (password.equals(String.valueOf(batteryPercentage)) && isPointingNorth() && isWithinTimeRange()) {
                // Grant access
                Toast.makeText(MainActivity.this, "Access Granted", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> {
                    // Start the next activity
                    Intent intent = new Intent(MainActivity.this, PassedLoginActivity.class);
                    intent.putExtra("BATTERY_PERCENTAGE", batteryPercentage + "");
                    intent.putExtra("AZIMUTH", azimuth + "");
                    Calendar calendar = Calendar.getInstance();
                    intent.putExtra("HOUR", calendar.get(Calendar.HOUR_OF_DAY) + "");
                    startActivity(intent);
                }, 2000);
            } else {
                Log.d("DEBUG", "Battery Percentage: " + batteryPercentage);
                Log.d("DEBUG", "Is Pointing North: " + isPointingNorth());
                Log.d("DEBUG", "Is Within Time Range: " + isWithinTimeRange());
                // Deny access
                Toast.makeText(MainActivity.this, "Access Denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getBatteryPercentage() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return (int) ((level / (float) scale) * 100);
    }

    private boolean isPointingNorth() {
        Log.d("DEBUG", "Azimuth: " + azimuth);
        return (azimuth >= 345 || azimuth <= 15);
    }

    private boolean isWithinTimeRange() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Log.d("DEBUG", "Hour: " + hour);
        return hour >= 12 && hour <= 23; // 12 PM to 11 PM
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                gravity = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                geomagnetic = event.values;
            if (gravity != null && geomagnetic != null) {
                float[] R = new float[9];
                float[] I = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
                if (success) {
                    float[] orientation = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    azimuth = (float) Math.toDegrees(orientation[0]);
                    if (azimuth < 0) azimuth += 360;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }
}
