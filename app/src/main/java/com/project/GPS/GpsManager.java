package com.project.GPS;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.project.HelpClasses.Constants;

/**
 * Simple manager that manages the google services location api callbacks
 */
public class GpsManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private String latitude;
    private String longitude;
    private int interval;
    private Context mContext;

    public GpsManager(int interval, Activity activity) {
        this.interval = interval;
        this.mContext = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();

    }

    public void start() {
        mGoogleApiClient.connect();
    }


    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval*Constants.SECONDS)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private boolean startLocationUpdates(){
        int permissionCheck = ContextCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            return true;
        } else {
            return false;
        }
    }

    public void stopLocationUpdates(){
        if(mGoogleApiClient != null){
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this.mContext, "A connection with the Google Location Services " +
                "client could not be established. Please try again later.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        System.out.println("lat: " + location.getLatitude());
    }

    public int getInterval() {
        return interval;
    }

    public String[] getLocation() {

        return new String[]{latitude,longitude};
    }

    public boolean isConnected() {
        return mGoogleApiClient.isConnected();
    }

    /*
     * (For both getLatitude() and getLongitude())
     * If, for any reason, the user's current location us unavailable,
     * we request their last known location and send that to the server
     */
    public String getLatitude() {
        if (this.latitude == null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (lastLocation == null) {
                    return null;
                }
                this.latitude = String.valueOf(lastLocation.getLatitude());
            }
        }
        return this.latitude;
    }

    public String getLongitude() {
        if (this.longitude == null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (lastLocation == null) {
                    return null;
                }
                this.longitude = String.valueOf(lastLocation.getLongitude());
            }
        }
        return this.longitude;
    }
}
