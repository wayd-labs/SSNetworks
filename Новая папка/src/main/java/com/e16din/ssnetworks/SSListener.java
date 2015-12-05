package com.e16din.ssnetworks;

/**
 * Created by e16din on 27.09.15.
 */
public interface SSListener {
    void onComplete(Object result);

    void onError(String message, Object error);
}
