package com.project.sensors;

/*
 * Both OnlineMode and OfflineMode implement this interface.
 * The MySensorManager class uses this callback to notify the
 * caller activity about any sensor value changes
 */
public interface SensorCallback {
    void onSensorValuesChanged(String lightVal, String proxVal);
}
