package com.example.voyage.util;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static void handleError(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            int statusCode = httpException.code();
            Toast.makeText(ApplicationContextProvider.getContext(),
                    "Http error. Status code: " + statusCode,
                    Toast.LENGTH_LONG)
                    .show();
            Log.d(LOG_TAG, "Http error. Status code: " + statusCode);
        } else if (throwable instanceof SocketTimeoutException) {
            Toast.makeText(ApplicationContextProvider.getContext(),
                    "Connection timed out. Please try again",
                    Toast.LENGTH_LONG)
                    .show();
            Log.d(LOG_TAG, throwable.getMessage());
        } else if (throwable instanceof IOException) {
            Toast.makeText(ApplicationContextProvider.getContext(),
                    "Host unreachable. Check your internet connection",
                    Toast.LENGTH_LONG)
                    .show();
            Log.d(LOG_TAG, throwable.getMessage());
        } else {
            Toast.makeText(ApplicationContextProvider.getContext(),
                    "Fatal error of unknown type. Contact app developers",
                    Toast.LENGTH_LONG)
                    .show();
            throwable.printStackTrace();
            Log.d(LOG_TAG, throwable.getMessage());
        }
    }
}
