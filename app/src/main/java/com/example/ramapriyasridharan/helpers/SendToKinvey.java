package com.example.ramapriyasridharan.helpers;

import android.util.Log;

import com.example.ramapriyasridharan.collections.ProfilingContextsClass;
import com.example.ramapriyasridharan.collections.ProfilingDataCollectorsClass;
import com.example.ramapriyasridharan.collections.ProfilingFeaturesClass;
import com.example.ramapriyasridharan.collections.ProfilingSensorsClass;
import com.example.ramapriyasridharan.collections.UserIdClass;
import com.example.ramapriyasridharan.collections.UserInformationClass;
import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;

/**
 * Created by ramapriyasridharan on 24.05.16.
 */
public class SendToKinvey {

    public static void sendUserInformation(UserInstanceClass user_instance, String collection_name, UserInformationClass ui){

        AsyncAppData<UserInformationClass> myui = user_instance.getmKinveyClient().appData(collection_name, UserInformationClass.class);

                myui.save(ui, new KinveyClientCallback<UserInformationClass>() {

                    @Override
                    public void onSuccess(UserInformationClass userInformationClass) {
                        Log.i("sent to kinvey", "suceess");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                        Log.i("ERROR sending to kinvey", "ERROR");
                    }
                });
    }

    public static void sendProfilingFeatures(UserInstanceClass user_instance, String collection_name, ProfilingFeaturesClass pf){

        AsyncAppData<ProfilingFeaturesClass> myui = user_instance.getmKinveyClient().appData(collection_name, ProfilingFeaturesClass.class);

        myui.save(pf, new KinveyClientCallback<ProfilingFeaturesClass>() {

            @Override
            public void onSuccess(ProfilingFeaturesClass f) {
                Log.i("sent to kinvey", "suceess");
            }

            @Override
            public void onFailure(Throwable throwable) {

                Log.i("ERROR sending to kinvey", "ERROR");
            }
        });
    }

    public static void sendProfilingSensors(UserInstanceClass user_instance, String collection_name, ProfilingSensorsClass ps){

        AsyncAppData<ProfilingSensorsClass> myui = user_instance.getmKinveyClient().appData(collection_name, ProfilingSensorsClass.class);

        myui.save(ps, new KinveyClientCallback<ProfilingSensorsClass>() {

            @Override
            public void onSuccess(ProfilingSensorsClass f) {
                Log.i("sent to kinvey", "suceess");
            }

            @Override
            public void onFailure(Throwable throwable) {

                Log.i("ERROR sending to kinvey", "ERROR");
            }
        });
    }

    public static void sendProfilingDataCollectors(UserInstanceClass user_instance, String collection_name, ProfilingDataCollectorsClass ps){

        AsyncAppData<ProfilingDataCollectorsClass> myui = user_instance.getmKinveyClient().appData(collection_name, ProfilingDataCollectorsClass.class);

        myui.save(ps, new KinveyClientCallback<ProfilingDataCollectorsClass>() {

            @Override
            public void onSuccess(ProfilingDataCollectorsClass f) {
                Log.i("sent to kinvey", "suceess");
            }

            @Override
            public void onFailure(Throwable throwable) {

                Log.i("ERROR sending to kinvey", "ERROR");
            }
        });
    }

    public static void sendProfilingContexts(UserInstanceClass user_instance, String collection_name, ProfilingContextsClass ps){

        AsyncAppData<ProfilingContextsClass> myui = user_instance.getmKinveyClient().appData(collection_name, ProfilingContextsClass.class);

        myui.save(ps, new KinveyClientCallback<ProfilingContextsClass>() {

            @Override
            public void onSuccess(ProfilingContextsClass f) {
                Log.i("sent to kinvey", "suceess");
            }

            @Override
            public void onFailure(Throwable throwable) {

                Log.i("ERROR sending to kinvey", "ERROR");
            }
        });
    }

    public static void sendUserId(UserInstanceClass user_instance, String collection_name, UserIdClass ui){

        AsyncAppData<UserIdClass> myui = user_instance.getmKinveyClient().appData(collection_name, UserIdClass.class);

        myui.save(ui, new KinveyClientCallback<UserIdClass>() {

            @Override
            public void onSuccess(UserIdClass userInformationClass) {
                Log.i("sent to kinvey", "suceess USER ID");
            }

            @Override
            public void onFailure(Throwable throwable) {

                Log.i("ERROR sending to kinvey", "ERROR USER ID");
            }
        });
    }

}
