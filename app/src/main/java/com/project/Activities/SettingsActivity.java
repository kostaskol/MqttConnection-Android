package com.project.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.project.GPS.OnlineAvailabilityChecker;
import com.project.HelpClasses.AlertBuilder;
import com.project.HelpClasses.Constants;

public class SettingsActivity extends AppCompatActivity {
    Switch modeSwitch;
    Spinner lightSettings;
    SeekBar proximitySettings;
    TextView maxRange;
    TextView progressTv;
    Button save;
    EditText connUrlText;
    EditText portText;

    int startedFrom;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    int progressNow;
    float max;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.startedFrom = getIntent().getIntExtra(Constants.FROM_MODE, Constants.MODE_OFFLINE);

        initialise();
        createListeners();
    }

    private void initialise() {

        /*
         * Initialise the shared preferences instance
         */
        prefs=getSharedPreferences(Constants.PREFS,MODE_PRIVATE);

        int prefMode = prefs.getInt(Constants.PREFERRED_MODE, Constants.MODE_OFFLINE);

        modeSwitch = (Switch) findViewById(R.id.mode_switch);

        modeSwitch.setChecked(prefMode == Constants.MODE_ONLINE);

         /*
         * Initialise all UI Views
         */
        lightSettings= (Spinner) findViewById(R.id.light_set);
        proximitySettings= (SeekBar) findViewById(R.id.proximity_set);
        save = (Button)findViewById(R.id.save_button);
        maxRange = (TextView) findViewById(R.id.seek_max_range);
        progressTv = (TextView) findViewById(R.id.progress_tv);

        /*
         * Initialise the shared preferences instance
         */
        prefs=getSharedPreferences(Constants.PREFS,MODE_PRIVATE);

        /*
         * Initialise the sensor manager (required for getting the proximity sensor's maximum range
         */
        SensorManager sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor proxSensor = sManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        /*
         * Set seekbar's maximum value
         */
        maxRange.setText(String.valueOf(proxSensor.getMaximumRange()));
        max = proxSensor.getMaximumRange();

        /*
         * Create an adapter for the spinner with default values
         */
        String[] adapterArray= {"50","60","75"};
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,adapterArray);
        lightSettings.setAdapter(adapter);

        /*
         * Set spinner's default position to user's last choice
         */
        int position = prefs.getInt(Constants.LIGHT, 50);
        switch (position) {
            case 50:
                position = 0;
                break;
            case 60:
                position = 1;
                break;
            case 75:
                position = 2;
                break;
            default:
                //I should never be in here
        }
        lightSettings.setSelection(position);

        proximitySettings.setProgress(prefs.getInt(Constants.LIGHT, 0));

        connUrlText = (EditText) findViewById(R.id.settings_online_conn_url);

        portText = (EditText) findViewById(R.id.settings_online_port);

        String connUrl = prefs.getString(Constants.MQTT_CONNECTION_URL, "tcp://192.168.1.3");
        connUrlText.setText(connUrl);

        String port = prefs.getString(Constants.MQTT_PORT, "1883");
        portText.setText(port);
    }

    private void createListeners() {

        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    OnlineAvailabilityChecker checker = new OnlineAvailabilityChecker(SettingsActivity.this);
                    checker.start();
                    try {
                        checker.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!checker.hasPermission()) {
                        checker.requestPermission();
                        modeSwitch.setChecked(false);
                    } else {
                        if (!checker.isInternetAvailable()) {
                            String title = "Internet connection required";
                            String message = "This feature requires the device to be connected to the internet\n" +
                                    "Please connect to the internet (WiFi/Mobile Data) and try again";
                            AlertBuilder alert = new AlertBuilder(SettingsActivity.this, message, title, null, null);
                            alert.showDialog();
                        }
                    }

                    if (!checker.isGpsAvailable()) {
                        String title = "GPS Availability Required";
                        String message = "This feature requires the device to have location availability\n" +
                                "Would you like to enable it now?";
                        DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        };

                        DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SettingsActivity.this, "This feature requires GPS availability." +
                                        "Please enable it and try again.", Toast.LENGTH_LONG).show();
                            }
                        };
                        AlertBuilder alert = new AlertBuilder(SettingsActivity.this, message, title, positive, negative);
                        alert.showDialog();
                        modeSwitch.setChecked(false);
                    }
                }
            }
        });

        proximitySettings.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /*
                 * Get the percentage of the progress bar in terms of
                 * the proximity sensor's maximum range
                 */
                //progressNow=(progress/100f)*max;
                progressNow = progress;
                progressTv.setText(progressNow + "/" + max);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        /*
         * On click listener to save user's preferences
         */
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp =lightSettings.getSelectedItem().toString();
                editor = prefs.edit();
                if (modeSwitch.isChecked()) {
                    editor.putInt(Constants.PREFERRED_MODE, Constants.MODE_ONLINE);
                } else {
                    editor.putInt(Constants.PREFERRED_MODE, Constants.MODE_OFFLINE);
                }

                editor.putInt(Constants.LIGHT,Integer.parseInt(tmp));
                editor.putFloat(Constants.PROX, progressNow);

                editor.putString(Constants.MQTT_CONNECTION_URL, connUrlText.getText().toString());
                editor.putString(Constants.MQTT_PORT, portText.getText().toString());

                editor.apply();
                if (startedFrom == Constants.MODE_OFFLINE) {
                    finish();
                    startActivity(new Intent(SettingsActivity.this, OfflineMode.class));
                } else {
                    finish();
                    startActivity(new Intent(SettingsActivity.this, OnlineMode.class));
                }
            }
        });
    }
}
