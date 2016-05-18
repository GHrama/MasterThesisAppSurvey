package com.example.ramapriyasridharan.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ramapriyasridharan on 17.05.16.
 */
public class SharedPreferencesHelper {

    // insert double intro sharedpreferences file
    static public SharedPreferences getSharedPreferencesHandle(Context c, String s) {
        return c.getSharedPreferences(s, Context.MODE_PRIVATE);
    }
}
