package com.example.ggr494;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.io.Serializable;
import java.util.ArrayList;

public class pointsWrapper implements Serializable{

    private static final long serialVersionUID = 1L;
    private ArrayList<GeoPoint> userLocationPoints = new ArrayList<>();

    public pointsWrapper(ArrayList<Marker> userLocationMarkers) {
        for(int i = 0; i < userLocationMarkers.size(); i++){
            userLocationPoints.add(userLocationMarkers.get(i).getPosition());
        }
    }

    //bool literally just to stop same erasure issue
    public pointsWrapper(ArrayList<Location> userLocations, Boolean bool) {
        for(Location locMarker:userLocations){
            userLocationPoints.add(locMarker.getmPoint());
        }
    }

    public ArrayList<GeoPoint> getUserLocationPoints() {
        return this.userLocationPoints;
    }
}
