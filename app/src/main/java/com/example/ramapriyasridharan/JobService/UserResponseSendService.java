package com.example.ramapriyasridharan.JobService;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.ramapriyasridharan.StoreValues.UserResponseTableStore;
import com.example.ramapriyasridharan.collections.UserResponseClass;
import com.example.ramapriyasridharan.helpers.DatabaseInstance;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;
import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;

import java.util.ArrayList;


/**
 * Created by ramapriyasridharan on 03.06.16.
 */
public class UserResponseSendService extends GcmTaskService {

    StoreDbHelper db = DatabaseInstance.db;
    private static final String TAG = UserResponseSendService.class.getSimpleName();
    UserInstanceClass userInstanceClass = new UserInstanceClass();
    public static final String GCM_REPEAT_TAG = "repeat|[7200,1800]";

    @Override
    public void onInitializeTasks() {
        //called when app is updated to a new version, reinstalled etc.
        //you have to schedule your repeating tasks again
        super.onInitializeTasks();
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        //do some stuff (mostly network) - executed in background thread (async)
        //obtain your data
        Bundle extras = taskParams.getExtras();
        UserInstanceClass user_instance = new UserInstanceClass();
        Log.v(TAG, "onRunTask");
        if(taskParams.getTag().equals(GCM_REPEAT_TAG)) {
                    ArrayList<UserResponseTableStore> ur_array = db.getUserResponseAndSendTable();
                    Log.d("UR SERVICE", "ur_array size ="+ur_array.size());
                    for(int i = 0;i< ur_array.size();i++){
                        Log.d("UR SERVICE", "data ur_array key ="+ur_array.get(i).key);

                        AsyncAppData<UserResponseClass> myui = user_instance.getmKinveyClient().appData("UserResponse2", UserResponseClass.class);

                        final int key = ur_array.get(i).key;
                        myui.save(ur_array.get(i).ur, new KinveyClientCallback<UserResponseClass>() {
                            @Override
                            public void onSuccess(UserResponseClass f) {
                                Log.d("UR SERVICE", "success sent!");
                                db.updateSentUserResponse(key);
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                Log.d("UR SERVICE", "Failed to send!");

                            }
                        });
                    }
                }

        return GcmNetworkManager.RESULT_SUCCESS;
    }

    public static void scheduleRepeat(Context context) {
        //in this method, single Repeating task is scheduled (the target service that will be called is MyTaskService.class)
        try {
            PeriodicTask periodic = new PeriodicTask.Builder()
                    //specify target service - must extend GcmTaskService
                    .setService(UserResponseSendService.class)
                            //repeat every 60 seconds
                    .setPeriod(500)
                            //specify how much earlier the task can be executed (in seconds)
                    .setFlex(500)
                            //tag that is unique to this task (can be used to cancel task)
                    .setTag(GCM_REPEAT_TAG)
                            //whether the task persists after device reboot
                    .setPersisted(true)
                            //if another task with same tag is already scheduled, replace it with this task
                    .setUpdateCurrent(true)
                            //set required network state, this line is optional
                    .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                            //request that charging must be connected, this line is optional
                    .setRequiresCharging(false)
                    .build();
            GcmNetworkManager.getInstance(context).schedule(periodic);
            Log.d(TAG, "repeating task scheduled user response cache");
        } catch (Exception e) {
            Log.d(TAG, "scheduling failed user response cache");
            e.printStackTrace();
        }
    }

    public static void cancelRepeat(Context context) {
        GcmNetworkManager
                .getInstance(context)
                .cancelTask(GCM_REPEAT_TAG, UserResponseSendService.class);
    }

}
