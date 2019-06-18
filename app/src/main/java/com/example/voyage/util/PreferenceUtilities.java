package com.example.voyage.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.voyage.R;

public class PreferenceUtilities {
    private static final String PREF_USER_TOKEN = "VOYAGE_USER_TOKEN";

    public static String getUserToken(Context context) {
        String sharedPrefsFile = context.getResources().getString(
                R.string.shared_preference_file);

        SharedPreferences sharedPrefs = context.getSharedPreferences(
                sharedPrefsFile, Context.MODE_PRIVATE);

        return sharedPrefs.getString(PREF_USER_TOKEN, null);
    }
}
