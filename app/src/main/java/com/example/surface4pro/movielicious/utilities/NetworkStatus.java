package com.example.surface4pro.movielicious.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public final class NetworkStatus {

    public static final int LOADING_STATUS_NO_CONNECTION = -1;
    public static final int LOADING_STATUS_LOADING = 1;
    public static final int LOADING_STATUS_SUCCESSFUL = 2;

    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager;
        boolean connected = false;
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return false;
    }
}
