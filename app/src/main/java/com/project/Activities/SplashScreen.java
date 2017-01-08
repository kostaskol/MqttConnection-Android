package com.project.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.project.GPS.OnlineAvailabilityChecker;
import com.project.HelpClasses.AlertBuilder;
import com.project.HelpClasses.Constants;

import java.util.UUID;

/*
 * Splash Screen Activity. Makes the necessary checks (GPS/Internet availability, user's preferred mode)
 * and starts the appropriate activity
 */

public class SplashScreen extends AppCompatActivity {

    static private boolean onlineMode = false;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        final int preferredMode = prefs.getInt(Constants.PREFERRED_MODE, Constants.MODE_OFFLINE);
        String id = prefs.getString(Constants.CLIENT_ID, null);
        if (id == null) {
            id = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Constants.CLIENT_ID, id);
            editor.apply();
        }
        Animation alpha = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.startAnimation(alpha);
        final OnlineAvailabilityChecker checker = new OnlineAvailabilityChecker(this);
        checker.start();
        final boolean firstTime = prefs.getBoolean(Constants.IS_FIRST_TIME, true);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            /*
             * While the animation is playing, we check if the phone has internet/GPS access.
             * If it doesn't, we go to offline mode (no matter the user's preferences)
             * If it does, we go to the user's preferred mode
             * If it is the first time the user has opened the application, we ask them about
             * their preferred mode of operation
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                if (!checker.hasLocationPermission()) {
                    checker.requestLocationPermission();
                }
                if (firstTime) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(Constants.IS_FIRST_TIME, false);
                    editor.apply();
                    String title = "Mode choice";
                    String message = "Since this is your first time " +
                            "using this application, please choose a mode into which the application will start";
                    String positiveText = "Online Mode";
                    String negativeText = "Offline Mode";
                    DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!checker.isInternetAvailable()) {
                                startActivity(new Intent(SplashScreen.this, OfflineMode.class)
                                        .putExtra(Constants.PERSIST_INTO_MODE, Constants.REASON_NO_INTERNET));
                            } else if (!checker.isGpsAvailable()) {
                                startActivity(new Intent(SplashScreen.this, OfflineMode.class)
                                        .putExtra(Constants.PERSIST_INTO_MODE, Constants.REASON_NO_GPS));
                            }
                            try {
                                checker.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // If the user wants to go to online mode,
                            // go to online mode. Else, go to offline mode
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.putInt(Constants.PREFERRED_MODE, Constants.MODE_ONLINE);
                            edit.apply();
                            startActivity(new Intent(SplashScreen.this, OnlineMode.class));
                        }
                    };
                    DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.putInt(Constants.PREFERRED_MODE, Constants.MODE_OFFLINE);
                            edit.apply();
                            startActivity(new Intent(SplashScreen.this, OfflineMode.class)
                                    .putExtra(Constants.PERSIST_INTO_MODE, ""));
                        }
                    };


                    AlertBuilder alert = new AlertBuilder(SplashScreen.this, message, title, positive, positiveText,
                            negative, negativeText);
                    alert.showDialog();
                } else {
                    if (preferredMode == Constants.MODE_ONLINE) {
                        startActivity(new Intent(SplashScreen.this, OnlineMode.class));
                    } else {
                        startActivity(new Intent(SplashScreen.this, OfflineMode.class));
                    }
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
