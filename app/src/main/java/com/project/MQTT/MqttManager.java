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


public class MqttManager implements MqttCallback {
    private MqttClient client;
    private boolean connectionAcknowledged = false;

    private Context context;

    private String id;

    private MqttConnectionCallback callback;


    public MqttManager(String id, Activity callerActivity) {
        this.id = id;
        this.context = callerActivity;
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
        Log.w("MQTT", "Mqtt Manager connecting");
        try {
            this.client.setCallback(this);
            this.client.connect();

            // If the Main Client is connected, it will acknowledge our
            // publication. If they have not, within the next 3 seconds,
            // we assume that it is not running
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.notifyCaller(connectionAcknowledged);
                }
            }, 3 * 1000);
            publish(Constants.REQUEST_ACKNOWLEDGEMENT_TOPIC, this.id);
            subscribe(Constants.CLIENT_TOPIC + Constants.CONNECTED_ACKNOWLEDGE_TOPIC, 2);
            finaliseConnection();
            Log.d("MQTT", "Ack topic: " + Constants.CONNECTED_TOPIC + this.id + Constants.CONNECTED_ACKNOWLEDGE_TOPIC);
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void subscribe(String topic, int QoS) {
        Log.d("MQTT", "Subscribing to topic: " + topic);
        try {
            this.client.subscribe(topic, QoS);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message) {
        MqttMessage m = new MqttMessage(message.getBytes());
        // m.setQos(0);
        try {
            this.client.publish(topic, m);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void finaliseConnection() {
        if (this.client.isConnected()) {
            this.publish(Constants.NEW_CONNECTION_TOPIC, this.id);
            subscribe(Constants.CLIENT_TOPIC + Constants.TOPIC_WARNING, 2);
            subscribe(Constants.CLIENT_TOPIC + Constants.TOPIC_DANGER, 2);
            subscribe(Constants.CLIENT_TOPIC + Constants.TOPIC_STOP_WARNING, 2);
            subscribe(Constants.LAST_WILL_TOPIC, 2);
        } else {
            Log.e("MQTT", "Mqtt Client is not connected (why?)");
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage m) throws Exception {

        if (topic.equals(Constants.CONNECTED_TOPIC + this.id + Constants.CONNECTED_ACKNOWLEDGE_TOPIC)
                || topic.equals(Constants.LAST_WILL_TOPIC)) {
            Log.w("MQTT", "New message arrived: " + m + " @ " + topic);
        }
        String message = m.toString();
        if (topic.equals(Constants.CONNECTED_TOPIC + this.id + Constants.CONNECTED_ACKNOWLEDGE_TOPIC)) {
            connectionAcknowledged = true;
            Log.d("MQTT", "Connection Ackd");
        } else if (topic.equals(Constants.CLIENT_TOPIC + Constants.TOPIC_WARNING)) {
            if (message.equals(Constants.MESSAGE_WARNING)) {
                callback.soundWarning();
            }
        } else if (topic.equals(Constants.CLIENT_TOPIC + Constants.TOPIC_DANGER)) {
            if (message.equals(Constants.MESSAGE_DANGER)) {
                callback.soundDanger();
            }
        } else if (topic.equals(Constants.CLIENT_TOPIC + Constants.TOPIC_STOP_WARNING)) {
            if (message.equals(Constants.CLIENT_TOPIC + Constants.MESSAGE_STOP_WARNING)) {
                callback.stopSounds();
            }
        } else if (topic.equals(Constants.LAST_WILL_TOPIC)) {
            if (message.equals(Constants.MAIN_CLIENT_DISCONNECTING)) {
                callback.notifyCaller(false);
            }
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.e("MQTT", "Mqtt Client Disconnecting");
        cause.printStackTrace();
        callback.notifyCaller(false);
    }

    public void disconnect() {
        Log.e("ERROR", "Disconnecting client (why?)");
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        //connectionLost(new Throwable());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
