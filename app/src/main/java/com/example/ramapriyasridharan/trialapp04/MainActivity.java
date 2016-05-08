package com.example.ramapriyasridharan.trialapp04;

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

public class MainActivity extends AppCompatActivity {


    private static String TAG = MainActivity.class.getName();
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences settings = getSharedPreferences(MY_PREFS_NAME,0);


        UserInstanceClass instance_user = new UserInstanceClass();
        //final Client mKinveyClient = new Client.Builder("kid_W1EFbeKyy-", "1b6f09e812114210ae4447f310b38a0a"
         //       , this.getApplicationContext()).build();

        instance_user.setmKinveyClient(new Client.Builder(this.getApplicationContext()).build());
        //final Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        // get app properties from kinvey.properties
        final TextView user_text;
        user_text = (TextView) findViewById(R.id.user_id);


        boolean meow = instance_user.mKinveyClient.user().isUserLoggedIn();

        // if user is logged in meow = True dont log him in again!
        {
            if (!meow) {
                instance_user.mKinveyClient.user().login(new KinveyUserCallback() {
                    @Override
                    public void onFailure(Throwable error) {
                        Log.i(TAG, "Fail");
                        user_text.setText("User not identified !!");
                        Log.i(TAG, "" + error);
                    }

                    @Override
                    public void onSuccess(User result) {
                        user_text.setText("user id is " + result.getId());

                        Log.i(TAG, "Logged in a new implicit user with id(first time): " + result.getId());

                    }
                });
            } else {

                Log.i(TAG, "Logged in a new implicit user with id(not first time): " + instance_user.mKinveyClient.user().getId());
                user_text.setText("user id is " + instance_user.mKinveyClient.user().getId());
            }
        }
        /*boolean k = settings.getBoolean("my_first_time",true);
        user_text.setText(""+k);
        if(settings.getBoolean("my_first_time",true)){
            //app launched for the first time
            //to check its value
            Log.i(TAG,"running for first time");
            Intent intent = new Intent(this, RegisterUserActivity.class);
            settings.edit().putBoolean("my_first_time",true).commit();
            startActivity(intent);
        }*/
        String user_string = instance_user.mKinveyClient.user().getId();
        Log.i(TAG,"running for first time");
        Intent intent = new Intent(this, RegisterUserActivity.class);
        intent.putExtra("user_id",user_string);
        startActivity(intent);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
