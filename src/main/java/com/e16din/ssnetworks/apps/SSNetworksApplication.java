package com.e16din.ssnetworks.apps;

import android.app.Application;
import android.util.Log;

import com.e16din.lightutils.LightUtils;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by e16din on 12.09.15.
 */
public class SSNetworksApplication extends Application {

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
        LightUtils.init(this);
    }
}