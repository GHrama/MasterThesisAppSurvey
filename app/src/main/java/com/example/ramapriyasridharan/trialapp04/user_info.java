package com.example.ramapriyasridharan.trialapp04;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.java.Query;
import com.kinvey.java.core.KinveyClientCallback;

import java.util.Arrays;

public class user_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UserInstanceClass user_instance = new UserInstanceClass();
        final TextView answer_first_name = (TextView) findViewById(R.id.textView_first_name);
        final TextView answer_sex = (TextView) findViewById(R.id.textView_sex_answer);
        final TextView answer_user_id = (TextView) findViewById(R.id.textView_user_id_answer);
        Bundle extras = getIntent().getExtras();
        final String user_id = extras.getString("user_id");

        /*



        answer_first_name.setText(extras.getString("first_name"));
        answer_sex.setText(extras.getString("sex"));
        answer_user_id.setText(extras.getString("user_id"));*/

        // query the UserInformation DB to get user information drectyl from DB
        final String[] temp = new String[2];
        // frame the query
        Query getUserInformationQuery = user_instance.getmKinveyClient().query().equals("_UserId", extras.getString("user_id"));


        AsyncAppData<UserInformationClass> myui = user_instance.getmKinveyClient().appData("InformationUsers",UserInformationClass.class);
        myui.get(getUserInformationQuery, new KinveyListCallback<UserInformationClass>() {
            @Override
            public void onSuccess(UserInformationClass[] results) {
                Log.i("SUCESS", "got info from db (UserInformation)" + results.length);
                if (results.length > 0) {
                    temp[0] = results[0].getUser_first_name();
                    temp[1] = results[0].getUser_sex();
                    Log.i("SUCESS", "information" + temp[0] + " " + temp[1]);
                    answer_first_name.setText(temp[0]); //get from DB
                    answer_sex.setText(temp[1]); // get from DB
                    answer_user_id.setText(user_id); // stored locally
                }


            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.i("ERROR","dint get info from db (UserInformation)" );

            }
        });

        addListenerOnButton();







    }

    public void addListenerOnButton(){
        Button button = (Button) findViewById(R.id.button_bidding);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on click send user_id to new intent
                Bundle extra = getIntent().getExtras();
                Intent intent = new Intent(v.getContext(), MainBiddingActivity.class);
                intent.putExtra("user_id",extra.getString("user_id"));
                startActivity(intent);





            }
        });
    }

}
