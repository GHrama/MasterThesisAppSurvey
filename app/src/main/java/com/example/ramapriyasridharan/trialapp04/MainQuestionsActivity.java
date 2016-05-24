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

import com.example.ramapriyasridharan.StoreValues.QuestionStore;
import com.example.ramapriyasridharan.StoreValues.WhichClicked;

import com.example.ramapriyasridharan.StoreValues.Contexts;
import com.example.ramapriyasridharan.StoreValues.DataCollectors;
import com.example.ramapriyasridharan.StoreValues.FetchByIdQuestionsTableStore;
import com.example.ramapriyasridharan.StoreValues.Questions;
import com.example.ramapriyasridharan.StoreValues.Sensors;
import com.example.ramapriyasridharan.StoreValues.WhichClicked;
import com.example.ramapriyasridharan.collections.ProfilingSensorsClass;
import com.example.ramapriyasridharan.collections.UserInformationClass;
import com.example.ramapriyasridharan.collections.UserResponseClass;

import com.example.ramapriyasridharan.helpers.AddDouble;
import com.example.ramapriyasridharan.helpers.ConvertStringToInt;
import com.example.ramapriyasridharan.helpers.Cost;
import com.example.ramapriyasridharan.helpers.Privacy;
import com.example.ramapriyasridharan.helpers.SendToKinvey;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;
import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;

import java.sql.Timestamp;

public class MainQuestionsActivity extends AppCompatActivity {

    QuestionStore global_qs = null;

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
        Integer current_question_number = settings.getInt("current_question_number",0);
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
        Button button_privacy = (Button) findViewById(R.id.button_improve_privacy);
        Button button_credit = (Button) findViewById(R.id.button_improve_credit);
        Button submit;
        submit = (Button) findViewById(R.id.button_bid_1);

        button_credit.setVisibility(View.VISIBLE);
        button_privacy.setVisibility(View.VISIBLE);
        tv_questions.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.INVISIBLE);


        UserInstanceClass user_instance = new UserInstanceClass();
        tv_user_id.setText(user_instance.getmKinveyClient().user().getId());
        tv_day_no.setText(String.valueOf(current_day));
        tv_privacy.setText(String.valueOf(current_privacy));
        tv_credit.setText(String.valueOf(current_credit));
        core1();
    }




    // N DAY SURVEY
    public void core1(){
        final int num = 10; // total number of possible questions

        //EXCEPT DAY_NO ALL 0 INDEXED
        // Setting textviews with updated values from sharedpreferences
        final SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        final Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
        Double current_privacy = AddDouble.getDouble(settings, "current_privacy", 100);
        int current_day = settings.getInt("current_day", 2);

        TextView tv_credit = (TextView) findViewById(R.id.tv_this_round_credit_1);
        TextView tv_privacy = (TextView) findViewById(R.id.tv_this_round_privacy_entry_1);
        TextView tv_user_id = (TextView) findViewById(R.id.tv_user_id_entry_1);
        TextView tv_day_no = (TextView) findViewById(R.id.tv_day_number_entry_1);

        final Button button_privacy = (Button) findViewById(R.id.button_improve_privacy);
        final Button button_credit = (Button) findViewById(R.id.button_improve_credit);
        final Button submit;
        submit = (Button) findViewById(R.id.button_bid_1);
        final Spinner spinner_privacy = (Spinner) findViewById(R.id.spinner_privacy_choice_entry_1);
        final TextView tv_questions = (TextView) findViewById(R.id.tv_question_window_entry_1);

        // update value of fields from preferences file
        UserInstanceClass user_instance = new UserInstanceClass();
        tv_user_id.setText(user_instance.getmKinveyClient().user().getId());
        tv_day_no.setText(String.valueOf(current_day));
        tv_privacy.setText(String.valueOf(current_privacy));
        tv_credit.setText(String.valueOf(current_credit));
        final WhichClicked wc = new WhichClicked();

        // choose privacy or credit
        // already set in xml
        tv_questions.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.INVISIBLE);
        spinner_privacy.setVisibility(View.INVISIBLE);
        button_credit.setVisibility(View.VISIBLE);
        button_privacy.setVisibility(View.VISIBLE);

        // click either 1 of the buttons
        // cannot click on them too soon ??

        //click on improve credit
        button_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wc.setCredit();
                QuestionStore qs = Questions.getQuestionWc(wc, v.getContext(), num);
                tv_questions.setText(qs.q);
                Log.d("main question", " q = " + qs.q);
                Log.d("main question", " q_no = " + qs.q_no);
                global_qs = qs;
                // reverse visibility of buttons
                button_credit.setVisibility(View.INVISIBLE);
                button_privacy.setVisibility(View.INVISIBLE);
                tv_questions.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                spinner_privacy.setVisibility(View.VISIBLE);
            }

        });

        // if privacy clicked
        button_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wc.setPrivacy();
                QuestionStore qs = Questions.getQuestionWc(wc, v.getContext(), num);
                tv_questions.setText(qs.q);
                Log.d("main question", " q = " + qs.q);
                Log.d("main question", " q_no = " + qs.q_no);
                global_qs = qs;
                // reverse visibility of buttons
                button_credit.setVisibility(View.INVISIBLE);
                button_privacy.setVisibility(View.INVISIBLE);
                tv_questions.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                spinner_privacy.setVisibility(View.VISIBLE);
            }

        });



        // if button clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("main question", "entered button click");
                java.util.Date date = new java.util.Date();
                String time = new Timestamp(date.getTime()).toString();
                UserInstanceClass user_instance = new UserInstanceClass();
                String level = spinner_privacy.getSelectedItem().toString();

                // Store user choices
                UserResponseClass ur = new UserResponseClass();
                ur.setUser_id(user_instance.getmKinveyClient().user().getId());
                ur.setLevel(ConvertStringToInt.categorizingStringToIntBid(level, v.getContext()));
                ur.setTimestamp(time);
                ur.setDay_no(settings.getInt("current_day", 2));
                ur.setSensors(global_qs.temp.s);
                ur.setDcs(global_qs.temp.d);
                ur.setContexts(global_qs.temp.c);
                if(wc.isCredit()){
                    ur.setImprove(2); // improve credit
                }
                else
                {
                    ur.setImprove(1); // improve privacy
                }

                //first round no improvement choice
                StoreDbHelper db = new StoreDbHelper(v.getContext());
                Log.d("main day", "day no =" + ur.getDay_no());
                db.replaceStoreAnswers(global_qs.q_no, ur.getLevel(), ur.getCredit(), ur.getDay_no());
                db.insertQuestionAnsweredTable(global_qs.q_no);
                Log.d("main day", "insert intro which answers" + global_qs.q_no);

                ur.setCredit_gain(Cost.returnReward(global_qs.temp.cost, ur.getLevel()));
                ur.setCredit(current_credit + ur.getCredit_gain());
                ur.setPrivacy_percentage(Privacy.returnPrivacyPercentage(ur.getDay_no(), v.getContext()));

                // Update preferences file with privacy and credit
                SharedPreferences.Editor editor = settings.edit();
                editor = AddDouble.putDouble(editor, "current_credit", ur.getCredit());
                editor = AddDouble.putDouble(editor, "current_privacy", ur.getPrivacy_percentage());
                editor.commit();
                global_qs = null;

                //SendToKinvey.sendUserResponse(user_instance, "UserResponse", ur);
                Log.d("main questions", "before call again inside");
                core1();

                // when midnight of current day
                // store credit & privacy levels & day_no
                // summarize the data in DB acc to level set in answers_table
                // reset which_answers table
                // send to kinvey
                // current_day ++
                // empty sensor tables in db
                // reset & set preferences


            }
        });

        //Log.d("questions","before call again outside");
        //core1();



    }




}
