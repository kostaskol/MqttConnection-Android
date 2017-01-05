package com.project.sensors;

public interface SensorCallback {
    void onSensorValuesChanged(String lightVal, String proxVal);
}
