package com.e16din.ssnetworks.apps;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.e16din.lightutils.DataManager;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by e16din on 12.09.15.
 */
public class SSNetworksMultidexApplication extends MultiDexApplication {

    private VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                Log.d("TheRing", "newToken: " + newToken);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        DataManager.init(this);
    }
}