package com.project.HelpClasses;

/*
 * Constants required for shared preferences (mostly)
 */
public class Constants {
    public static final String PREFS = "my_preferences";
    public static final String LIGHT = "light";
    public static final String PROX = "proximity";
    public static final String FREQ= "frequency";
    public static final String PREFERRED_MODE = "pref mode";
    public static final int DOWN_TIME = 10;
    public static final int UP_TIME = 5;
    public static final String MQTT_CONNECTION_URL = "conn url";
    public static final String MQTT_PORT = "port";
    public static final String CLIENT_ID = "client id";
    public static final int INTERVAL = 1;

    public static final int MODE_ONLINE = 0;
    public static final int MODE_OFFLINE = 1;

    public static final int SECONDS = 1000;

    public static final int PERMISSION_ACCESS_LOCATION_RESULT = 256;
    public static final int PERMISSION_ACCESS_NETWORK_STATE_RESULT = 257;

    public static final String FROM_MODE = "from mode";

    /*
     * Mqtt Topic Constants
     */
    public static final String CONNECTION_TOPIC = "connections";
    public static final String CONNECTED_TOPIC = CONNECTION_TOPIC + "/connected/";
    public static final String NEW_CONNECTION_TOPIC = CONNECTION_TOPIC + "/newConnections";
    public static final String CONNECTED_TOPIC_SIMPLE = "connected";
    public static final String CONNECTED_ACKNOWLEDGE_TOPIC = "/acknowledged";
    public static final String ACKNOWLEDGE = "acknowledged id";
    public static final String TOPIC_WARNING = "/warning";
    public static final String TOPIC_DANGER = "/danger";
    public static final String TOPIC_STOP_WARNING = "/stopSounds";
    public static String CLIENT_TOPIC;

    /*
     * Mqtt Message Constants
     */
    public static final String MESSAGE_WARNING = "warning";
    public static final String MESSAGE_DANGER = "danger";
    public static final String MESSAGE_NO_WARNING = "no warning";
    public static final String MESSAGE_STOP_WARNING = "stopSounds warning";


    public static final String PERSIST_INTO_MODE = "persist";
}
