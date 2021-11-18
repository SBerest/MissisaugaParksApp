package com.example.ggr494;

import android.app.Application;
import android.graphics.Point;
import android.location.Address;
import android.util.Log;

import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManagingClass extends Application {
    ArrayList<Park> allParks = null;
    ArrayList<Park> parksToDraw = null;
    ArrayList<Location> userLocations = null;
    ArrayList<Integer> amenityFilter = new ArrayList<>();

}
