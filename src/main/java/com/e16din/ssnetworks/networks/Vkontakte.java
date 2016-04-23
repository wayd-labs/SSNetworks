package com.e16din.ssnetworks.networks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.e16din.lightutils.tools.DataManager;
import com.e16din.ssnetworks.SSListener;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

/**
 * Created by e16din on 27.09.15.
 */
public class Vkontakte extends SSNetwork {//todo: move to SSVkontakte module

    public static final int ACTION_NONE = -1;
    public static final int ACTION_AUTH = 0;
    public static final int ACTION_POST = 1;

    private int currentAction = ACTION_AUTH;
    private OnAuthListener authListener;

    private Vkontakte() {
    }

    private static class Holder {
        public static final Vkontakte HOLDER_INSTANCE = new Vkontakte();
    }

    public static Vkontakte getInstance() {
        return Holder.HOLDER_INSTANCE;
    }

    //private VKRequest request;

    @Override
    public void post(Activity activity, Bitmap bitmap, final String text,
                     final SSListener listener) {
        super.post(activity, bitmap, text, listener);
        if (bitmap == null) {
            post(activity, text, listener);
            return;
        }

        currentAction = ACTION_POST;
        if (authIfNeed(activity, text)) return;

        VKRequest request = VKApi.uploadWallPhotoRequest(
                new VKUploadImage(bitmap, VKImageParameters.jpgImage(0.9f)), 0, 0);

        request.attempts = 10;
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                String error = request.toString() + " attemptNumber"
                        + String.valueOf(attemptNumber) + " totalAttempts"
                        + String.valueOf(totalAttempts);
                Log.e(TAG, "error: " + error);

                if (listener != null)
                    listener.onError(error, request);
            }

            @Override
            public void onComplete(VKResponse response) {
                SSNetwork.getBitmap().recycle();
                VKApiPhoto photo = ((VKPhotoArray) response.parsedModel).get(0);

                Log.d(TAG, "photo: " + photo);

                VKRequest request = VKApi.wall().post(
                        VKParameters.from(VKApiConst.OWNER_ID, VKSdk.getAccessToken().userId,
                                VKApiConst.ATTACHMENTS, new VKAttachments(photo),
                                VKApiConst.MESSAGE, text));
                postVkRequest(request, listener);
            }

            @Override
            public void onError(VKError error) {
                Log.e(TAG, "error: " + error);

                if (listener != null)
                    listener.onError((error != null ? error.errorMessage : null), error);
            }
        });
    }

    public void auth(Activity activity, String[] scope, OnAuthListener listener) {
        authListener = listener;
        currentAction = ACTION_AUTH;
        VKSdk.login(activity, scope);
    }

    //true if need authorization
    private boolean authIfNeed(Activity activity, String text) {
        if (!VKSdk.isLoggedIn()) {
            DataManager.getInstance().save(VKApiConst.MESSAGE, text);
            String[] VK_SCOPE = new String[]{VKScope.WALL,
                    VKScope.PHOTOS};
            auth(activity, VK_SCOPE, null);
            return true;
        }
        return false;
    }

    @Override
    public void post(Activity activity, String text, SSListener listener) {
        super.post(activity, text, listener);

        currentAction = ACTION_POST;
        if (authIfNeed(activity, text)) return;

        VKRequest request = VKApi.wall().post(
                VKParameters.from(VKApiConst.OWNER_ID, VKSdk.getAccessToken().userId,
                        VKApiConst.MESSAGE, text));

        postVkRequest(request, listener);
    }

    private void postVkRequest(VKRequest post, final SSListener listener) {
        post.setModelClass(VKWallPostResult.class);
        post.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                if (listener != null)
                    listener.onComplete(response);
            }

            @Override
            public void onError(VKError error) {
                Log.e(TAG, "error: " + (error != null ? error.errorMessage : null));

                if (listener != null)
                    listener.onError(error != null ? error.errorMessage : null, error);
            }
        });
    }

    @Override
    public boolean onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
        return VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
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
            public void onError(VKError error) {
                switch (currentAction) {
                    case ACTION_AUTH:
                        if (authListener != null)
                            authListener.onError(error != null ? error.errorMessage : null);
                        break;
                }


                Log.e(TAG, "error: " + (error != null ? error.errorMessage : null));
            }
        });
    }
}
