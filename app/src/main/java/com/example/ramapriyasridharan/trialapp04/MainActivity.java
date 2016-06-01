package com.example.ramapriyasridharan.trialapp04;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.LinearLayout;

import com.example.ramapriyasridharan.helpers.AddDouble;
import com.example.ramapriyasridharan.helpers.GotoActivity;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

public class MainActivity extends AppCompatActivity {


    private static String TAG = MainActivity.class.getName();
    ImageButton button_bid_1;
    LinearLayout homeLayout;
    LinearLayout improveLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do not let screen switch off
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_bidding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        final SharedPreferences s_logged = getSharedPreferences("logged", Context.MODE_PRIVATE);
        final SharedPreferences.Editor e = s_logged.edit();
        int temp = s_logged.getInt("activity", 0);
        Log.d("main activity", " s_logged activity ="+temp );

        UserInstanceClass instance_user = new UserInstanceClass();
        //final Client mKinveyClient = new Client.Builder("kid_W1EFbeKyy-", "1b6f09e812114210ae4447f310b38a0a"
         //       , this.getApplicationContext()).build();

        instance_user.setmKinveyClient(new Client.Builder(this.getApplicationContext()).build());
        //final Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
        // get app properties from kinvey.properties
        //final TextView user_text;
        //user_text = (TextView) findViewById(R.id.user_id);
        button_bid_1 = (ImageButton) findViewById(R.id.button_bid_1);
        homeLayout = (LinearLayout) findViewById(R.id.homeLayout);
        improveLayout = (LinearLayout) findViewById(R.id.improveLayout);

        button_bid_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                homeLayout.setVisibility(View.GONE);
                improveLayout.setVisibility(View.VISIBLE);
            }
        });


        // check if new to app
        boolean meow = instance_user.mKinveyClient.user().isUserLoggedIn();
        Log.i("main activity", "meow = " + meow);
        int is_logged = s_logged.getInt("logged",0);
        Log.d("main activity", " s_logged logged ="+is_logged);

        // if already logged
        if(meow){
            Log.i(TAG, "(not first time): " + instance_user.mKinveyClient.user().getId());
            //user_text.setText("user id is " + instance_user.mKinveyClient.user().getId());
            int a = s_logged.getInt("activity", 2);
            Log.d(TAG, "activity going to" + a);
            Intent i = null;
            switch(a){
                case 1: i = new Intent(this, MainActivity.class);break;
                case 2: i = new Intent(this, ProfilingFeaturesActivity.class);break;
                case 4: i = new Intent(this, ProfilingSensorsActivity.class);break;
                case 5: i = new Intent(this, ProfilingDataCollectorsActivity.class);break;
                case 6: i = new Intent(this, ProfilingContextsActivity.class);break;
                case 7: i = new Intent(this, QuestionsActivity.class);break;
                case 8: i = new Intent(this, PauseActivity.class);break;
                case 9: i = new Intent(this, MainQuestionsActivity.class);break;
            }
            //startActivity(i);
        }

        // if user is logged in meow = True dont log him in again!

        if (!meow) {
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
                        e.putInt("logged",1); // means it is logged in
                        e.commit();
                        Log.i(TAG, "Logged in a new implicit user with id(first time): " + result.getId());
                    }
            });

        }


        String user_string = instance_user.mKinveyClient.user().getId();
        Log.i(TAG, "running for first time");

        // Reset preferences file when installing application for the first time
        // remove only debugging!
        SharedPreferences s = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putInt("current_question_number",0);
        editor.putInt("current_day",1);
        editor = AddDouble.putDouble(editor,"current_credit",0);
        editor = AddDouble.putDouble(editor,"current_privacy",100);
        editor.commit();

        // Create DB
        StoreDbHelper db = new StoreDbHelper(this);
        // clear content to simulate first time user
        db.removeAll();
        db.close();
        Intent intent = new Intent(this, GetUserInformation.class);
        e.putInt("activity", 2);
        e.commit();
        intent.putExtra("user_id",user_string);
        //startActivity(intent);
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

    @TargetApi(23)
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) button_bid_1.setVisibility(View.VISIBLE);
        else button_bid_1.setVisibility(View.VISIBLE);
        // Check which radio button was clicked

    }

}
