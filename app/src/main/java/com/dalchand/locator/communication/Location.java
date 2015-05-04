package com.dalchand.locator.communication;

import android.util.Log;

import com.dalchand.locator.Constants;
import com.dalchand.locator.HttpResponseHelpers;
import com.dalchand.locator.dataobjects.Result;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dalchand on 3/5/15.
 */
public class Location {

    private static CookieManager cookieManager = CookieStore.getInstance().getCookieManager();


    public static com.dalchand.locator.dataobjects.Location getFriendLocation() {
        HttpURLConnection connection = null;
        OutputStream os = null;

        try {

            URL url = new URL(Constants.API_URL + "/location");

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            Log.d("ResponseCode", String.valueOf(connection.getResponseCode()));

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String response = HttpResponseHelpers.getStringFromInputStream(connection.getInputStream());
                Result result = Result.parseJSONObject(new JSONObject(response));
                return com.dalchand.locator.dataobjects.Location.parseJSONObject((JSONObject) result.data);
            }

        } catch (Exception e) {
            Log.e("getLocation", e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static boolean updateLocation(com.dalchand.locator.dataobjects.Location location) {

        HttpURLConnection connection = null;
        OutputStream os = null;

        try {

            URL url = new URL(Constants.API_URL + "/location");

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            byte[] inputParams = location.toJSONObject().toString().getBytes("UTF-8");

            connection.setRequestProperty("Content-Length", String.valueOf(inputParams.length));
            connection.setDoOutput(true);
            connection.connect();
            os = connection.getOutputStream();
            os.write(inputParams);
            os.flush();
            os.close();

            Log.d("ResponseCode", String.valueOf(connection.getResponseCode()));

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }

        } catch (Exception e) {
            Log.e("updateLocation", e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }
}
