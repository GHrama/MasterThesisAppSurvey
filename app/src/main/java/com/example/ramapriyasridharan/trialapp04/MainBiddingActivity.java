package com.example.ramapriyasridharan.trialapp04;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainBiddingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bidding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Ask for a question every 10 minutes
        final TextView user = (TextView) findViewById(R.id.textView_user_id_bidding);
        final TextView credit = (TextView) findViewById(R.id.textView_user_credit_bidding);
        final  TextView question = (TextView) findViewById(R.id.textView_offers_window_bidding);
        final Button button_bid = (Button) findViewById(R.id.button_send_bid_bidding);
        UserInstanceClass ui = new UserInstanceClass();
        //ui.getmKinveyClient().push().initialize(getApplication()); // register this client to get push notifications
        //ui.getmKinveyClient().push().disablePush(); //deregister push notifications

        

    }

    public void onBackPressed() {
        Intent intent = new Intent(this , Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
