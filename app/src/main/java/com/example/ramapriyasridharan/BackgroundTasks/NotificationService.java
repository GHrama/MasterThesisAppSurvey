package com.example.ramapriyasridharan.BackgroundTasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.ramapriyasridharan.trialapp04.MainQuestionsActivity;
import com.example.ramapriyasridharan.trialapp04.QuestionsActivity;
import com.example.ramapriyasridharan.trialapp04.R;

/**
 * Created by ramapriyasridharan on 25.05.16.
 * Send notification every x minutes?
 */
public class NotificationService extends Service {

//    private PowerManager.WakeLock mWakeLock;

    @Nullable
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("SERVICE NOTIFICATION","started");
        // START YOUR TASKS
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("SERVICE NOTIFICATION", "destroyed");
        // STOP YOUR TASKS
        super.onDestroy();

//        mWakeLock.release();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    private void handleIntent(Intent intent){
        Log.d("SERVICE NOTIFICATION","start handle intent");
        // obtain the wake lock
//        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
//        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getSimpleName());
//        mWakeLock.acquire();
        Log.d("SERVICE NOTIFICATION", "calling async task");
        new NotificationAsyncTask().execute();

        // check the global background data setting
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        if (!cm.getBackgroundDataSetting()) {
//            stopSelf();
//            return;
//        }

    }

    // inner async Task class
    public class NotificationAsyncTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d("SERVICE NOTIFICATION","entered do in backgorund");
            // create notification
            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.common_ic_googleplayservices)
                            .setContentTitle("Forgot us? :)")
                            .setContentText("Would you like to further improve your privacy?")
                            .setPriority(Notification.PRIORITY_MAX);

            // get intent of main question activity
            Intent go_to_bid = new Intent(getApplicationContext(), MainQuestionsActivity.class);
            PendingIntent on_click_bid =
                    PendingIntent.getActivity(
                            getApplicationContext(),
                            0,
                            go_to_bid,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(on_click_bid);

            // issue notitfication
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(0, mBuilder.build());

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("SERVICE NOTIFICATION","on Postexecute");
            // handle your data
            stopSelf();
        }
    }
}
