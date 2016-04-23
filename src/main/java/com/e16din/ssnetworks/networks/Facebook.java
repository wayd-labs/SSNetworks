package com.e16din.ssnetworks.networks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.e16din.ssnetworks.SSListener;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.vk.sdk.VKSdk;

import java.util.List;

/**
 * Created by e16din on 27.09.15.
 */
public class Facebook extends SSNetwork {//todo: move to SSVkontakte module

    public static final int ACTION_NONE = -1;
    public static final int ACTION_AUTH = 0;
    public static final int ACTION_POST = 1;

    private int currentAction = ACTION_AUTH;
    private OnAuthListener authListener;

    private SimpleFacebook simpleFacebook;

    private Facebook() {
    }

    private static class Holder {
        public static final Facebook HOLDER_INSTANCE = new Facebook();
    }

    public static Facebook getInstance() {
        return Holder.HOLDER_INSTANCE;
    }


    @Override
    public void post(Activity activity, Bitmap bitmap, final String text,
                     final SSListener listener) {
        super.post(activity, bitmap, text, listener);
        //todo:
    }

    public void auth(final Activity activity, String[] scope, OnAuthListener listener) {
        authListener = listener;
        currentAction = ACTION_AUTH;

        simpleFacebook = SimpleFacebook.getInstance(activity);

        simpleFacebook.login(new OnLoginListener() {
            @Override
            public void onLogin(final String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                //(accessToken, simpleFacebook.getAccessToken().getUserId());
                switch (currentAction) {
                    case ACTION_AUTH:
                        if (authListener != null) {
                            authListener.onSuccess(
                                    VKSdk.getAccessToken().accessToken,
                                    VKSdk.getAccessToken().userId);
                        }
                        break;
                    case ACTION_POST:
                        post(activity, SSNetwork.getBitmap(), SSNetwork.getText(), getListener());
                        break;
                }

                currentAction = ACTION_NONE;
            }

            @Override
            public void onCancel() {
                // user canceled the dialog
                Log.i(TAG, "cancel");
            }

            @Override
            public void onFail(String reason) {
                // failed to login
                switch (currentAction) {
                    case ACTION_AUTH:
                        if (authListener != null)
                            authListener.onError(reason);
                        break;
                }

                Log.e(TAG, "error: " + reason);
            }

            @Override
            public void onException(Throwable throwable) {
                // exception from facebook
                switch (currentAction) {
                    case ACTION_AUTH:
                        if (authListener != null)
                            authListener.onError(throwable.toString());
                        break;
                }

                Log.e(TAG, "error: " + throwable.toString());
            }
        });
    }

    @Override
    public void post(Activity activity, String text, SSListener listener) {
        super.post(activity, text, listener);
        //todo:
    }

    @Override
    public boolean onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
        simpleFacebook.onActivityResult(requestCode, resultCode, data);

        return resultCode == Activity.RESULT_OK;
    }
}
