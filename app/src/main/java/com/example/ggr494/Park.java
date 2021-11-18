package com.example.ggr494;

import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Park implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String TAG = "PARK";
    private final String mParkName;
    private final double mLatitude;
    private final double mLongitude;
    private final String mStreet;
    private final String mStatus;
    private GeoPoint mLocation;
    private ArrayList<Integer> mAmenityAmounts;

    public Park(String parkName, double latitude, double longitude, String street, String status,
                ArrayList<Integer> amenityAmounts){

        this.mParkName = parkName;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mStreet = street;
        this.mStatus = status;
        this.mAmenityAmounts = amenityAmounts;
        this.mLocation = new GeoPoint(mLatitude, mLongitude);
    }

    public String toString(){
        return this.mParkName +" on "+ this.mStreet +" is "+ this.mStatus +".";
    }

    public String getmParkName() {
        return mParkName;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public String getmStreet() {
        return mStreet;
    }

    public String getmStatus() {
        return mStatus;
    }

    public ArrayList<Integer> getmAmenityAmounts() {
        return mAmenityAmounts;
    }

    public String printAmenities() {
        return mAmenityAmounts.toString();
    }

    public GeoPoint getmLocation() {
        return mLocation;
    }

    public String getmAmenityString() {
        StringBuilder toRet = new StringBuilder();
        ArrayList<String> amenityTitle = new ArrayList<>();
        amenityTitle.add(" Baseball Field");
        amenityTitle.add(" Basketball Court");
        amenityTitle.add(" Bocceball Court");
        amenityTitle.add(" Cricket Field");
        amenityTitle.add(" Ice Rink");
        amenityTitle.add(" Play Structure");
        amenityTitle.add(" Soccer Field");
        amenityTitle.add(" Softball Field");
        amenityTitle.add(" Spray Pad");
        amenityTitle.add(" Tennis Court");

        for(int i = 0; i < mAmenityAmounts.size(); i++){
            if(mAmenityAmounts.get(i) > 1){
                toRet.append("<b>").append(mAmenityAmounts.get(i)).append("</b>").append(amenityTitle.get(i)).append("s<br>");
            }
            else if(mAmenityAmounts.get(i) == 1){
                toRet.append("<b>").append(mAmenityAmounts.get(i)).append("</b>").append(amenityTitle.get(i)).append("<br>");
            }
        }
        if(toRet.length() != 0){
            toRet = new StringBuilder(toRet.substring(0, toRet.length() - 4));
        }
        return toRet.toString();
    }

    public String getAmenityName(int amenity){
        ArrayList<String> amenityTitle = new ArrayList<>();
        amenityTitle.add(" Baseball Field");
        amenityTitle.add(" Basketball Court");
        amenityTitle.add(" Bocceball Court");
        amenityTitle.add(" Cricket Field");
        amenityTitle.add(" Ice Rink");
        amenityTitle.add(" Play Structure");
        amenityTitle.add(" Soccer Field");
        amenityTitle.add(" Softball Field");
        amenityTitle.add(" Spray Pad");
        amenityTitle.add(" Tennis Court");
        return (amenityTitle.get(amenity));
    }
}
