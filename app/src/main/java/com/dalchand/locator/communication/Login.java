package com.dalchand.locator.communication;

import android.util.Log;

import com.dalchand.locator.Constants;
import com.dalchand.locator.HttpRequestHelpers;
import com.dalchand.locator.HttpResponseHelpers;
import com.dalchand.locator.dataobjects.Error;
import com.dalchand.locator.dataobjects.Result;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dalchand on 3/5/15.
 */
public class Login {

    private static CookieManager cookieManager = CookieStore.getInstance().getCookieManager();

    public static Result authenticate(String username, String password) {
        HttpURLConnection connection = null;
        OutputStream os = null;

        try {

            URL url = new URL(Constants.API_URL + "/login");

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            Map<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("password", password);

            byte[] inputParams = HttpRequestHelpers.getFormEncodedParams(map);

            connection.setRequestProperty("Content-Length", String.valueOf(inputParams.length));
            connection.setDoOutput(true);

            os = connection.getOutputStream();
            os.write(inputParams);
            os.flush();
            os.close();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                String response = HttpResponseHelpers.getStringFromInputStream(connection.getErrorStream());
                return Result.parseJSONObject(new JSONObject(response));
            } else {
                return new Result();
            }

        } catch (Exception e) {
            Log.d("Login", e.getMessage(), e);
            Error error = new Error();
            error.code = 1;
            error.reason = "Connection Failed";
            return new Result(error);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
