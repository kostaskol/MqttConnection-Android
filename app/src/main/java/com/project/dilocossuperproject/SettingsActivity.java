package com.project.dilocossuperproject;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.SeekableByteChannel;

public class SettingsActivity extends AppCompatActivity {

    Spinner lightSettings, frequencySet;

    SeekBar proximitySettings;

    TextView maxRange;
    TextView progressTv;

    Button save;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    int progressNow;
    float max;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialise();

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
                editor.putInt(Constants.LIGHT,Integer.parseInt(tmp));
                editor.putFloat(Constants.PROX, progressNow);
                tmp= frequencySet.getSelectedItem().toString();
                if(tmp.equals("SLOW")){
                    editor.putInt(Constants.FREQ,SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    editor.putInt(Constants.FREQ,SensorManager.SENSOR_DELAY_GAME);
                }
                editor.apply();
                finish();
            }
        });


    }

    private void initialise() {
        /*
         * Initialise all UI Views
         */
        frequencySet= (Spinner)  findViewById(R.id.freq_set);
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

        String[] connectArray = {"SLOW","FAST"};
        ArrayAdapter<String> connect= new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,connectArray);
        frequencySet.setAdapter(connect);

        /*
         * Set spinner's default position at user's last choice
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
    }
}
