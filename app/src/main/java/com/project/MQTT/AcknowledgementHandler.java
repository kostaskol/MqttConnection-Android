package com.project.MQTT;


import android.os.Handler;
import android.os.Looper;

class AcknowledgementHandler extends Thread {
    volatile private boolean acknowledged;
    private MqttConnectionCallback callback;

    AcknowledgementHandler() {}

    AcknowledgementHandler(String threadName, MqttConnectionCallback callback) {
        super(threadName);
        this.callback = callback;
        this.acknowledged = false;
    }

    @Override
    public void run() {
        Looper.prepare();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.notifyCaller(acknowledged);
            }
        }, 4 * 1000);
    }

    void setAcknowledged() { this.acknowledged = true; }
}
