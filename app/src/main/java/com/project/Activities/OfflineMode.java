package com.project.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.GPS.OnlineAvailabilityChecker;
import com.project.HelpClasses.AlertBuilder;
import com.project.HelpClasses.Constants;
import com.project.MQTT.MqttConnectionCallback;
import com.project.MQTT.MqttManager;
import com.project.sensors.MySensorManager;
import com.project.sensors.SensorCallback;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class OfflineMode extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MqttConnectionCallback,
        SensorCallback {

    private MySensorManager mySensorManager;
    private int times ; /*Used to calculate light average */
    private float proxFloor; //Low threshold for proximity
    private float number, floorAvg;
    private float av; /*light average*/
    private boolean over, under;  //over - under light threshold
    private Runnable rUp, rDown, proxAlert;
    private final ScheduledExecutorService schedulerUp = Executors.newScheduledThreadPool(1);  //Executors required for
    private final ScheduledExecutorService schedulerDown = Executors.newScheduledThreadPool(1);  //Recurring tasks
    private SharedPreferences prefs;
    private MediaPlayer myLightPlayer;
    private MediaPlayer myProxPlayer;
    private Toast lightToast;  //Keep a toast instance (needed to hide the message)
    private Toast proxToast;
    private boolean lightToastIsShowing;
    private boolean proxToastIsShowing;
    private TextView txtProx;
    private TextView txtLight;
    private ImageView bulb;
    private ImageView proxImg;
    private Context offlineContext;
    private MqttManager mqttManager;
    private boolean stopRecon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_mode);

         /*
         * Create a shared preferences instance (needed to keep track of user's settings)
         */
        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

        Intent tmp = getIntent();
        if (!tmp.hasExtra(Constants.PERSIST_INTO_MODE)) {
            Log.d ("DEBUG", "INTENT DOES NOT HAVE EXTRA");
            if (prefs.getInt(Constants.PREFERRED_MODE, Constants.MODE_OFFLINE) != Constants.MODE_OFFLINE) {
            /*
             * The activity has resumed from the SettingsActivity and the user has switched to online mode
             *
             */
                OnlineAvailabilityChecker checker = new OnlineAvailabilityChecker(this);
                checker.start();
                try {
                    checker.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!checker.hasPermission()) {
                    checker.requestPermission();
                } else {
                    if (!checker.isInternetAvailable() || !checker.isGpsAvailable()) {
                        String title = "Offline Mode";
                        String message = "You have been brought to offline mode because " +
                                "your device either has no internet connection or its location is unavailable";
                        AlertBuilder alert = new AlertBuilder(this, message, title);
                        alert.showDialog();
                    } else {
                        goOnline();
                    }
                }
            }
        } else {
            String reason = tmp.getStringExtra(Constants.PERSIST_INTO_MODE);
            if (reason.equals(Constants.REASON_CLIENT_NOT_CONNECTED)) {
                String title = "Offline Mode";
                String message = "You have been brought to Offline Mode because " + reason + "" +
                        "Would you like the application to automatically try to reconnect?";
                DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Handler handler = new Handler();
                        Runnable recon = new Runnable() {
                            @Override
                            public void run() {
                                if (!stopRecon) {
                                    Log.w("RECON", "Reconnecting");
                                    String id = prefs.getString(Constants.CLIENT_ID, null);
                                    if (id == null) {
                                        Log.e("NO ID", "Why didn't splash screen catch this?");
                                    }
                                    mqttManager = new MqttManager(id, OfflineMode.this);
                                    Log.d("MQTT", "Trying to reconnect");
                                    mqttManager.connect();
                                    handler.postDelayed(this, 3 * 1000);
                                }
                            }
                        };
                        recon.run();
                    }
                };

                DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                };
                AlertBuilder alert = new AlertBuilder(this, message, title, positive, negative);
                alert.showDialog();
            }
        }

        initialise();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
         * Get the user's light and proximity protection settings
         */
        floorAvg = ((100f-prefs.getInt(Constants.LIGHT,50))/100f);
        proxFloor = (prefs.getFloat(Constants.PROX, 0.25f));

        mySensorManager = new MySensorManager(this);
        mySensorManager.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        onResume();
    }

    private void initialise() {


        /*
         * Initialise the activity's Toolbar and navigation drawer
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        offlineContext = this;

        /*
         * Initialise UI
         */
        txtProx = (TextView) findViewById(R.id.textProximity);
        txtLight = (TextView) findViewById(R.id.textLight);
        bulb = (ImageView) findViewById(R.id.bulb);
        proxImg = (ImageView) findViewById(R.id.prox_img);
        /*
         * Initialise both light's and proximity's media players
         */
        myLightPlayer = MediaPlayer.create(this,R.raw.offline_sound_warning);
        myProxPlayer = MediaPlayer.create(this,R.raw.offline_sound_warning);

        /*
         * Initialise both light's and proximity's toasts (We first initialise them so we can later
         * show and cancel them at will
         */
        lightToast = Toast.makeText(this, "Warning! Warning! Warning! Warning!", Toast.LENGTH_SHORT);
        lightToastIsShowing = false;
        proxToast = Toast.makeText(this, "Warning! Warning! Warning! Warning!", Toast.LENGTH_SHORT);
        proxToastIsShowing = false;

        /*
         * Make various initialisations, mainly necessary for
         * keeping track of the room's average lighting
         */
        times = 0; number = av = 0f;
        over = under = false;


        /*
         * The next two runnables reset all average required variables so that a new average is calculated
         */

        rUp = new Runnable() {
            @Override
            public void run() {
                if (over) {
                    times = 0;
                    av = 0;
                    number = 0;
                    over = false;
                }
            }
        };

        rDown = new Runnable() {
            @Override
            public void run() {
                if (under) {
                    times = 0;
                    av = 0;
                    number = 0;
                    under = false;
                }
            }
        };

    }

    @Override
    public void onSensorValuesChanged(String lightVal, String proxVal) {
        float lux;
        float cm;
        float floor;
        if (lightVal == null || proxVal == null) {
            return;
        }
        lux = Float.valueOf(lightVal);
        Resources res = offlineContext.getResources();
        final String lightString = String.format(res.getString(R.string.main_light_text_view), lux);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtLight.setText(lightString);
            }
        });
        if (times == 10) {       /*Calculate light average*/
            av = number / times;
        } else if (times < 10) {
            number += lux;
            times++;
            return;
        }
        if (av != 0) {
            floor = av * floorAvg;
            if (lux >= av + floor) {     /*Light is increased. Calculate the new average*/
                over = true;
                under = false;
                schedulerUp.schedule(rUp, Constants.UP_TIME, TimeUnit.SECONDS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bulb.setImageResource(R.drawable.light_bulb_brighter);
                    }
                });
            } else if (lux <= av - floor) {   /*Light decreased*/
                over = false;
                under = true;
                lightToast.show();              /*Warning*/
                lightToastIsShowing = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bulb.setImageResource(R.drawable.light_bulb_darker);
                    }
                });
                if (!myProxPlayer.isPlaying() && !myLightPlayer.isPlaying()) {
                    myLightPlayer.start();
                }
            /*if light stay low for a period of time we calculate average again*/
                schedulerDown.schedule(rDown, Constants.DOWN_TIME, TimeUnit.SECONDS);
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bulb.setImageResource(R.drawable.light_bulb_normal);
                    }
                });
                over = false;
                under = false;
                lightToast.cancel();
                lightToastIsShowing = false;
            }
        }

        cm = Float.valueOf(proxVal);
        if (cm <= proxFloor) {       /*Warning*/
            if (!lightToastIsShowing && !proxToastIsShowing) {
                proxToast.show();
                proxToastIsShowing = true;
            }

            if (!myLightPlayer.isPlaying() && !myProxPlayer.isPlaying()) {
                myProxPlayer.start();
            }
        } else {
            if (proxToastIsShowing) {
                proxToast.cancel();
            }

            if (myProxPlayer.isPlaying()) {
                myProxPlayer.stop();
            }
            proxToastIsShowing = false;
        }

        final String proxString = String.format(res.getString(R.string.main_proximity_text_view), (int) cm);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtProx.setText(proxString);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_offline_mode_drawer, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_exit:
                exit();
                break;
            case R.id.nav_settings:
                stop();
                finish();
                startActivity(new Intent(OfflineMode.this, SettingsActivity.class)
                        .putExtra(Constants.FROM_MODE, Constants.MODE_OFFLINE));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            String mes = "Are you sure you would like to exit the application?";     /*Asking the user*/
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

    @Override
    public void notifyCaller(boolean ack) {
        Log.d("MQTT", "Caller notified: " + ack);
        if (ack) {
            // This means that both the broker and the client
            // are online. If the user has chosen online mode as
            // the preferred mode of operation, we start the
            // corresponding activity
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    goOnline();
                }
            });

        }
    }

    @Override
    public void soundWarning() {}

    @Override
    public void soundDanger() {}

    @Override
    public void stopSounds() {}

    public void stop() {
        /*
         * Stop the warnings
         */
        if (myLightPlayer != null) {
            if (myLightPlayer.isPlaying()) {
                myLightPlayer.stop();
            }
        }
        if (myProxPlayer != null) {
            if (myProxPlayer.isPlaying()) {
                myProxPlayer.stop();
            }
        }
        if (proxToastIsShowing) {
            if (proxToast != null) {
                proxToast.cancel();
            }
        }
        if (lightToastIsShowing) {
            if (lightToast != null) {
                lightToast.cancel();
            }
        }
        if (mySensorManager != null) {
            Log.w("SENSOR", "Stopping sensor manager");
            mySensorManager.unregisterListeners();
            mySensorManager.interrupt();
            mySensorManager = null;
        }

        this.stopRecon = true;
        Log.w("OFFLINE MODE", "OFFLINE MODE STOPPING!!!!!!!!!!!!!!");
    }

    public void exit() {
        stop();
        this.finishAffinity();
    }

    private void goOnline() {
        stop();
        finish();

        startActivity(new Intent(OfflineMode.this, OnlineMode.class));
    }
}
