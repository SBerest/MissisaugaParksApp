package com.example.ggr494;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import org.apache.commons.text.WordUtils;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MapEventsReceiver, Marker.OnMarkerClickListener{

    private static final String TAG = "MAIN";
    parksWrapper allParks;
    ArrayList<Park> parksToDraw;
    Menu menu;
    MapView map;
    ImageButton locationButton;
    ArrayList<Marker> userLocationMarkers = new ArrayList<>();
    int buttonState = 0; //0 Unclicked, 1 Adding Points, 2 Removing Points

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting up OSM
        org.osmdroid.config.IConfigurationProvider osmConf = org.osmdroid.config.Configuration.getInstance();
        File basePath = new File(getCacheDir().getAbsolutePath(), "osmdroid");
        osmConf.setOsmdroidBasePath(basePath);
        File tileCache = new File(osmConf.getOsmdroidBasePath().getAbsolutePath(), "tile");
        osmConf.setOsmdroidTileCache(tileCache);

        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //Define Map
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        map.setTilesScaledToDpi(true);

        IMapController mapController = map.getController(); 
        mapController.setZoom(10.8);
        GeoPoint startPoint = new GeoPoint(43.593, -79.67);
        mapController.setCenter(startPoint);

        //get all parks
        allParks = readParksData();

        Intent intent = getIntent();

        parksWrapper parksToDrawWrapper = (parksWrapper) intent.getSerializableExtra("filteredParks");
        if(parksToDrawWrapper != null) {
            parksToDraw = parksToDrawWrapper.getParks();
            parksToDraw = filterNotToBeNamed(parksToDraw);
        }
        else{
            Log.d(TAG, "Park Wrap is null");
            parksToDraw = filterNotToBeNamed(allParks);
        }

        //Get the user locations back from the filter tab
        pointsWrapper userWrapper = (pointsWrapper) intent.getSerializableExtra("userLocations");
        if(userWrapper != null) {
            ArrayList<GeoPoint> userLocations = userWrapper.getUserLocationPoints();
            for(GeoPoint p:userLocations){
                Log.d(TAG,"New Marker");
                Marker tempMarker = new Marker(map);
                tempMarker.setPosition(p);
                tempMarker.setTitle("User Location");
                tempMarker.setIcon(getUserLocationDrawable(userLocationMarkers.size()));
                tempMarker.setOnMarkerClickListener(this);
                userLocationMarkers.add(tempMarker);
                map.getOverlays().add(tempMarker);
            }
            map.invalidate();
        }

        //Define the starting location
        RadiusMarkerClusterer parkMarkers = new RadiusMarkerClusterer(this);
        parkMarkers.mTextAnchorU = 0.50f;
        parkMarkers.mTextAnchorV = 0.35f;
        parkMarkers.setRadius(150);
        map.getOverlays().add(parkMarkers);

        Drawable parkIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.park, null);

        for (Park park:parksToDraw){
            Marker parkMarker = new Marker(map);
            parkMarker.setTitle(park.getmParkName());
            parkMarker.setSnippet(park.getmAmenityString());
            parkMarker.setPosition(park.getmLocation());
            parkMarker.setIcon(parkIcon);
            parkMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            parkMarkers.add(parkMarker);
        }
        parkMarkers.setIcon(BitmapFactory.decodeResource(getResources(),R.drawable.marker_poi_cluster));

        locationButton = findViewById(R.id.locationButton);
        locationButton.setOnClickListener(this);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay);

        map.invalidate();
    }

    private ArrayList<Park> filterNotToBeNamed(parksWrapper parksWrap) {
        ArrayList<Park> parks = parksWrap.getParks();
        ArrayList<Park> toRet = new ArrayList<>();
        for(int i = 0; i < parks.size(); i++){
            if(!parks.get(i).getmParkName().contains("NAMED")) {
                toRet.add(parks.get(i));
            }
        }
        return toRet;
    }

    private ArrayList<Park> filterNotToBeNamed(ArrayList<Park> parks) {
        ArrayList<Park> toRet = new ArrayList<>();
        for(int i = 0; i < parks.size(); i++){
            if(!parks.get(i).getmParkName().contains("NAMED")) {
                toRet.add(parks.get(i));
            }
        }
        return toRet;
    }

    private parksWrapper readParksData(){
        ArrayList<Park> parks = new ArrayList<>();
        InputStream is = getResources().openRawResource(R.raw.parks);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        String headers = "";
        try{
            headers=br.readLine();
            while ((line = br.readLine()) != null){

                String[] tokens = line.split(",");
                String parkName = tokens[1];
                parkName = WordUtils.capitalizeFully(parkName);
                double latitude = Double.parseDouble(tokens[9]);
                double longitude = Double.parseDouble(tokens[10]);
                String street = tokens[2] +" "+ tokens[3] +" "+ tokens[4];
                String status = tokens[8];

                ArrayList<Integer> amenityAmounts = new ArrayList<>();
                amenityAmounts.add(Integer.parseInt(tokens[11]));
                amenityAmounts.add(Integer.parseInt(tokens[12]));
                amenityAmounts.add(Integer.parseInt(tokens[13]));
                amenityAmounts.add(Integer.parseInt(tokens[14]));
                amenityAmounts.add(Integer.parseInt(tokens[15]));
                amenityAmounts.add(Integer.parseInt(tokens[16]));
                amenityAmounts.add(Integer.parseInt(tokens[17]));
                amenityAmounts.add(Integer.parseInt(tokens[18]));
                amenityAmounts.add(Integer.parseInt(tokens[19]));
                amenityAmounts.add(Integer.parseInt(tokens[20]));

                if(!parkName.contains("NAMED")) {
                    Park tempPark = new Park(parkName, latitude, longitude, street, status, amenityAmounts);
                    parks.add(tempPark);
                }
            }
        } catch (IOException e){
            Log.wtf(TAG,"Error reading data file on line " + line, e);
            e.printStackTrace();
        }
        return new parksWrapper(parks);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter, menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");

        if (item.getItemId() == R.id.action_filter) {
            Log.d(TAG, "To Filter");
            Intent intent = new Intent(this, FilterActivity.class);
            intent.putExtra("allParks", allParks);
            pointsWrapper userWrapper = new pointsWrapper(userLocationMarkers);
            intent.putExtra("userLocations",userWrapper);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.locationButton) {
            if (buttonState == 0) {
                buttonState = 1;
                locationButton.setImageResource(R.drawable.add_location);
            }
            else if (buttonState == 1) {
                buttonState = 2;
                locationButton.setImageResource(R.drawable.subtractlocation);
            }
            else if (buttonState == 2) {
                buttonState = 0;
                locationButton.setImageResource(R.drawable.location);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG,"Back Pressed");
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        InfoWindow.closeAllInfoWindowsOn(map);
        if(buttonState == 1 && userLocationMarkers.size() < 4){
            Log.d(TAG,"New Marker");
            Marker tempMarker = new Marker(map);
            tempMarker.setPosition(p);
            tempMarker.setTitle("User Location");
            tempMarker.setIcon(getUserLocationDrawable(userLocationMarkers.size()));
            tempMarker.setOnMarkerClickListener(this);
            userLocationMarkers.add(tempMarker);
            map.getOverlays().add(tempMarker);
            map.invalidate();
        }
        return false;
    }

    private Drawable getUserLocationDrawable(int num) {
        switch (num){
            case 0:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.location1,null);
            case 1:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.location2,null);
            case 2:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.location3,null);
            case 3:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.location4,null);
            default:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.location,null);

        }
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        if(buttonState == 2){
            for(Marker userMarker:userLocationMarkers){
                if (userMarker == marker){
                    userLocationMarkers.remove(userMarker);
                    mapView.getOverlays().remove(userMarker);
                    refactorUserLocations();
                    mapView.invalidate();
                    return true;
                }
            }
        }
        else{
            marker.showInfoWindow();
            mapView.getController().animateTo(marker.getPosition());
        }
        return false;
    }

    private void refactorUserLocations() {
        int i = 0;
        for(Marker userLocation:userLocationMarkers){
            userLocation.setIcon(getUserLocationDrawable(i));
            i++;
        }
    }
}