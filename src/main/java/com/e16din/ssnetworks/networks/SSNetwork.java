package com.e16din.ssnetworks.networks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.e16din.ssnetworks.SSListener;

/**
 * Created by e16din on 27.09.15.
 */
public abstract class SSNetwork {
    public static final String TAG = "SSNetworks";

    private static Bitmap bitmap;
    private static String text;

    private static SSListener listener;

    //override it
    public void post(Activity activity, Bitmap bitmap, String text, SSListener listener) {
        SSNetwork.setListener(listener);
        SSNetwork.setText(text);
        SSNetwork.setBitmap(bitmap);
    }

    //override it
    public void post(Activity activity, String text, SSListener listener) {
        SSNetwork.setListener(listener);
        SSNetwork.setText(text);
    }

    //override it
    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        return false;
    }

    public static Bitmap getBitmap(ImageView iv) {//todo move to LightUtils
        return ((BitmapDrawable) iv.getDrawable()).getBitmap();
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        SSNetwork.bitmap = bitmap;
    }

    public static String getText() {
        return text;
    }

    public static void setText(String text) {
        SSNetwork.text = text;
    }

    public static SSListener getListener() {
        return listener;
    }

    public static void setListener(SSListener listener) {
        SSNetwork.listener = listener;
    }

    public interface OnAuthListener {
        void onSuccess(String accessToken, String userId);

        void onError(String message);
    }
}
