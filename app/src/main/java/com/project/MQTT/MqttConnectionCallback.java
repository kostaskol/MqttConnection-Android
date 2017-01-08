package com.project.MQTT;

/*
 * The MqttManager class uses this interface to notify
 * the caller activity about any messages sent by the Desktop app
 */
public interface MqttConnectionCallback {
    void notifyCaller(boolean ack, Integer interval);
    void soundWarning();
    void soundDanger();
    void stopSounds();
}