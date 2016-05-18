package com.example.ramapriyasridharan.helpers;

import android.content.SharedPreferences;

/**
 * Created by ramapriyasridharan on 17.05.16.
 */
public class AddDouble {


    // insert double intro sharedpreferences file
    static public SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }


    // get double from sharedpreferences file
    static public double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
