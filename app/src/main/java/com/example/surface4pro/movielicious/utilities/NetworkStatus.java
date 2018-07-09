/*
 * Copyright (c) 2018. Daniel Penz
 */

package com.example.surface4pro.movielicious.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

/**
 * Class containing methods regarding the network status.
 */
public final class NetworkStatus {

    public static final int LOADING_STATUS_NO_CONNECTION = -1;
    public static final int LOADING_STATUS_LOADING = 1;
    public static final int LOADING_STATUS_SUCCESSFUL = 2;

    /**
     * This method checks if an Internet connection is available.
     *
     * @param context a context
     * @return a boolean returning true if a network connection is available
     */
    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager;
        boolean connected;
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo netInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
            connected = netInfo != null && netInfo.isAvailable() &&
                    netInfo.isConnected();
            return connected;
        } catch (Exception e) {
            return false;
        }
    }
}
