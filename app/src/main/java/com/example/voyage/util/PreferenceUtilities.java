package com.example.voyage.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.voyage.R;

public class PreferenceUtilities {
    private static final String PREF_USER_TOKEN = "VOYAGE_USER_TOKEN";
    private static final String PREF_FCM_TOKEN = "FCM_USER_TOKEN";

    public static String getUserToken(Context context) {
        String sharedPrefsFile = context.getResources().getString(
                R.string.preference_file_key);

        SharedPreferences sharedPrefs = context.getSharedPreferences(
                sharedPrefsFile, Context.MODE_PRIVATE);

        return sharedPrefs.getString(PREF_USER_TOKEN, null);
    }

    public static void setUserToken(Context context, String token) {
        String sharedPrefsFile = context.getResources().getString(
                R.string.preference_file_key);

        SharedPreferences sharedPref = context.getSharedPreferences(
                sharedPrefsFile, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        if (token == null) {
            Log.d("TAG", "DELETING TOKEN");
            editor.remove(PREF_USER_TOKEN);
        } else {
            editor.putString(PREF_USER_TOKEN, token);
        }
        editor.apply();
    }

    public static String getPrefFcmToken(Context context) {
        String sharedPrefsFile = context.getResources().getString(
                R.string.preference_file_key);

        SharedPreferences sharedPrefs = context.getSharedPreferences(
                sharedPrefsFile, Context.MODE_PRIVATE);

        return sharedPrefs.getString(PREF_FCM_TOKEN, null);
    }

    public static void saveFcmToken(Context context, String fcmToken) {
        String sharedPrefsFile = context.getResources().getString(
                R.string.preference_file_key);

        SharedPreferences sharedPref = context.getSharedPreferences(
                sharedPrefsFile, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        if (fcmToken == null) {
            Log.d("TAG", "DELETING FCM TOKEN");
            editor.remove(PREF_FCM_TOKEN);
        } else {
            editor.putString(PREF_FCM_TOKEN, fcmToken);
        }
        editor.apply();
    }
}
