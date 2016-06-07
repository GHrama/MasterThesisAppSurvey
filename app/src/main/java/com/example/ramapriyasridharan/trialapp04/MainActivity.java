package com.example.ramapriyasridharan.trialapp04;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.ramapriyasridharan.BackgroundTasks.SensorIntentService;
import com.example.ramapriyasridharan.BackgroundTasks.WeightCostMatrixService;
import com.example.ramapriyasridharan.BroadcastReceivers.AlarmReceiver;
import com.example.ramapriyasridharan.JobService.UserResponseSendService;
import com.example.ramapriyasridharan.SensorHelpers.SensorCollectStatus;
import com.example.ramapriyasridharan.SensorHelpers.SensorConfiguration;
import com.example.ramapriyasridharan.helpers.AddDouble;
import com.example.ramapriyasridharan.helpers.DatabaseInstance;
import com.example.ramapriyasridharan.helpers.GotoActivity;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static MainActivity main_activity;

    public static MainActivity getInstance(){
        return main_activity;
    }

    static public AlarmManager alarm_notification;
    static public AlarmManager alarm_next_day_change;

    static public Intent intent_notification;
    static public Intent intent_next_day_change;

    static public PendingIntent pi_notification;
    static public PendingIntent pi_next_day_change;

    public static String NOTIFICATION_ACTION = "com.example.ramapriyasridharan.alarm.notification";
    public static String NEXT_DAY_CHANGE_ACTION = "com.example.ramapriyasridharan.alarm.next.day.change";

    private static final int GPS_REQUEST_CODE = 1000;

    DatabaseInstance d = new DatabaseInstance(this);
    StoreDbHelper db = DatabaseInstance.db;

    UserInstanceClass instance_user = null;

    private static String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_activity = this;

        // GPS stuff todo: make it better
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int errorCheck = api.isGooglePlayServicesAvailable(this);
        if(errorCheck == ConnectionResult.SUCCESS) {
            //google play services available, hooray
        } else if(api.isUserResolvableError(errorCheck)) {
            //GPS_REQUEST_CODE = 1000, and is used in onActivityResult
            api.showErrorDialogFragment(this, errorCheck, GPS_REQUEST_CODE);
            //stop our activity initialization code
            return;
        } else {
            //GPS not available, user cannot resolve this error
            //todo: somehow inform user or fallback to different method
            //stop our activity initialization code
            return;
        }

        //get DB instance

        // do not let screen switch off
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        instance_user = new UserInstanceClass();
        instance_user.setmKinveyClient(new Client.Builder(this.getApplicationContext()).build());

        // check if new to app
        boolean meow = instance_user.mKinveyClient.user().isUserLoggedIn();

        //UserResponseSendService.scheduleRepeat(this);
        // set alarms
        //setApproxNotificationAlarm();
        //setExactNextDayChangeAlarm();
        //startSensorService();
        if(meow){
            gotoLastState();
        }
        else if (!meow) {
            firstLogin();
        }
    }



    private void firstLogin(){
        final SharedPreferences s_logged = getSharedPreferences("logged", Context.MODE_PRIVATE);
        final SharedPreferences.Editor e = s_logged.edit();

        instance_user.mKinveyClient.user().login(new KinveyUserCallback() {
            @Override
            public void onFailure(Throwable error) {
                Log.i(TAG, "Fail");
                //user_text.setText("User not identified !!");
                Log.i(TAG, "" + error);
            }

            @Override
            public void onSuccess(User result) {
                //user_text.setText("user id is " + result.getId());
                Log.i(TAG, "Logged in a new implicit user with id(first time): " + result.getId());
            }
        });
        String user_string = instance_user.mKinveyClient.user().getId();
        Log.i(TAG, "running for first time");

        // Reset preferences file when installing application for the first time
        // remove only debugging!
        SharedPreferences s = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putInt("current_question_number", 0);
        editor.putInt("current_day", 1);
        editor = AddDouble.putDouble(editor,"current_credit",0);
        editor = AddDouble.putDouble(editor,"current_privacy",100);
        editor.commit();

        // clear content to simulate first time user
        db.removeAll();
        Intent intent = new Intent(this, GetUserInformation.class);
        e.putInt("activity", 2);
        e.commit();
        intent.putExtra("user_id", user_string);
        startActivity(intent);
    }


    private void gotoLastState(){

        final SharedPreferences s_logged = getSharedPreferences("logged", Context.MODE_PRIVATE);
        final SharedPreferences.Editor e = s_logged.edit();
        Log.i(TAG, "(not first time): " + instance_user.mKinveyClient.user().getId());
        //user_text.setText("user id is " + instance_user.mKinveyClient.user().getId());
        int a = s_logged.getInt("activity", 2);
        Log.d(TAG, "activity going to :" + a);
        Intent i = null;
        switch(a){
            case 1: Log.d(TAG, "activity going to :" + a);i = new Intent(getApplicationContext(), MainActivity.class);break;
            case 2: Log.d(TAG, "activity going to :" + a);i = new Intent(getApplicationContext(), ProfilingFeaturesActivity.class);break;
            case 4: Log.d(TAG, "activity going to :" + a);i = new Intent(getApplicationContext(), ProfilingSensorsActivity.class);break;
            case 5: Log.d(TAG, "activity going to :" + a);i = new Intent(getApplicationContext(), ProfilingDataCollectorsActivity.class);break;
            case 6: Log.d(TAG, "activity going to :" + a);i = new Intent(getApplicationContext(), ProfilingContextsActivity.class);break;
            case 7: Log.d(TAG, "activity going to :" + a);i = new Intent(getApplicationContext(), QuestionsActivity.class);break;
            case 8: Log.d(TAG, "activity going to :" + a);i = new Intent(getApplicationContext(), PauseActivity.class);break;
            case 9: Log.d(TAG, "activity going to :" + a);i = new Intent(getApplicationContext(), MainQuestionsActivity.class);break;
        }
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    boolean serviceRunning = false;

    public void startSensorService() {
            SensorIntentService.startService(this);
            serviceRunning = true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setApproxNotificationAlarm(){
        Log.d("ALARM", "create notification alarm");
        intent_notification = new Intent(this, AlarmReceiver.class);
        intent_notification.setAction(NOTIFICATION_ACTION);
        pi_notification = PendingIntent.getBroadcast(this, 111, intent_notification, 0);
        alarm_notification = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_notification.set(alarm_notification.RTC_WAKEUP, System.currentTimeMillis() + 60 * 1000, pi_notification);
    }


    // Set Next day change alarm
    public void setExactNextDayChangeAlarm(){
        // At a time
        Log.d("ALARM", "create next day alarm");
        intent_next_day_change = new Intent(this, AlarmReceiver.class);
        intent_next_day_change.setAction(NEXT_DAY_CHANGE_ACTION);
        pi_next_day_change = PendingIntent.getBroadcast(this, 1003, intent_next_day_change,0);
        alarm_next_day_change = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 16); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, 55);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarm_next_day_change.setExact(alarm_next_day_change.RTC_WAKEUP,calendar.getTimeInMillis(),pi_next_day_change);
        } else {
            alarm_next_day_change.set(alarm_next_day_change.RTC_WAKEUP, calendar.getTimeInMillis(), pi_next_day_change);
        }

    }


}
