package com.example.ramapriyasridharan.BackgroundTasks;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ramapriyasridharan.AsyncTasks.GoToNextDay;
import com.example.ramapriyasridharan.BroadcastReceivers.AlarmReceiver;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ramapriyasridharan on 28.05.16.
 */
public class AlarmNextDayService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("SERVICE NEXT DAY", "started");
        // START YOUR TASKS
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("SERVICE NEXT DAY", "destroyed");
        // STOP YOUR TASKS
        super.onDestroy();
    }

    private void handleIntent(Intent intent){
        Log.d("SERVICE NEXT DAY", "start handle intent");
        SharedPreferences s = getSharedPreferences("alarm", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = s.edit();
        editor.putInt("current_question_number", 0);
        editor.commit();
        // check if time is between this and that
//        int from = 1842;
//        int to = 1847;
//        Date date = new Date();
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);
//        int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
//        boolean isBetween = to > from && t >= from && t <= to || to < from && (t >= from || t <= to);
//        Log.d("SERVICE NEXT DAY", "It is between =" + isBetween);
        boolean isBetween = true;
        if(isBetween){
            // call the AsyncTask to go to Next day
            new GoToNextDay().execute();
        }
        AlarmReceiver.completeWakefulIntent(intent);
    }

}
