package com.project.MQTT;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.project.HelpClasses.AlertBuilder;
import com.project.HelpClasses.Constants;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Manager class that manages all of the required MQTT operations
 */
public class MqttManager implements MqttCallback {
    private MqttClient client;
    private boolean connectionAcknowledged = false;
    private String id;
    private MqttConnectionCallback callback;
    private Integer interval = null;

    public MqttManager(String id, Activity callerActivity) {
        this.id = id;
        Context context = callerActivity;
        this.callback = (MqttConnectionCallback) callerActivity;

        Constants.CLIENT_TOPIC = Constants.CONNECTED_TOPIC + this.id;

        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        String connUrl = prefs.getString(Constants.MQTT_CONNECTION_URL, "tcp://192.168.1.3");
        String port = prefs.getString(Constants.MQTT_PORT, "1883");
        try {
            this.client = new MqttClient(connUrl + ":" + port, id, new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean connect() {
        try {
            this.client.setCallback(this);
            this.client.connect();

            // If the Desktop application is connected, it will acknowledge our
            // publication. If they have not, within the next 3 seconds,
            // we assume that it is not running
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.notifyCaller(connectionAcknowledged, interval);
                }
            }, 3 * Constants.SECONDS);
            /*
             * We request an acknowledgement response from the Desktop application
             * and subscribe to the appropriate topic to get the response
             */
            publish(Constants.REQUEST_ACKNOWLEDGEMENT_TOPIC, this.id);
            subscribe(Constants.CLIENT_TOPIC + Constants.CONNECTED_ACKNOWLEDGE_TOPIC, 2);
            finaliseConnection();
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void subscribe(String topic, int QoS) {
        try {
            this.client.subscribe(topic, QoS);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message) {
        MqttMessage m = new MqttMessage(message.getBytes());
        m.setQos(2);
        try {
            this.client.publish(topic, m);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void finaliseConnection() {
        if (this.client.isConnected()) {
            /*
             * Notify the desktop application about our existence
             * and subscribe to all the required topics
             */
            this.publish(Constants.NEW_CONNECTION_TOPIC, this.id);
            subscribe(Constants.CLIENT_TOPIC + Constants.TOPIC_WARNING, 2);
            subscribe(Constants.CLIENT_TOPIC + Constants.TOPIC_DANGER, 2);
            subscribe(Constants.CLIENT_TOPIC + Constants.TOPIC_STOP_WARNING, 2);
            subscribe(Constants.LAST_WILL_TOPIC, 2);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage m) throws Exception {
        /*
         * We handle the incoming messages
         */
        String message = m.toString();
        if (topic.equals(Constants.CONNECTED_TOPIC + this.id + Constants.CONNECTED_ACKNOWLEDGE_TOPIC)) {
            /*
             * If the desktop application acknowledges us, it also sends us the interval at which we should
             * send it values
             */
            connectionAcknowledged = true;
            interval = Integer.parseInt(m.toString());
        } else if (topic.equals(Constants.CLIENT_TOPIC + Constants.TOPIC_WARNING)) {
            if (message.equals(Constants.MESSAGE_WARNING)) {
                callback.soundWarning();
            }
        } else if (topic.equals(Constants.CLIENT_TOPIC + Constants.TOPIC_DANGER)) {
            if (message.equals(Constants.MESSAGE_DANGER)) {
                callback.soundDanger();
            }
        } else if (topic.equals(Constants.CLIENT_TOPIC + Constants.TOPIC_STOP_WARNING)) {
            if (message.equals(Constants.MESSAGE_STOP_WARNING)) {
                callback.stopSounds();
            }
        } else if (topic.equals(Constants.LAST_WILL_TOPIC)) {
            /*
             * If the Desktop application disconnects ungracefully
             * (it always does when we close the JavaFX window)
             * We will be notified by the broker and take the user into
             * offline mode
             */
            if (message.equals(Constants.MAIN_CLIENT_DISCONNECTING)) {
                callback.notifyCaller(false, null);
            }
        }
    }

    /*
     * If we ungracefully lose connection to the MQTT broker,
     * we notify the user
     */
    @Override
    public void connectionLost(Throwable cause) {
        cause.printStackTrace();
        callback.notifyCaller(false, null);
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
