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

/**
 * Thread that checks if GPS and an internet connection are available
 * Also offers useful functions for checking available permissions and
 * requesting them if not
 */
public class OnlineAvailabilityChecker extends Thread {
    private Context context;
    private Activity activity;
    private boolean internetAvailable = false;
    private boolean gpsAvailable = false;
    private boolean hasNetworkStatePermission = false;
    private boolean hasLocationPermission = false;

    public OnlineAvailabilityChecker(Context context) {
        this.context = context;
        this.activity = (Activity) context;
    }

    @Override
    public void run() {
        this.hasNetworkStatePermission = checkForNetworkStatePermission();
        this.hasLocationPermission = checkForFineLocationPermission();
        if (this.hasNetworkStatePermission && this.hasLocationPermission) {
            this.internetAvailable = checkForNetwork();
            this.gpsAvailable = checkForGPS();
        }
    }

    public boolean isInternetAvailable() { return this.internetAvailable; }

    public boolean isGpsAvailable() { return this.gpsAvailable; }

    public boolean hasNetworkStatePermission() { return this.hasNetworkStatePermission; }

    public boolean hasLocationPermission() { return this.hasLocationPermission; }

    public boolean hasPermissions() { return this.hasNetworkStatePermission && this.hasLocationPermission; }

    private boolean checkForNetworkStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkForFineLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    public void requestNetworkStatePermission() {
        ActivityCompat.requestPermissions(activity,
                new String[] {Manifest.permission.ACCESS_NETWORK_STATE},
                Constants.PERMISSION_ACCESS_NETWORK_STATE_RESULT);
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(activity,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                Constants.PERMISSION_ACCESS_FINE_LOCATION_RESULT);
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
