package com.project.dilocossuperproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    SensorManager mSensorManager;
    int times ; /*Used to calculate light average */
    float proxFloor; //Low threshold for proximity
    float number, floorAvg;
    float av; /*light average*/
    boolean over, under;  //over - under light threshold
    Runnable rUp, rDown, proxAlert;
    private ScheduledFuture  cancelProx;
    private final ScheduledExecutorService schedulerUp = Executors.newScheduledThreadPool(1);  //Executors required for
    private final ScheduledExecutorService schedulerDown = Executors.newScheduledThreadPool(1);  //Recurring tasks
    private final ScheduledExecutorService schedulerProx = Executors.newScheduledThreadPool(1);
    SharedPreferences prefs;
    MediaPlayer myLightPlayer;
    MediaPlayer myProxPlayer;
    Toast lightToast;  //Keep a toast instance (needed to hide the message)
    Toast proxToast;
    boolean lightToastIsShowing;
    boolean proxToastIsShowing;
    TextView txtProx;
    TextView txtLight;
    ImageView bulb;
    ImageView proxImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initialise();

    }

    private void initialise() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
         * Initialise the activity's Toolbar and navigation drawer
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        myLightPlayer = MediaPlayer.create(this,R.raw.warning);
        myProxPlayer = MediaPlayer.create(this,R.raw.warning);

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
         * Get the necessary sensor manager
         */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        /*
         * Create a shared preferences instance (needed to keep track of user's settings)
         */
        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);

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

        /*
         * This runnable will keep the proximity alert ringing while the proximity sensor
         * is less than the threshold
         */

        proxAlert= new Runnable() {
            @Override
            public void run() {
                proxToast.show();
                proxToastIsShowing = true;
                if (!myLightPlayer.isPlaying()&& !myProxPlayer.isPlaying()) {
                    myProxPlayer.start();
                }
                if (proxImg.getVisibility() == View.INVISIBLE) {
                    proxImg.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    public void onResume(){
        super.onResume();

         /*
         * Register a light and proximity listener
         */
        int delay= prefs.getInt(Constants.FREQ,SensorManager.SENSOR_DELAY_NORMAL);

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), delay);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), delay);

        /*
         * Get a temporary proximity sensor to get hold of proximity's maximum range
         */

        /*
         * Get the user's light and proximity protection settings
         */
        floorAvg = ((100f-prefs.getInt(Constants.LIGHT,50))/100f);
        proxFloor = (prefs.getFloat(Constants.PROX, 0.25f));
    }


    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {}


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float lux;
        float cm;
        float floor;


        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            lux = event.values[0];
            Resources res = getResources();
            String lightString = String.format(res.getString(R.string.main_light_text_view), lux);
            txtLight.setText(lightString);
            if (times == 10) {       /*Calculate light average*/
                av = number/times;
            } else if (times < 10){
                number += lux;
                times++;
            }
            if (av != 0) {
                floor = av* floorAvg;
                if (lux >= av + floor) {     /*Light is increased. Calculate the new average*/
                    over = true;
                    under = false;
                    schedulerUp.schedule(rUp, Constants.UP_TIME, TimeUnit.SECONDS);
                    bulb.setImageResource(R.drawable.light_bulb_brighter);
                } else if (lux <= av - floor) {   /*Light decreased*/
                    over = false;
                    under = true;
                    lightToast.show();              /*Warning*/
                    lightToastIsShowing = true;
                    bulb.setImageResource(R.drawable.light_bulb_darker);
                    if (!myProxPlayer.isPlaying()&&!myLightPlayer.isPlaying()) {
                        myLightPlayer.start();
                    }
                    /*if light stay low for a period of time we calculate average again*/
                    schedulerDown.schedule(rDown, Constants.DOWN_TIME, TimeUnit.SECONDS);
                } else {
                    bulb.setImageResource(R.drawable.light_bulb_normal);
                    over = false;
                    under = false;
                    lightToast.cancel();
                    lightToastIsShowing = false;
                }
            }
        } else if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
            cm = event.values[0];
            if (cm <= proxFloor) {       /*Warning*/
                cancelProx = schedulerProx.scheduleAtFixedRate(proxAlert, 0, 2, TimeUnit.SECONDS);
            } else {
                if (cancelProx != null) {
                    cancelProx.cancel(true);
                    proxToast.cancel();
                }
                proxToastIsShowing = false;
                if (proxImg.getVisibility() == View.VISIBLE) {
                    proxImg.setVisibility(View.INVISIBLE);
                }
            }
            Resources res = getResources();
            String proxString = String.format(res.getString(R.string.main_proximity_text_view), (int) cm);
            txtProx.setText(proxString);
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            String mes = "Θέλετε να τερματίσετε την εφαρμογή;";     /*Asking the user*/
            String title = "Έξοδος";
            DialogInterface.OnClickListener positive= new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   exit();
                }
            };
            AlertBuilder alert= new AlertBuilder(this, mes, title,positive,null);
            alert.showDialog();
        }

    }

    public void exit() {
        if (myLightPlayer.isPlaying()) {
            myLightPlayer.stop();               /*Stop the warnings*/
        }
        if (myProxPlayer.isPlaying()) {
            myProxPlayer.stop();
        }
        if (proxToastIsShowing) {
            proxToast.cancel();
        }
        if (lightToastIsShowing) {
            lightToast.cancel();
        }
        mSensorManager.unregisterListener(this); /*Stop the Listener*/
        this.finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            /*
             * Start the settings activity when user presses the toolbar settings button
             */
            startActivity (new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_exit) {
            /*
             * In case the user presses the sidebar exit button
             * close the application
             */
            exit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
