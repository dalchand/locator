package com.dalchand.locator.dataobjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by dalchand on 3/5/15.
 */
public class Result {

    public Error error;
    public Object data;

    public Result(){

    }

    public Result(Error error) {
        this.error = error;
    }

    public static Result parseJSONObject(JSONObject object) throws JSONException {
        Result result = new Result();
        if(object != null) {
            if(object.has("error")) {
                result.error = Error.parseJSONObject(object.getJSONObject("error"));
            }
            if(object.has("data")) {
                result.data = object.get("data");
            }
        }
        return result;
    }
}
