package com.project.GPS;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.project.HelpClasses.Constants;

public class OnlineAvailabilityChecker extends Thread {
    private Context context;
    private Activity activity;
    private boolean internetAvailable = false;
    private boolean gpsAvailable = false;
    private boolean hasPermission = false;

    public OnlineAvailabilityChecker(Context context) {
        this.context = context;
        this.activity = (Activity) context;
    }

    @Override
    public void run() {
        this.hasPermission = checkForPermission();
        if (hasPermission) {
            this.internetAvailable = checkForNetwork();
            this.gpsAvailable = checkForGPS();
        }
    }

    public boolean isInternetAvailable() { return this.internetAvailable; }

    public boolean isGpsAvailable() { return this.gpsAvailable; }

    public boolean hasPermission() { return this.hasPermission; }

    private boolean checkForPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(activity,
                new String[] {Manifest.permission.ACCESS_NETWORK_STATE},
                Constants.PERMISSION_ACCESS_NETWORK_STATE_RESULT);
    }

    private boolean checkForNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo active = connectivityManager.getActiveNetworkInfo();
        return active != null;
    }


    private boolean checkForGPS() {
        final LocationManager manager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
