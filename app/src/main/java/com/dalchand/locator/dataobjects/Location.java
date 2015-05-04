package com.dalchand.locator.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dalchand on 3/5/15.
 */
public class Location {

    public double longitude;
    public double latitude;

    public Location() {

    }

    public Location(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("latitude", latitude);
        object.put("longitude", longitude);
        return object;
    }

    public static Location parseJSONObject(JSONObject object) throws JSONException {
        Location location = new Location();
        if(object.has("latitude")) {
            location.latitude = object.getDouble("latitude");
        }
        if(object.has("longitude")) {
            location.longitude = object.getDouble("longitude");
        }
        return location;
    }


}
