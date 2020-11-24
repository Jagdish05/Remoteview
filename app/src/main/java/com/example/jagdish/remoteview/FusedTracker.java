package com.example.jagdish.remoteview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaal on 20-01-2017.
 */

public class FusedTracker implements LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    // flag for GPS status
    public boolean isGPSEnabled = false;
    private boolean isServicesStart = false;
    boolean pathCapture = false;

    private static final long Interval = 1000 * 4;
    private static final long Fastes_Interval = 1000 * 1;
    private static final float Min_Distance = 6;

    private GoogleMap mMap;

    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Context mContext;

    private Location mCurLoction;
    private Location preLoc; // location
    private LocationManager locationManager;

    private double latitude, longitude; // latitude // longitude

    private List latlngarray;
    private List pathDetail;
    private LatLng latLng;

    //----------------------------------------------------------------------------Contructor section
    public FusedTracker(Context context) {
        mContext = context;

        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if(resp == ConnectionResult.SUCCESS){
            Log.e("Google Play Services","Success");
        }
        else{
            Log.e("Google Play Services","Failed");
            return;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Interval)
                .setFastestInterval(Fastes_Interval)
                .setSmallestDisplacement(Min_Distance)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        latlngarray = new ArrayList();
        pathDetail = new ArrayList();
        startLocationUpdates();
    }
    //----------------------------------------------------------------------Contructor section ended

    //-----------------------------------------------------------------------Location method section

    /*Call when location is changed
    Run in background continueous*/
    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            Log.e("Location","Location Changed");
            this.mCurLoction = location;
            latitude = getLatitude();
            longitude = getLongitude();

            if(pathCapture && getServicesStart() && mCurLoction != null) {
                if(preLoc == null) {
                    Log.e("Location", "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\ndistance: 0");
                    pathDetail.add(new String("Location added - \nLat: " + latitude + "\nLong: " + longitude + "\ndistance: 0"));
                    latLng = new LatLng(latitude, longitude);
                    latlngarray.add(latLng);
                    if(mMap != null && latLng != null) {
                        mMap.addPolyline(new PolylineOptions()
                                .add(latLng)
                                .width(5)
                                .color(Color.BLUE));
                    } /*mMap != null && latLng != null*/
                    preLoc = location;
                } else {
                    Log.e("Location", "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\ndistance: " + preLoc.distanceTo(location));
                    pathDetail.add(new String("Location added - \nLat: " + latitude + "\nLong: " + longitude + "\ndistance: " + preLoc.distanceTo(location)));
                    latLng = new LatLng(latitude, longitude);
                    latlngarray.add(latLng);
                    if(mMap != null && latLng != null) {
                        mMap.addPolyline(new PolylineOptions()
                                .add(latLng)
                                .width(5)
                                .color(Color.BLUE));
                    } /*mMap != null && latLng != null*/
                    preLoc = location;
                } /*preLoc == null*/
            } //pathCapture && getServicesStart() && mCurLoction != null
        }
    }

    private void requestLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
        );
    }

    /**
     * start location updates
     */
    public void startLocationUpdates() {
        // connect and force the updates
        // getting GPS status
        locationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.e("isGPSEnabledfused", "=" + isGPSEnabled);

        if (isGPSEnabled == false) {
            showSettingsAlert();
        }

        if(!isGPSEnabled) {
            return;
        }

        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected()) {
            requestLocationUpdates();
        }
        isServicesStart = true;
    }

    /**
     * removes location updates from the FusedLocationApi
     * stop location update
     */
    public void stopLocationUpdates() {
        // stop updates, disconnect from google api
        isServicesStart = false;
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
    //-----------------------------------------------------------------Location method section ended

    //------------------------------------------------------------------onConnection handler section
    @Override
    public void onConnected(Bundle bundle) {
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // connection to Google Play services was lost for some reason
        if (null != mGoogleApiClient) {
            mGoogleApiClient.connect(); // attempt to establish a new connection
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {    }
    //------------------------------------------------------------onConnection handler section ended

    /*This method is called by object to
    * set variable pathCapture*/
    public void capturePath(boolean value, GoogleMap m) {
        if(value) {
            if(getServicesStart()) {
                mMap = m;
                setCapturePath(true);
                preLoc = mCurLoction;
            } else {
                startLocationUpdates();
            } /*getServicesStart()*/
        } else {
            setCapturePath(false);
            if(!latlngarray.isEmpty()) {
                Log.e("Location", "Location :\n" + latlngarray.toString());
            }
            mMap = m;
        } /*value*/
    }

    //-------------------------------------------------------------------------Setter Method Section
    /*Set pathCapture varible to stored location in array
    false ==> not to stored path
    true ==> store path
    Used by method "capturePath"*/
    public void setCapturePath(boolean value) {
        pathCapture = value;
    }

    public void setPathDetail() { pathDetail = new ArrayList(); }

    public void setLatlngarray() { latlngarray = new ArrayList(); }
    //-------------------------------------------------------------------Setter Method Section Ended

    //----------------------------------------------------------------------------Get Method Section
    public boolean getCapturePath() {
        return pathCapture;
    }

    public boolean getServicesStart() {
        return isServicesStart;
    }

    public List getPathDetail() { return pathDetail; }

    public List getLatlngarray() {
        return latlngarray;
    }

    public Location getLocation() {
        return mCurLoction;
    }

    /**
     * get last available location
     * @return last known location
     */
    public Location getLastLocation() {
        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            // return last location
            return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        else {
            startLocationUpdates(); // start the updates
            return null;
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (mCurLoction != null) {
            latitude = mCurLoction.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (mCurLoction != null) {
            longitude = mCurLoction.getLongitude();
        }
        // return longitude
        return longitude;
    }
    //----------------------------------------------------------------------Get Method Section Ended

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        stopLocationUpdates();
    }
}
