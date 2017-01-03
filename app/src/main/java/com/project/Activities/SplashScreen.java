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
 * Simple splash screen
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
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String title = "Mode choice";
                String message = "Please choose a mode into which the application will start";
                String positiveText = "Online Mode";
                String negativeText = "Offline Mode";
                DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onlineMode) {
                            // If the user can AND wants to go to online mode,
                            // go to online mode. Else, go to offline mode
                            startActivity(new Intent(SplashScreen.this, OnlineMode.class));
                        } else {
                            startActivity(new Intent(SplashScreen.this, OfflineMode.class)
                                    .putExtra(Constants.PERSIST_INTO_MODE, ""));
                        }
                    }
                };
                DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SplashScreen.this, OfflineMode.class)
                                .putExtra(Constants.PERSIST_INTO_MODE, ""));
                    }
                };
                try {
                    checker.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                onlineMode = checker.isGpsAvailable() && checker.isInternetAvailable();

                AlertBuilder alert = new AlertBuilder(SplashScreen.this, message, title, positive, positiveText,
                                        negative, negativeText);
                alert.showDialog();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
