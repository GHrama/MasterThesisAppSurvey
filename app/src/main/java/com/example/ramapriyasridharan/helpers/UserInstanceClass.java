package com.example.ramapriyasridharan.helpers;

import com.kinvey.android.Client;

/**
 * Created by ramapriyasridharan on 10.03.16.
 */
public class UserInstanceClass {
    // all instance share the same instance of this variable
    static public Client mKinveyClient;

    public static void setmKinveyClient(Client mKinveyClient) {
        UserInstanceClass.mKinveyClient = mKinveyClient;
    }

    public static Client getmKinveyClient() {
        return mKinveyClient;
    }
}
