package com.e16din.ssnetworks.apps;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.e16din.lightutils.LightUtils;
import com.e16din.ssnetworks.R;
import com.sromku.simple.fb.BuildConfig;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
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
                Log.d("SSNetworks", "newToken: " + newToken);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        LightUtils.init(this);

        Permission[] permissions = new Permission[]{Permission.USER_ABOUT_ME};
        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(getString(R.string.facebook_app_id))
                .setNamespace(BuildConfig.APPLICATION_ID)
                .setPermissions(permissions)
                .build();
        SimpleFacebook.setConfiguration(configuration);
    }
}