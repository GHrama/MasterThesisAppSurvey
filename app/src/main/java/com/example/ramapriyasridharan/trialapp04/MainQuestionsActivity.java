package com.example.ramapriyasridharan.trialapp04;

import android.app.Activity;
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

import com.example.ramapriyasridharan.StoreValues.Contexts;
import com.example.ramapriyasridharan.StoreValues.DataCollectors;
import com.example.ramapriyasridharan.StoreValues.FetchByIdQuestionsTableStore;
import com.example.ramapriyasridharan.StoreValues.Questions;
import com.example.ramapriyasridharan.StoreValues.Sensors;
import com.example.ramapriyasridharan.collections.ProfilingSensorsClass;
import com.example.ramapriyasridharan.collections.UserInformationClass;
import com.example.ramapriyasridharan.collections.UserResponseClass;

import com.example.ramapriyasridharan.helpers.AddDouble;
import com.example.ramapriyasridharan.helpers.ConvertStringToInt;
import com.example.ramapriyasridharan.helpers.Cost;
import com.example.ramapriyasridharan.helpers.Privacy;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;
import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;

import java.sql.Timestamp;

public class MainQuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("main question", "created MAIN questions activity");
        setContentView(R.layout.activity_main_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    protected void onStart(){
        super.onStart();
        // if first time default values
        Log.d("main question", "started MAIN questions activity");
        SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
        Double current_privacy = AddDouble.getDouble(settings,"current_privacy",100);
        Integer current_question_number = settings.getInt("current_question_number",-1);
        int current_day = settings.getInt("current_day", 2);

        Log.d("main question"," current_credit = "+current_credit);
        Log.d("main question"," current_privacy = "+current_privacy);
        Log.d("main question"," current_day = "+current_day);
        Log.d("main question"," current_question_number = "+current_question_number);

        TextView tv_credit = (TextView) findViewById(R.id.tv_this_round_credit_1);
        TextView tv_privacy = (TextView) findViewById(R.id.tv_this_round_privacy_entry_1);
        TextView tv_user_id = (TextView) findViewById(R.id.tv_user_id_entry_1);
        TextView tv_day_no = (TextView) findViewById(R.id.tv_day_number_entry_1);
        TextView tv_questions = (TextView) findViewById(R.id.tv_question_window_entry_1);

        UserInstanceClass user_instance = new UserInstanceClass();
        tv_user_id.setText(user_instance.getmKinveyClient().user().getId());
        tv_day_no.setText(String.valueOf(current_day));
        tv_privacy.setText(String.valueOf(current_privacy));
        tv_credit.setText(String.valueOf(current_credit));
        core1();
    }




    // N DAY SURVEY
    public void core1(){
        //EXCEPT DAY_NO ALL 0 INDEXED
        // Setting textviews with updated values from sharedpreferences
        final SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        final Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
        Double current_privacy = AddDouble.getDouble(settings,"current_privacy",100);
        int current_question_number = settings.getInt("current_question_number", -1);
        int current_day = settings.getInt("current_day", 2);

        // if questions over to go next activity
//        if (current_question_number == 10) { // 0 based index for questions
//            //push to points table
//            StoreDbHelper q = new StoreDbHelper(this);
//            q.insertPointsTable(current_day,current_credit,current_privacy);
//            // reset all these parameters
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putInt("current_day", 2);
//            editor = AddDouble.putDouble(editor,"current_privacy",100);
//            editor = AddDouble.putDouble(editor,"current_credit",0);
//            editor = AddDouble.putDouble(editor,"current_question_number",-1);
//            editor.commit();
//            Intent intent = new Intent(this, PauseActivity.class);
//            startActivity(intent);
//        }

        TextView tv_credit = (TextView) findViewById(R.id.tv_this_round_credit_1);
        TextView tv_privacy = (TextView) findViewById(R.id.tv_this_round_privacy_entry_1);
        TextView tv_user_id = (TextView) findViewById(R.id.tv_user_id_entry_1);
        TextView tv_day_no = (TextView) findViewById(R.id.tv_day_number_entry_1);

        //update with new scores from previous round in preferences file
        UserInstanceClass user_instance = new UserInstanceClass();
        tv_user_id.setText(user_instance.getmKinveyClient().user().getId());
        tv_day_no.setText(String.valueOf(current_day));
        tv_privacy.setText(String.valueOf(current_privacy));
        tv_credit.setText(String.valueOf(current_credit));

        Log.d("core1 main","entered method");
        Button submit;
        submit = (Button) findViewById(R.id.button_bid_1);
        final Spinner spinner_privacy = (Spinner) findViewById(R.id.spinner_privacy_choice_entry_1);

        // update question number
        Log.d("question","current_question_number = "+current_question_number);
        // go to next question
        if(current_question_number == 63){
            current_question_number = 0; //reset to first question
        }
        else{
            current_question_number ++; // go to next question
        }

        Log.d("main question","current_question_number = "+current_question_number);
        SharedPreferences.Editor editor = settings.edit();
        // update question number to current
        editor.putInt("current_question_number",current_question_number);
        editor.commit();


        // Database helper
        StoreDbHelper db = new StoreDbHelper(this);
        final FetchByIdQuestionsTableStore temp = db.fetchByIdQuestionTable(current_question_number);
        TextView tv_questions = (TextView) findViewById(R.id.tv_question_window_entry);
        Log.d("question","sensor id = "+temp.s);
        Log.d("question", "dc id = " + temp.d);
        Log.d("question", "context id = " + temp.c);
        String q = Questions.returnQuestion(Sensors.sensor_list[temp.s], DataCollectors.dc_list[temp.d], Contexts.contexts_list[temp.c]);
        tv_questions.setText(q);
        Log.d("question", " q = " + q);
        final int temp_q_no = current_question_number;
        // if button clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("main question", "entered button click");
                java.util.Date date = new java.util.Date();
                String time = new Timestamp(date.getTime()).toString();
                UserInstanceClass user_instance = new UserInstanceClass();
                String level = spinner_privacy.getSelectedItem().toString();

                UserResponseClass ur = new UserResponseClass();
                ur.setUser_id(user_instance.getmKinveyClient().user().getId());
                ur.setLevel(ConvertStringToInt.categorizingStringToIntBid(level, v.getContext()));
                ur.setTimestamp(time);
                ur.setDay_no(settings.getInt("current_day", 1));
                ur.setSensors(temp.s);
                ur.setDcs(temp.d);
                ur.setContexts(temp.c);
                ur.setImprove(-1); //first round no improvement choice
                StoreDbHelper db = new StoreDbHelper(v.getContext());
                Log.d("main day", "day no =" + ur.getDay_no());
                db.replaceStoreAnswers(temp_q_no, ur.getLevel(), ur.getCredit(), ur.getDay_no());
                // modify this dynamically
                ur.setCredit_gain(Cost.returnReward(temp.cost, ur.getLevel()));
                ur.setCredit(current_credit + ur.getCredit_gain());
                ur.setPrivacy_percentage(Privacy.returnPrivacyPercentage(ur.getDay_no(), v.getContext()));
                // Update preferences file
                SharedPreferences.Editor editor = settings.edit();
                editor = AddDouble.putDouble(editor, "current_credit", ur.getCredit());
                editor = AddDouble.putDouble(editor, "current_privacy", ur.getPrivacy_percentage());
                editor.commit();

                // if last question of day 1, go to pause activity
                // pause till the next day



                /*AsyncAppData<UserResponseClass> myui = user_instance.getmKinveyClient().appData("UserResponse", UserResponseClass.class);

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
                });*/

                Log.d("questions", "before call again inside");
                core1();


            }
        });

        //Log.d("questions","before call again outside");
        //core1();



    }

    public void core(){

        Button submit;
        submit = (Button) findViewById(R.id.button_bid);
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
