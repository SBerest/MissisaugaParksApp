
/**
 * Reverse Geocoding
 */
/*
import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public String getAddress(GeoPoint p){
        GeocoderNominatim geocoder = new GeocoderNominatim(userAgent);
        //GeocoderGraphHopper geocoder = new GeocoderGraphHopper(Locale.getDefault(), graphHopperApiKey);
        String theAddress;
        try {
        double dLatitude = p.getLatitude();
        double dLongitude = p.getLongitude();
        List<Address> addresses = geocoder.getFromLocation(dLatitude, dLongitude, 1);
        StringBuilder sb = new StringBuilder();
        if (addresses.size() > 0) {
        Address address = addresses.get(0);
        int n = address.getMaxAddressLineIndex();
        for (int i=0; i<=n; i++) {
        if (i!=0)
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
        return theAddress;
        } else {
        return "";
        }
        }

private class GeocodingTask extends AsyncTask<Object, Void, List<Address>> {
    int mIndex;
    protected List<Address> doInBackground(Object... params) {
        String locationAddress = (String)params[0];
        mIndex = (Integer)params[1];
        GeocoderNominatim geocoder = new GeocoderNominatim(userAgent);
        geocoder.setOptions(true); //ask for enclosing polygon (if any)
        //GeocoderGraphHopper geocoder = new GeocoderGraphHopper(Locale.getDefault(), graphHopperApiKey);
        try {
            BoundingBox viewbox = map.getBoundingBox();
            List<Address> foundAdresses = geocoder.getFromLocationName(locationAddress, 1,
                    viewbox.getLatSouth(), viewbox.getLonEast(),
                    viewbox.getLatNorth(), viewbox.getLonWest(), false);
            return foundAdresses;
        } catch (Exception e) {
            return null;
        }
    }
    protected void onPostExecute(List<Address> foundAdresses) {
        if (foundAdresses == null) {
            Toast.makeText(getApplicationContext(), "Geocoding error", Toast.LENGTH_SHORT).show();
        } else if (foundAdresses.size() == 0) { //if no address found, display an error
            Toast.makeText(getApplicationContext(), "Address not found.", Toast.LENGTH_SHORT).show();
        } else {
            Address address = foundAdresses.get(0); //get first address
            String addressDisplayName = address.getExtras().getString("display_name");
            if (mIndex == START_INDEX){
                startPoint = new GeoPoint(address.getLatitude(), address.getLongitude());
                markerStart = updateItineraryMarker(markerStart, startPoint, START_INDEX,
                        R.string.departure, R.drawable.marker_departure, -1, addressDisplayName);
                map.getController().setCenter(startPoint);
            } else if (mIndex == DEST_INDEX){
                destinationPoint = new GeoPoint(address.getLatitude(), address.getLongitude());
                markerDestination = updateItineraryMarker(markerDestination, destinationPoint, DEST_INDEX,
                        R.string.destination, R.drawable.marker_destination, -1, addressDisplayName);
                map.getController().setCenter(destinationPoint);
            }
            getRoadAsync();
            //get and display enclosing polygon:
            Bundle extras = address.getExtras();
            if (extras != null && extras.containsKey("polygonpoints")){
                ArrayList<GeoPoint> polygon = extras.getParcelableArrayList("polygonpoints");
                //Log.d("DEBUG", "polygon:"+polygon.size());
                updateUIWithPolygon(polygon, addressDisplayName);
            } else {
                updateUIWithPolygon(null, "");
            }
        }
    }
}

    /**
     * Geocoding of the departure or destination address
     */
/*
    public void handleSearchButton(int index, int editResId){
        EditText locationEdit = (EditText)findViewById(editResId);
        //Hide the soft keyboard:
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(locationEdit.getWindowToken(), 0);

        String locationAddress = locationEdit.getText().toString();

        if (locationAddress.equals("")){
            removePoint(index);
            map.invalidate();
            return;
        }

        Toast.makeText(this, "Searching:\n"+locationAddress, Toast.LENGTH_LONG).show();
        AutoCompleteOnPreferences.storePreference(this, locationAddress, SHARED_PREFS_APPKEY, PREF_LOCATIONS_KEY);
        new GeocodingTask().execute(locationAddress, index);
    }
*/