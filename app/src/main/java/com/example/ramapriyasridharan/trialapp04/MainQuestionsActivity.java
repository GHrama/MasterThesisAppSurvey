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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ramapriyasridharan.StoreValues.QuestionStore;
import com.example.ramapriyasridharan.StoreValues.Settings;
import com.example.ramapriyasridharan.StoreValues.WhichClicked;

import com.example.ramapriyasridharan.StoreValues.Questions;
import com.example.ramapriyasridharan.collections.UserResponseClass;

import com.example.ramapriyasridharan.helpers.AddDouble;
import com.example.ramapriyasridharan.helpers.Answer;
import com.example.ramapriyasridharan.helpers.Cost;
import com.example.ramapriyasridharan.helpers.DatabaseInstance;
import com.example.ramapriyasridharan.helpers.Privacy;
import com.example.ramapriyasridharan.helpers.Round;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class MainQuestionsActivity extends AppCompatActivity {

    static MainQuestionsActivity main_question;

    QuestionStore global_qs = null;
    public StoreDbHelper db = null;
    public long last_clicked = 0;

    public int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        main_question = this;
        db = DatabaseInstance.db;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.d("main question", "created MAIN questions activity");
    }

    public static MainQuestionsActivity getInstance(){
        return main_question;
    }

    // Set notification alarm

    protected void onStart() {
        super.onStart();
        // if first time default values
        Log.d("main question", "started MAIN questions activity");

        //get file values
        SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
        Double current_privacy = AddDouble.getDouble(settings, "current_privacy", 0);
        Integer current_question_number = settings.getInt("current_question_number", 0);
        int current_day = settings.getInt("current_day", 2);

        Log.d("main question", " current_credit = " + current_credit);
        Log.d("main question", " current_privacy = " + current_privacy);
        Log.d("main question", " current_day = " + current_day);
        Log.d("main question", " current_question_number = " + current_question_number);

        //UI ids
        TextView tv_credit = (TextView) findViewById(R.id.tv_this_round_credit_1);
        TextView tv_privacy = (TextView) findViewById(R.id.tv_this_round_privacy_entry_1);
        TextView tv_user_id = (TextView) findViewById(R.id.tv_user_id_entry_1);
        TextView tv_day_no = (TextView) findViewById(R.id.tv_day_number_entry_1);

        //set values to ids ui
        UserInstanceClass user_instance = new UserInstanceClass();
        tv_user_id.setText(user_instance.getmKinveyClient().user().getId());
        tv_day_no.setText(String.valueOf(current_day));
        tv_privacy.setText(String.valueOf(Round.round(current_privacy, 2)));
        tv_credit.setText(String.valueOf(Round.round(current_credit, 2)));

        // call main function
        core1();
    }

    // N DAY SURVEY
    public void core1() {

        final ImageButton submit;
        submit = (ImageButton) findViewById(R.id.button_bid_1);
        submit.setVisibility(View.INVISIBLE);
        // total number of possible questions

        final RadioGroup radio_group = (RadioGroup) findViewById(R.id.finalselection);
        radio_group.clearCheck(); //clear options

        // if any rafio button clicked, make submit button appear
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radio_group, int checkedId) {
                submit.setVisibility(View.VISIBLE);
            }
        });

        // Setting textviews with updated values from sharedpreferences
        final SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        final Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
        Double current_privacy = AddDouble.getDouble(settings, "current_privacy", 0);
        int current_day = settings.getInt("current_day", 2);
        Log.d("main question", " current_credit = " + current_credit);
        Log.d("main question", " current_privacy = " + current_privacy);
        Log.d("main question", " current_day = " + current_day);


        final ImageButton button_privacy = (ImageButton) findViewById(R.id.button_improve_privacy);
        final ImageButton button_credit = (ImageButton) findViewById(R.id.button_improve_credit);
        final WhichClicked wc = new WhichClicked();

        //click on improve credit
        button_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - last_clicked < 1000) {
                    return;
                }
                last_clicked = SystemClock.elapsedRealtime();
                wc.setCredit();
                new GetQuestionAsyncTask().execute(wc);
                // reverse visibility of buttons
            }

        });

        // if privacy clicked
        button_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - last_clicked < 1000) {
                    return;
                }
                last_clicked = SystemClock.elapsedRealtime();
                wc.setPrivacy();
                new GetQuestionAsyncTask().execute(wc);
                // reverse visibility of buttons
            }

        });

        // if submit button clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - last_clicked < 1000) {
                    return;
                }

                last_clicked = SystemClock.elapsedRealtime();

                // get checked option
                int radioButtonID = radio_group.getCheckedRadioButtonId();
                View radioButton = radio_group.findViewById(radioButtonID);
                int idx = radio_group.indexOfChild(radioButton);
                if(idx < 0 || idx > 4){
                    return;
                }
                idx++; //make 1-indexed
                Log.d("question", "index of selected child" + idx);

                Log.d("main question", "entered button click");
                Date date = new Date();
                String time = new Timestamp(date.getTime()).toString();
                UserInstanceClass user_instance = new UserInstanceClass();

                // Store user choices
                UserResponseClass ur = new UserResponseClass();
                ur.setUser_id(user_instance.getmKinveyClient().user().getId());
                ur.setLevel(idx);
                ur.setTimestamp(time);
                ur.setDay_no(settings.getInt("current_day", 2));
                ur.setSensors(global_qs.temp.s);
                ur.setDcs(global_qs.temp.d);
                ur.setContexts(global_qs.temp.c);
                ur.setCredit_gain(Cost.returnReward(global_qs.temp.cost, ur.getLevel()));

                Log.d("main question", "sensors" + global_qs.temp.s);
                Log.d("main question", "dc" + global_qs.temp.d);
                Log.d("main question", "context" + global_qs.temp.c);

                if (wc.isCredit()) {
                    ur.setImprove(2); // improve credit
                } else {
                    ur.setImprove(1); // improve privacy
                }

                //execute async Tasks in a separate thread
                new StoreAndUpdateDbUiN().execute(ur);
                core1();
            }
        });
    }

    @Override
    public void onBackPressed() {
        final ImageButton submit;
        submit = (ImageButton) findViewById(R.id.button_bid_1);
        submit.setVisibility(View.INVISIBLE);
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    public void onDestroy() {
        final ImageButton submit;
        submit = (ImageButton) findViewById(R.id.button_bid_1);
        submit.setVisibility(View.INVISIBLE);
        super.onDestroy();
        db = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        final ImageButton submit;
        submit = (ImageButton) findViewById(R.id.button_bid_1);
        submit.setVisibility(View.INVISIBLE);
    }

    // store answers in db
    // upate privacy,credit ui
    private class StoreAndUpdateDbUiN extends AsyncTask<UserResponseClass, Void, UserResponseClass> {

        @Override
        protected UserResponseClass doInBackground(UserResponseClass... urs) {
            // get object
            UserResponseClass ur = urs[0];

            Log.d("main day", "day no =" + ur.getDay_no());
            Log.d("main day", "insert intro which answers" + global_qs.q_no);

            // insert enteredt answer
            // insert what questions answered in this round
            db.replaceStoreAnswers(global_qs.q_no, ur.getLevel(), ur.getCredit_gain(), ur.getDay_no());
            db.insertQuestionAnsweredTable(global_qs.q_no);

            count ++;
            Log.d("main questions", "count =" + count);
            // calculate the credit
            ur.setCredit(Cost.returnTotalCost(ur.getDay_no()));
            ur.setPrivacy_percentage(Privacy.returnPrivacyPercentage(ur.getDay_no()));

            // store answers for kinvey
            db.insertIntoUserResponseTable(ur);

            //log result
            Log.d("main questions", "total cost" + ur.getCredit());
            Log.d("main questions", "privacy %tage" + ur.getPrivacy_percentage());

            // Update preferences file with privacy and credit
            SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor = AddDouble.putDouble(editor, "current_credit", ur.getCredit());
            editor = AddDouble.putDouble(editor, "current_privacy", ur.getPrivacy_percentage());
            editor.commit();
            //SendToKinvey.sendUserResponse(user_instance, "UserResponse", ur);
            return ur;
        }

        protected void onPostExecute(UserResponseClass ur) {
            // get ids
            TextView tv_credit = (TextView) findViewById(R.id.tv_this_round_credit_1);
            TextView tv_privacy = (TextView) findViewById(R.id.tv_this_round_privacy_entry_1);
            TextView tv_user_id = (TextView) findViewById(R.id.tv_user_id_entry_1);
            TextView tv_day_no = (TextView) findViewById(R.id.tv_day_number_entry_1);

            // set UI values
            tv_user_id.setText(ur.getUser_id());
            tv_day_no.setText(String.valueOf(ur.getDay_no()));
            tv_privacy.setText(String.valueOf(Round.round(ur.getPrivacy_percentage(),2)));
            tv_credit.setText(String.valueOf(Round.round(ur.getCredit(),2)));

            //remove privacy buttons
            LinearLayout improveLayout = (LinearLayout) findViewById(R.id.improveLayout);
            improveLayout.setVisibility(View.VISIBLE);

            //bring default layout
            LinearLayout homeLayout = (LinearLayout) findViewById(R.id.homeLayout);
            homeLayout.setVisibility(View.GONE);

//            if (count >= 10) {
//                new GoToNextDay().execute(); // Use alarm manager
//            }
        }
    }

    // Fetch question and update UI
    private class GetQuestionAsyncTask extends AsyncTask<WhichClicked, Void, ArrayList<Integer>> {

        @Override
        protected ArrayList<Integer> doInBackground(WhichClicked... params) {
            WhichClicked wc = params[0];
            global_qs = Questions.getQuestionWc(wc, db, Settings.num, getApplicationContext());

            // get prev_answer and suggestions
            ArrayList<Integer> ans = Answer.getSuggestions(wc, global_qs.q_no, getApplicationContext(), db);

            Log.d("main question", " q = " + global_qs.q);
            Log.d("main question", " q_no = " + global_qs.q_no);

            return ans;
        }

        @Override
        protected void onPostExecute(ArrayList<Integer> ans) {

            //set question to UI
            TextView tv_questions = (TextView) findViewById(R.id.tv_question_window_entry_1);
            tv_questions.setText(global_qs.q);

            //remove privacy buttons
            LinearLayout improveLayout = (LinearLayout) findViewById(R.id.improveLayout);
            improveLayout.setVisibility(View.GONE);

            //bring default layout
            LinearLayout homeLayout = (LinearLayout) findViewById(R.id.homeLayout);
            homeLayout.setVisibility(View.VISIBLE);

            final ImageButton submit;
            submit = (ImageButton) findViewById(R.id.button_bid_1);
            submit.setVisibility(View.INVISIBLE);

            final SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
            final Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
            Double current_privacy = AddDouble.getDouble(settings, "current_privacy", 100);
            int current_day = settings.getInt("current_day", 2);

            ArrayList<Double> cost = Cost.getUiCost(current_day,current_credit,global_qs.q_no);
            ArrayList<Double> pri = Privacy.getUiPrivacy(current_privacy,global_qs.q_no,current_day);

            //Set the privacy improvements
            Button req_11 = (Button) findViewById(R.id.required_11);
            Button req_12 = (Button) findViewById(R.id.required_12);
            Button req_13 = (Button) findViewById(R.id.required_13);
            Button req_14 = (Button) findViewById(R.id.required_14);
            Button req_15 = (Button) findViewById(R.id.required_15);
            Button req_16 = (Button) findViewById(R.id.required_16);
            Button req_17 = (Button) findViewById(R.id.required_17);
            Button req_18 = (Button) findViewById(R.id.required_18);
            Button req_19 = (Button) findViewById(R.id.required_19);
            Button req_20 = (Button) findViewById(R.id.required_20);

            req_11.setText(String.valueOf(Round.round(cost.get(0), 1)));
            req_13.setText(String.valueOf(Round.round(cost.get(1), 1)));
            req_15.setText(String.valueOf(Round.round(cost.get(2), 1)));
            req_17.setText(String.valueOf(Round.round(cost.get(3), 1)));
            req_19.setText(String.valueOf(Round.round(cost.get(4), 1)));

            req_12.setText(String.valueOf(Round.round(pri.get(0), 1)));
            req_14.setText(String.valueOf(Round.round(pri.get(1), 1)));
            req_16.setText(String.valueOf(Round.round(pri.get(2), 1)));
            req_18.setText(String.valueOf(Round.round(pri.get(3), 1)));
            req_20.setText(String.valueOf(Round.round(pri.get(4), 1)));

            // set the UI for seuggestions as well
        }
    }


}
