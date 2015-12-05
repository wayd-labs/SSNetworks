package com.e16din.ssnetworks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.e16din.ssnetworks.networks.SSNetwork;
import com.e16din.ssnetworks.networks.Vkontakte;

/**
 * Created by e16din on 27.09.15.
 */
public class SSMaster {

    public static final int ID_VKONTAKTE = 0;
    public static final int ID_FACEBOOK = 1;
    public static final int ID_TWITTER = 2;
    public static final int ID_ODNOCLASSNIKI = 3;
    public static final int ID_INSTAGRAM = 4;


    public static void post(int networkId, Activity activity, Bitmap bitmap, String text, SSListener listener) {
        switch (networkId) {
            case ID_VKONTAKTE:
                Vkontakte.getInstance().post(activity, bitmap, text, listener);
                break;
            case ID_FACEBOOK:
                //todo
                break;
            case ID_TWITTER:
                //todo
                break;
            case ID_ODNOCLASSNIKI:
                //todo
                break;
            case ID_INSTAGRAM:
                //todo
                break;
        }
    }

    public static void post(int networkId, Activity activity, int imageResId, final String text, SSListener listener) {
        post(networkId, activity, BitmapFactory.decodeResource(activity.getResources(), imageResId), text, listener);
    }

    public static void post(int networkId, Activity activity, ImageView iv, final String text, SSListener listener) {
        post(networkId, activity, SSNetwork.getBitmap(iv), text, listener);
    }

    public static void post(int networkId, Activity activity, String text, SSListener listener) {
        switch (networkId) {
            case ID_VKONTAKTE:
                Vkontakte.getInstance().post(activity, text, listener);
                break;
            case ID_FACEBOOK:
                //todo
                break;
            case ID_TWITTER:
                //todo
                break;
            case ID_ODNOCLASSNIKI:
                //todo
                break;
            case ID_INSTAGRAM:
                //todo
                break;
        }
    }

    public static void post(int networkId, Activity activity, ImageView iv, final String text) {
        post(networkId, activity, iv, text, null);
    }

    public static void post(int networkId, Activity activity, String text) {
        post(networkId, activity, text, null);
    }

    public static void post(int networkId, Activity activity, Bitmap bitmap, final String text) {
        post(networkId, activity, bitmap, text, null);
    }

    public static boolean onActivityResult(Activity activity, int networkId, int requestCode, int resultCode, Intent data) {
        switch (networkId) {
            case ID_VKONTAKTE:
                return Vkontakte.getInstance().onActivityResult(activity, requestCode, resultCode, data);
            case ID_FACEBOOK:
                //todo
                break;
            case ID_TWITTER:
                //todo
                break;
            case ID_ODNOCLASSNIKI:
                //todo
                break;
            case ID_INSTAGRAM:
                //todo
                break;
        }

        return false;
    }
}
