package com.project.MQTT;


public interface MqttConnectionCallback {
    void notifyCaller(boolean ack);
    void soundWarning();
    void soundDanger();
    void stopSounds();
}