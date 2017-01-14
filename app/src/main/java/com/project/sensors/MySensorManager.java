package com.project.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;

import com.project.HelpClasses.Constants;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Manager class that notifies the caller activity
 * about any sensor value changes
 */
public class MySensorManager extends Thread implements SensorEventListener {

    private String proxVal;
    private String lightVal;
    private SensorManager mSensorManager;
    private SensorCallback callback;
    private boolean sendValues = true;
    private Integer interval;

    public MySensorManager() {}

    public MySensorManager(Context context, int interval) {
        System.out.println("Creating sensor manager with interval: " + interval);
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        callback = (SensorCallback) context;
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        this.proxVal = String.valueOf(sensor.getMaximumRange());
        this.interval = interval;
    }

    @Override
    public void run() {
        System.out.println("Started sensor thread");
        /*
         * Register to the necessary sensors
         */
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);


        runAtInterval(interval);

    }

    private void runAtInterval(final Integer interval) {
        Looper.prepare();
         /*
         * Notify the caller activity each time the sensor values have changed.
         * We use an inner thread due to the fact that onSensorChanged
         * is only called when either sensor's value has changed whereas what we want
         * are steady updates to the caller activity
         */
        final Handler handler = new Handler();
        Runnable runn = new Runnable() {
            @Override
            public void run() {
                callback.onSensorValuesChanged(lightVal, proxVal);
                handler.postDelayed(this, interval);
            }
        };
        runn.run();

        Looper.loop();
    }

    public void unregisterListeners() {
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }

    /*
     * Updates the light and proximity values,
     * which are in turn sent to the caller activity by
     * the above inner thread
     */
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
        // We don't need this
    }
}
