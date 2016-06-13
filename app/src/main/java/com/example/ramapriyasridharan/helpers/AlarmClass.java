package com.example.ramapriyasridharan.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ramapriyasridharan.BroadcastReceivers.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by ramapriyasridharan on 13.06.16.
 */
public class AlarmClass {

    public static String NOTIFICATION_ACTION = "com.example.ramapriyasridharan.alarm.notification";
    public static String NEXT_DAY_CHANGE_ACTION = "com.example.ramapriyasridharan.alarm.next.day.change";

    public void setApproxNotificationAlarm(Context context){
        Intent intent_notification;
        Log.d("ALARM", "create notification alarm");
        intent_notification = new Intent(context, AlarmReceiver.class);
        intent_notification.setAction(NOTIFICATION_ACTION);
        PendingIntent pi_notification;
        pi_notification = PendingIntent.getBroadcast(context, 111, intent_notification, 0);
        AlarmManager alarm_notification;
        alarm_notification = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarm_notification.set(alarm_notification.RTC_WAKEUP, System.currentTimeMillis() + 300 * 1000, pi_notification);
    }

    // Ring after 24 hours
    public void setExactNextDayChangeAlarm(Context context){
        // At a time
        Log.d("ALARM", "create next day alarm");
        Intent intent_next_day_change;
        intent_next_day_change = new Intent(context, AlarmReceiver.class);
        intent_next_day_change.setAction(NEXT_DAY_CHANGE_ACTION);
        PendingIntent pi_next_day_change;
        pi_next_day_change = PendingIntent.getBroadcast(context, 1003, intent_next_day_change,0);
        AlarmManager alarm_next_day_change;
        alarm_next_day_change = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        // set for 24 hours from now
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarm_next_day_change.setExact(alarm_next_day_change.RTC_WAKEUP, System.currentTimeMillis() + 86400*1000, pi_next_day_change);
        } else {
            alarm_next_day_change.set(alarm_next_day_change.RTC_WAKEUP, System.currentTimeMillis() + 86400*1000, pi_next_day_change);
        }

    }
}
