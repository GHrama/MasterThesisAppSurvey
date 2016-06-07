package com.example.ramapriyasridharan.helpers;

import android.content.Context;

import com.example.ramapriyasridharan.localstore.StoreDbHelper;
/**
 * Created by ramapriyasridharan on 30.05.16.
 */
public class DatabaseInstance {
    public static StoreDbHelper db = null;

    public DatabaseInstance(Context c){
        db = new StoreDbHelper(c);
    }
}
