package com.dalchand.locator.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dalchand on 3/5/15.
 */
public class Error {

    public int code;
    public String reason;

    public static Error parseJSONObject(JSONObject json) throws JSONException {
        Error error = new Error();
        if(json != null) {
            if(json.has("code")) {
                error.code = json.getInt("code");
            }
            if(json.has("reason")) {
                error.reason = json.getString("reason");
            }
        }
        return error;
    }

    @Override
    public String toString() {
        return "Error: " + "[" + this.code + "] : " + this.reason;
    }
}
