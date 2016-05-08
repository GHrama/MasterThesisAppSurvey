package com.example.ramapriyasridharan.trialapp04;

import com.kinvey.android.Client;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

/**
 * Created by ramapriyasridharan on 10.03.16.
 */
public class UserInstanceClass {
    // all instance share the same instance of this variable
    static Client mKinveyClient;

    public static void setmKinveyClient(Client mKinveyClient) {
        UserInstanceClass.mKinveyClient = mKinveyClient;
    }

    public static Client getmKinveyClient() {
        return mKinveyClient;
    }
}
