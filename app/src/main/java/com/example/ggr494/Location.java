package com.example.ggr494;

import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.util.Log;

import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Location implements Runnable {

    private static final String TAG = "MYLOCATION";
    private GeoPoint mPoint;
    private String mAddress = "Searching...";
    private double mSelectedDist;

    public Location(GeoPoint point) {
        this.mPoint = point;
        mSelectedDist = 0.75;
        Configuration.getInstance().setUserAgentValue("OsmNavigator/2.2");
    }
    public Location(GeoPoint point, double selectedDist){
        this.mPoint = point;
        this.mSelectedDist = selectedDist;
        Configuration.getInstance().setUserAgentValue("OsmNavigator/2.2");
    }

    public GeoPoint getmPoint() {
        return this.mPoint;
    }

    public GeoPoint getLocation() {
        return this.mPoint;
    }

    public String getAddress() {
        if(mAddress.equals("Searching...")) {
            Log.d("ManagingClass", "Attempting to get location");
            GeocoderNominatim geocoder = new GeocoderNominatim("OsmNavigator/2.2");
            //GeocoderGraphHopper geocoder = new GeocoderGraphHopper(Locale.getDefault(), graphHopperApiKey);
            String theAddress;
            try {
                double dLatitude = this.mPoint.getLatitude();
                double dLongitude = this.mPoint.getLongitude();
                List<Address> addresses = geocoder.getFromLocation(dLatitude, dLongitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    int n = address.getMaxAddressLineIndex();
                    for (int i = 0; i <= n; i++) {
                        if (i != 0)
                            sb.append(", ");
                        sb.append(address.getAddressLine(i));
                    }
                    theAddress = sb.toString();
                } else {
                    theAddress = null;
                }
            } catch (IOException e) {
                theAddress = null;
            }
            if (theAddress != null) {
                Log.d(TAG, theAddress + "");
                this.mAddress = theAddress;
            } else {
                Log.d(TAG, "Failed to find address");
                this.mAddress = "Failed to find Street";
            }
        }
        return this.mAddress;
    }

    public double getmSelectedDist() {
        return mSelectedDist;
    }

    public void setmSelectedDist(double mSelectedDist) {
        this.mSelectedDist = mSelectedDist;
    }

    @Override
    public void run() {
        getAddress();
    }
}
