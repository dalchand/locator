package com.dalchand.locator.communication;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by dalchand on 3/5/15.
 */
public class CookieStore {

    private static CookieStore instance;

    private CookieManager cookieManager;

    private CookieStore() {
        cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
    }

    public static synchronized  CookieStore getInstance() {
        if(instance == null) {
            instance = new CookieStore();
        }
        return instance;
    }

    public CookieManager getCookieManager() {
        return this.cookieManager;
    }

}
