package com.example.ramapriyasridharan.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.ramapriyasridharan.BackgroundTasks.AlarmNextDayService;
import com.example.ramapriyasridharan.BackgroundTasks.NotificationService;

/**
 * Created by ramapriyasridharan on 01.06.16.
 * Receive Alarms for Notifications and Next day Change
 */
public class AlarmReceiver extends WakefulBroadcastReceiver{

    public static String NOTIFICATION_ACTION = "com.example.ramapriyasridharan.alarm.notification";
    public static String NEXT_DAY_CHANGE_ACTION = "com.example.ramapriyasridharan.alarm.next.day.change";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("ALARM", "action = "+action);
        if(NOTIFICATION_ACTION.equals(action)){
            Log.d("ALARM", "calling notitifcation service");
            startWakefulService(context, new Intent(context, NotificationService.class));

        }
        else if(NEXT_DAY_CHANGE_ACTION.equals(action)){
            Log.d("ALARM", "calling next day service");
            startWakefulService(context, new Intent(context, AlarmNextDayService.class));
        }
    }
}
