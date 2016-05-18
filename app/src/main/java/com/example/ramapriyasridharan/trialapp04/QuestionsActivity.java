package com.example.ramapriyasridharan.trialapp04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramapriyasridharan.collections.ProfilingSensorsClass;
import com.example.ramapriyasridharan.collections.UserInformationClass;
import com.example.ramapriyasridharan.collections.UserResponseClass;

import com.example.ramapriyasridharan.helpers.AddDouble;
import com.example.ramapriyasridharan.helpers.ConvertStringToInt;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;

import java.sql.Timestamp;

public class QuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // if first time default values
        SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
        Double current_privacy = AddDouble.getDouble(settings,"current_privacy",100);
        int current_day = settings.getInt("current_day", 1);

        Log.d("question"," current_credit = "+current_credit);
        Log.d("question"," current_privacy = "+current_privacy);
        Log.d("question"," current_day = "+current_day);

        TextView tv_credit = (TextView) findViewById(R.id.tv_this_round_credit);
        TextView tv_privacy = (TextView) findViewById(R.id.tv_this_round_privacy_entry);
        TextView tv_user_id = (TextView) findViewById(R.id.tv_user_id_entry);
        TextView tv_day_no = (TextView) findViewById(R.id.tv_day_number_entry);
        TextView tv_questions = (TextView) findViewById(R.id.tv_question_window_entry);
        UserInstanceClass user_instance = new UserInstanceClass();

        //if(current_day == 1){
            // do not show privacy and credit textview
            //tv_credit.setVisibility(View.INVISIBLE);
            //tv_privacy.setVisibility(View.INVISIBLE);
        //}

        tv_user_id.setText(user_instance.getmKinveyClient().user().getId());
        tv_day_no.setText(String.valueOf(current_day));
        tv_privacy.setText(String.valueOf(current_privacy));
        tv_credit.setText(String.valueOf(current_credit));

        //core();


    }

    public void core(){

        Button submit;
        submit = (Button) findViewById(R.id.button_bidding_submit);
        final Spinner spinner_privacy = (Spinner) findViewById(R.id.spinner_privacy_choice_entry);

        // get the question


        // Once submit is hit, check state of each option
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                java.util.Date date = new java.util.Date();
                String time = new Timestamp(date.getTime()).toString();
                UserInstanceClass user_instance = new UserInstanceClass();
                String level = spinner_privacy.getSelectedItem().toString();

                UserResponseClass ur = new UserResponseClass();
                ur.setUser_id(user_instance.getmKinveyClient().user().getId());
                ur.setLevel(ConvertStringToInt.categorizingStringToIntBid(level, v.getContext()));
                ur.setTimestamp(time);


                AsyncAppData<UserResponseClass> myui = user_instance.getmKinveyClient().appData("UserResponse", UserResponseClass.class);

                myui.save(ur, new KinveyClientCallback<UserResponseClass>() {

                    @Override
                    public void onSuccess(UserResponseClass userInformationClass) {

                        Toast.makeText(QuestionsActivity.this, "Data successfully received", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                        Toast.makeText(QuestionsActivity.this, "Data not sent error", Toast.LENGTH_SHORT).show();
                        Log.i("ERROR sending to kinvey", "ERROR");
                    }
                });

                // place intent inside button click function

                //Intent intent = new Intent(v.getContext(), ProfilingDataCollectorsActivity.class);
                //startActivity(intent);

            }
        });



    }

}
