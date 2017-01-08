package com.project.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.GPS.GpsManager;
import com.project.HelpClasses.AlertBuilder;
import com.project.HelpClasses.Constants;
import com.project.HelpClasses.MyMediaPlayer;
import com.project.MQTT.MqttConnectionCallback;
import com.project.MQTT.MqttManager;
import com.project.sensors.MySensorManager;
import com.project.sensors.SensorCallback;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.security.AccessController.getContext;

/**
 * Activity that warns the user about imminent danger by
 * sending the device's sensor's data to a desktop application
 * which notifies the client whether it should ring out a
 * warning signal, a danger signal or to stop any signals in progress
 */
public class OnlineMode extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MqttConnectionCallback, SensorCallback {

    private TextView statusText;
    private MqttManager mqttManager;
    private GpsManager gpsManager;
    private String clientId;

    MySensorManager mySensorManager;

    private MyMediaPlayer warningPlayer;
    private MyMediaPlayer dangerPlayer;
    private Toast warningToast;
    private Toast dangerToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_mode);

        initialise();

    }


    @Override
    public void onStart() {
        super.onStart();
        if (clientId != null) {
            mqttManager = new MqttManager(clientId, this);
        } else {
            Log.e("NO ID SAVED", "There is no saved ID on this device. Why didn't splash screen catch that?");
        }

        /*
         * If we cannot connect to the MQTT broker, the application
         * goes into offline mode and notifies the user about the problem
         */
        if (!mqttManager.connect()) {
            goOffline(Constants.REASON_CLIENT_NOT_CONNECTED);
        }
    }

    private void initialise() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

        if (prefs.getInt(Constants.PREFERRED_MODE, Constants.MODE_ONLINE) != Constants.MODE_ONLINE) {
            goOffline("");
        }

        statusText = (TextView) findViewById(R.id.online_status_text_view);

        gpsManager = new GpsManager(Constants.INTERVAL, this);
        gpsManager.start();
        clientId = prefs.getString(Constants.CLIENT_ID, null);

        warningPlayer = new MyMediaPlayer(this, R.raw.online_sound_warning, true);
        dangerPlayer = new MyMediaPlayer(this, R.raw.online_sound_danger, true);

        Animation anim = new AlphaAnimation(1.0f, 0.3f);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(600);
        statusText.startAnimation(anim);
    }

    @Override
    public void onSensorValuesChanged(String lightVal, String proxVal) {
        String latitude = gpsManager.getLatitude();
        String longitude = gpsManager.getLongitude();
        mqttManager.publish(Constants.CLIENT_TOPIC, clientFormat(clientId, latitude,
                longitude, lightVal, proxVal));
    }

    private String clientFormat(String id, String lat, String lng, String light, String prox) {
        return id + "/" + lat + "/" + lng + "/" + light + "/" + prox;
    }

    /*
     * The mqttManager will call this function after 3 seconds, passing
     * the desktop application's response (if any)
     */
    @Override
    public void notifyCaller(final boolean ack, final Integer interval) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!ack) {
                    // The main client seems to not be connected. Fall back to offline mode
                    Log.e("MQTT", "Main Client did not acknowledge us. Disconnecting");
                    mqttManager.disconnect();
                    goOffline(Constants.REASON_CLIENT_NOT_CONNECTED);
                } else {
                    Log.d("MQTT", "Finalising connection");
                    mySensorManager = new MySensorManager(OnlineMode.this, interval);
                    mySensorManager.start();
                }
            }
        });
    }

    @Override
    public void soundWarning() {
        if (warningPlayer != null) {
            warningPlayer.start(true);
        }
        warningToast = Toast.makeText(this, "Warning! Warning! Warning!", Toast.LENGTH_SHORT);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Warning!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void soundDanger() {
        if (dangerPlayer != null) {
            if (warningPlayer != null) {
                warningPlayer.stopImmediately();
            }
            dangerPlayer.start(true);
        }
        dangerToast = Toast.makeText(this, "Danger! Danger! Danger!", Toast.LENGTH_SHORT);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dangerToast.show();
            }
        });
    }

    @Override
    public void stopSounds() {
        if (statusText != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusText.clearAnimation();
                }
            });
        }
        if (warningPlayer != null) {
            warningPlayer.stop();
        }
        if (dangerPlayer != null) {
            dangerPlayer.stop();
        }
        if (warningToast != null) {
            warningToast.cancel();
            warningToast = null;
        }
        if (dangerToast != null) {
            dangerToast.cancel();
            dangerToast = null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                String mes = "Are you sure you would like to exit the application?";
                String title = "Application Exit";
                DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit();
                    }
                };

                DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                };
                AlertBuilder alert= new AlertBuilder(this, mes, title, positive, negative);
                alert.showDialog();
            }
        }
    }

    public void stop() {
        stopSounds();
        if (gpsManager != null) {
            if (gpsManager.isConnected()) {
                gpsManager.stopLocationUpdates();
            }
        }
        if (mySensorManager != null) {
            mySensorManager.unregisterListeners();
            mySensorManager.interrupt();
            mySensorManager = null;
        }
        if (warningPlayer != null) {
            warningPlayer.destroy();
        }
        if (dangerPlayer != null) {
            dangerPlayer.destroy();
        }
        if (mySensorManager != null) {
            mySensorManager.unregisterListeners();
        }
    }

    public void exit() {
        stop();
        finishAffinity();
    }

    private void goOffline(String reason) {
        Log.d("DEBUG", "Starting offline mode");
        stop();
        finish();
        startActivity(new Intent(OnlineMode.this, OfflineMode.class).putExtra(Constants.PERSIST_INTO_MODE, reason));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.online_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_exit:
                exit();
                break;
            case R.id.nav_settings:
                stop();
                finish();
                startActivity(new Intent(OnlineMode.this, SettingsActivity.class)
                        .putExtra(Constants.FROM_MODE, Constants.MODE_ONLINE));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
