package com.project.MQTT;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
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
    private SharedPreferences prefs;
    private String connUrl;
    private String port;
    private boolean connectionAcknowledged = false;

    private Context context;

    private String id;

    private MqttConnectionCallback callback;


    public MqttManager(String id, Context context) {
        this.id = id;
        this.context = context;
        this.callback = (MqttConnectionCallback) context;
        Constants.CLIENT_TOPIC = Constants.CONNECTED_TOPIC + this.id;
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        connUrl = prefs.getString(Constants.MQTT_CONNECTION_URL, "tcp://192.168.1.3");
        port = prefs.getString(Constants.MQTT_PORT, "1883");
        try {
            //Log.d("DEBUG", "Connecting to client: url: " + connUrl + " @ port: " + port);
            this.client = new MqttClient(connUrl + ":" + port, id, new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean connect() {
        final Runnable ackRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("MQTT", "Notifying caller with: " + connectionAcknowledged);
                callback.notifyCaller(connectionAcknowledged);
            }
        };

        final Handler handler = new Handler();

        try {
            this.client.setCallback(this);
            this.client.connect();

            // If the Main Client is connected, it will acknowledge our
            // publication. If they have not within the next 3 seconds,
            // we assume that it is not running
            subscribe(Constants.CLIENT_TOPIC + Constants.TOPIC_WARNING);
            subscribe(Constants.CLIENT_TOPIC + Constants.TOPIC_DANGER);
            subscribe(Constants.CLIENT_TOPIC + Constants.TOPIC_STOP_WARNING);
            subscribe(Constants.CLIENT_TOPIC + Constants.CONNECTED_ACKNOWLEDGE_TOPIC);
            publish(Constants.NEW_CONNECTION_TOPIC, this.id);
            handler.postDelayed(ackRunnable, 10 * 1000);
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void subscribe(String topic) {
        try {
            this.client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String message) {
        Log.d("PUBLISH", "Publishing message: " + message + " to topic: " + topic);
        MqttMessage m = new MqttMessage(message.getBytes());
        try {
            this.client.publish(topic, m);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage m) throws Exception {
        String message = m.toString();
        if (topic.equals(Constants.CONNECTED_TOPIC + this.id + Constants.CONNECTED_ACKNOWLEDGE_TOPIC)) {
            if (message.equals(Constants.ACKNOWLEDGE)) {
                Log.d("MQTT", "Connection Acknowledged");
                connectionAcknowledged = true;
            }
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
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.e("MQTT", "Mqtt Client Disconnecting");
        cause.printStackTrace();
        if (!this.connect()) {
            String title = "Connection Lost";
            String message = "Lost connection to the Mqtt Broker (" + connUrl + ":" + port + ") " +
                    "\nThis application will now switch to offline mode. Please try to connect again later";
            AlertBuilder alert = new AlertBuilder(context, title, message);
            alert.showDialog();
        }
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
