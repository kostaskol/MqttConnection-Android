package com.project.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.Context.SENSOR_SERVICE;

public class MySensorManager implements SensorEventListener {

    private String proxVal;
    private String lightVal;
    private SensorManager mSensorManager;

    public MySensorManager(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        this.proxVal = String.valueOf(sensor.getMaximumRange());
    }

    public void start() {
        Log.d ("DEBUG", "Sensor Manager called");
        /*
         * Get the necessary sensor manager
         */
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    public String[] getValues() {
        return new String[] {this.lightVal, this.proxVal};
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            this.lightVal = String.valueOf(event.values[0]);
        } else if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
            this.proxVal = String.valueOf(event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
