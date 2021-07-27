package com.example.ggr494;

import android.util.Log;

import org.osmdroid.util.GeoPoint;

public class MyCircle {
    private GeoPoint mCenter;
    private double mRadius;

    MyCircle(GeoPoint center, double radius){
        mCenter = center;
        mRadius = radius;
    }

    public MyCircle(GeoPoint mid,GeoPoint point) {
        mCenter = mid;
        mRadius = calcDist(point);
    }

    public boolean contains(GeoPoint point0){
        return calcDist(point0) <= mRadius;
    }

    private double calcDist(GeoPoint point0){
        double lat0 = point0.getLatitude();
        double lon0 = point0.getLongitude();
        double lat1 = mCenter.getLatitude();
        double lon1 = mCenter.getLongitude();

        if ((lat0 == lat1) && (lon0 == lon1)) {
            return 0;
        }
        else {
            //From https://www.geodatasource.com/developers/java Euclidean Distance then convert to km
            double theta = lon0 - lon1;
            double dist = Math.sin(Math.toRadians(lat0)) * Math.sin(Math.toRadians(lat1)) + Math.cos(Math.toRadians(lat0)) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;

            //Convert to km
            dist = dist * 1.609344;
            return dist;
        }
    }

    public GeoPoint getmCenter() {
        return mCenter;
    }

    public void setmCenter(GeoPoint mCenter) {
        this.mCenter = mCenter;
    }

    public double getmRadius() {
        return mRadius;
    }

    public void setmRadius(double mRadius) {
        this.mRadius = mRadius;
    }
}
