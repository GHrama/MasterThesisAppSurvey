package com.example.ramapriyasridharan.trialapp04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ramapriyasridharan.StoreValues.QuestionStore;
import com.example.ramapriyasridharan.StoreValues.Questions;
import com.example.ramapriyasridharan.StoreValues.Settings;
import com.example.ramapriyasridharan.collections.UserResponseClass;

import com.example.ramapriyasridharan.helpers.AddDouble;
import com.example.ramapriyasridharan.helpers.ConvertStringToInt;
import com.example.ramapriyasridharan.helpers.Cost;
import com.example.ramapriyasridharan.helpers.DatabaseInstance;
import com.example.ramapriyasridharan.helpers.Privacy;
import com.example.ramapriyasridharan.helpers.Round;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;

import java.sql.Timestamp;

public class QuestionsActivity extends AppCompatActivity {

    long last_clicked = 0;
    StoreDbHelper db = null;
    QuestionStore qs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = DatabaseInstance.db;
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.d("question", "created questions activity");
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    protected void onStart(){

        super.onStart();
        // if first time default values
        Log.d("question", "started questions activity");
        SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
        Double current_privacy = AddDouble.getDouble(settings,"current_privacy",0);
        Integer current_question_number = settings.getInt("current_question_number",0);
        int current_day = settings.getInt("current_day", 1);

        Log.d("question"," current_credit = "+current_credit);
        Log.d("question"," current_privacy = "+current_privacy);
        Log.d("question"," current_day = "+current_day);
        Log.d("question"," current_question_number = "+current_question_number);

        //TextView tv_credit = (TextView) findViewById(R.id.tv_this_round_credit);
        //TextView tv_privacy = (TextView) findViewById(R.id.tv_this_round_privacy_entry);
        TextView tv_user_id = (TextView) findViewById(R.id.tv_user_id_entry);
        TextView tv_day_no = (TextView) findViewById(R.id.tv_day_number_entry);
        TextView tv_questions = (TextView) findViewById(R.id.tv_question_window_entry);
        UserInstanceClass user_instance = new UserInstanceClass();
        //tv_user_id.setText(user_instance.getmKinveyClient().user().getId());
        tv_day_no.setText(String.valueOf(current_day));
        // first day no seeing the pirvacy or credit
        //tv_credit.setVisibility(View.INVISIBLE);
        //tv_privacy.setVisibility(View.INVISIBLE);
        tv_user_id.setText(user_instance.getmKinveyClient().user().getId());


        tv_day_no.setText(String.valueOf(current_day));
        //tv_privacy.setText(String.valueOf(Round.round(current_privacy, 2)));
        //tv_credit.setText(String.valueOf(Round.round(current_credit, 2)));

        qs = Questions.getQuestion(db,current_question_number);
        tv_questions.setText(qs.q);
        Log.d("question", " q = " + qs.q);

        if(current_day == 1){
            Log.d("question", " core1 current day 1 entered if");
            // do not show privacy and credit textview
            Log.d("core1", "enter method");
            core1();
        }

    }

    // FIRST DAY SURVEY
    public void core1(){

        final RadioGroup radio_group = (RadioGroup) findViewById(R.id.finalselection);
        radio_group.clearCheck(); //clear options

        // call notification
        //EXCEPT DAY NO ALL 0 INDEXED
        // Setting textviews with updated values from sharedpreferences
        final SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        //final Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
        //final Double current_privacy = AddDouble.getDouble(settings, "current_privacy", 0);
        final int current_day = settings.getInt("current_day", 1);

        final ImageButton submit;
        submit = (ImageButton) findViewById(R.id.button_bid);
        submit.setVisibility(View.INVISIBLE);

        // if any rafio button clicked, make submit button appear
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radio_group, int checkedId) {
                submit.setVisibility(View.VISIBLE);
            }
        });

        // if button clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - last_clicked < 1000) {
                    return;
                }
                last_clicked = SystemClock.elapsedRealtime();
                Log.d("question", "entered button click");

                java.util.Date date = new java.util.Date();
                String time = new Timestamp(date.getTime()).toString();
                UserInstanceClass user_instance = new UserInstanceClass();

                int radioButtonID = radio_group.getCheckedRadioButtonId();
                View radioButton = radio_group.findViewById(radioButtonID);
                int idx = radio_group.indexOfChild(radioButton);
                if(idx < 0 || idx > 4){
                    return;
                }
                idx++; //make 1-indexed
                Log.d("question", "index of selected child" + idx);

                // Set object value
                UserResponseClass ur = new UserResponseClass();
                ur.setUser_id(user_instance.getmKinveyClient().user().getId());
                ur.setLevel(idx);//1-5

                ur.setTimestamp(time);

                ur.setDay_no(settings.getInt("current_day", 1));
                Log.d("question", "Day_no" + ur.getDay_no());

                ur.setSensors(qs.temp.s);
                ur.setDcs(qs.temp.d);
                ur.setContexts(qs.temp.c);

                Log.d("question", "sensors" + qs.temp.s);
                Log.d("question", "dc" + qs.temp.d);
                Log.d("question", "context" + qs.temp.c);

                Log.d("question", "sensors" + ur.getSensors());
                Log.d("question", "dc" + ur.getDcs());
                Log.d("question", "context" + ur.getContexts());

                ur.setImprove(-1); //first round no improvement choice

                ur.setCredit_gain(Cost.returnReward(qs.temp.cost, ur.getLevel()));

                Log.d("question", "credit_gain" + ur.getCredit_gain());

                // do async task
                new StoreAndUpdateDbUiOne().execute(ur);

                Log.d("questions", "before call again inside");
                core1();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }

    public void onDestroy(){
        super.onDestroy();
        db = null;
    }

    private class StoreAndUpdateDbUiOne extends AsyncTask<UserResponseClass, Void, UserResponseClass> {

        @Override
        protected UserResponseClass doInBackground(UserResponseClass... urs) {
            // get object
            UserResponseClass ur = urs[0];

            Log.d("main day", "day no =" + ur.getDay_no());
            Log.d("main day", "insert into which answers" + qs.q_no);

            // insert enteredt answer
            // insert what questions answered in this round
            db.replaceStoreAnswers(qs.q_no, ur.getLevel(), ur.getCredit_gain(), ur.getDay_no());

            // calculate the credit
            ur.setCredit(Cost.returnTotalCost(ur.getDay_no()));
            ur.setPrivacy_percentage(Privacy.returnPrivacyPercentage(ur.getDay_no()));

            Log.d("question", "Credit" + ur.getCredit());
            Log.d("question", "Privacy" + ur.getPrivacy_percentage());

            // store answers for kinvey
            db.insertIntoUserResponseTable(ur);

            // Update preferences file with privacy and credit
            SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor = AddDouble.putDouble(editor, "current_credit", ur.getCredit());
            editor = AddDouble.putDouble(editor, "current_privacy", ur.getPrivacy_percentage());
            editor.commit();

            // store the question_number to ask
            Integer current_question_number = settings.getInt("current_question_number", 0);
            editor.putInt("current_question_number", current_question_number + 1);
            editor.commit();

            // get question after update current question
            current_question_number = settings.getInt("current_question_number", 0);
            qs = Questions.getQuestion(db,current_question_number);
            //SendToKinvey.sendUserResponse(user_instance, "UserResponse", ur);
            return ur;
        }

        protected void onPostExecute(UserResponseClass ur){

            SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            //TextView tv_credit = (TextView) findViewById(R.id.tv_this_round_credit);
            //TextView tv_privacy = (TextView) findViewById(R.id.tv_this_round_privacy_entry);
            TextView tv_user_id = (TextView) findViewById(R.id.tv_user_id_entry);
            TextView tv_day_no = (TextView) findViewById(R.id.tv_day_number_entry);
            TextView tv_questions = (TextView) findViewById(R.id.tv_question_window_entry);

            // set UI values
            tv_user_id.setText(ur.getUser_id());
            tv_day_no.setText(String.valueOf(ur.getDay_no()));
            ImageButton button_bid = (ImageButton) findViewById(R.id.button_bid);
            button_bid.setVisibility(View.INVISIBLE);
            //tv_privacy.setText(String.valueOf(Round.round(ur.getPrivacy_percentage(),2)));
            //tv_credit.setText(String.valueOf(Round.round(ur.getCredit(), 2)));

            // if question to ask is 64 (or) 10 (represents the next question to ask)
            if(settings.getInt("current_question_number", 0) == Settings.num){
                // answered all first day questions
                // go to next activity and save all things
                db.insertPointsTable(ur.getDay_no(), ur.getCredit(), ur.getPrivacy_percentage());

                // reset all these parameters
                editor.putInt("current_question_number",0);
                editor.putInt("current_day", 2);
                editor = AddDouble.putDouble(editor,"current_privacy",0);
                editor = AddDouble.putDouble(editor,"current_credit",0);
                editor.putInt("prev_day", 1);
                editor.commit();

                // start new intent
                Intent intent = new Intent(getApplicationContext(), PauseActivity.class);
                final SharedPreferences s_logged = getSharedPreferences("logged", Context.MODE_PRIVATE);
                final SharedPreferences.Editor e = s_logged.edit();
                e.putInt("activity", 8);
                e.commit();
                startActivity(intent);
            }
            else{
                tv_questions.setText(qs.q);
                Log.d("question", " q = " + qs.q);
            }

        }


    }





}
