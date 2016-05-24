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
import com.example.ramapriyasridharan.StoreValues.QuestionStore;
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

public class QuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Double current_privacy = AddDouble.getDouble(settings,"current_privacy",100);
        Integer current_question_number = settings.getInt("current_question_number",0);
        int current_day = settings.getInt("current_day", 1);

        Log.d("question"," current_credit = "+current_credit);
        Log.d("question"," current_privacy = "+current_privacy);
        Log.d("question"," current_day = "+current_day);
        Log.d("question"," current_question_number = "+current_question_number);

        TextView tv_credit = (TextView) findViewById(R.id.tv_this_round_credit);
        TextView tv_privacy = (TextView) findViewById(R.id.tv_this_round_privacy_entry);
        TextView tv_user_id = (TextView) findViewById(R.id.tv_user_id_entry);
        TextView tv_day_no = (TextView) findViewById(R.id.tv_day_number_entry);
        TextView tv_questions = (TextView) findViewById(R.id.tv_question_window_entry);
        UserInstanceClass user_instance = new UserInstanceClass();
        tv_user_id.setText(user_instance.getmKinveyClient().user().getId());
        tv_day_no.setText(String.valueOf(current_day));
        // first day no seeing the pirvacy or credit
        //tv_credit.setVisibility(View.INVISIBLE);
        //tv_privacy.setVisibility(View.INVISIBLE);

        if(current_day == 1){
            Log.d("question", " core1 current day 1 entered if");
            // do not show privacy and credit textview

            Log.d("core1", "enter method");
            core1();
        }

        //tv_credit.setVisibility(View.VISIBLE);
        //tv_privacy.setVisibility(View.VISIBLE);

        tv_privacy.setText(String.valueOf(current_privacy));
        tv_credit.setText(String.valueOf(current_credit));

    }




    // FIRST DAY SURVEY
    public void core1(){
        //EXCEPT DAY NO ALL 0 INDEXED

        // Setting textviews with updated values from sharedpreferences
        final SharedPreferences settings = getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        final Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
        final Double current_privacy = AddDouble.getDouble(settings,"current_privacy",100);
        int current_question_number = settings.getInt("current_question_number", 0);
        final int current_day = settings.getInt("current_day", 1);


        TextView tv_credit = (TextView) findViewById(R.id.tv_this_round_credit);
        TextView tv_privacy = (TextView) findViewById(R.id.tv_this_round_privacy_entry);
        TextView tv_user_id = (TextView) findViewById(R.id.tv_user_id_entry);
        TextView tv_day_no = (TextView) findViewById(R.id.tv_day_number_entry);
        //tv_credit.setVisibility(View.INVISIBLE);
        //tv_privacy.setVisibility(View.INVISIBLE);


        UserInstanceClass user_instance = new UserInstanceClass();
        tv_user_id.setText(user_instance.getmKinveyClient().user().getId());
        tv_day_no.setText(String.valueOf(current_day));
        tv_privacy.setText(String.valueOf(current_privacy));
        tv_credit.setText(String.valueOf(current_credit));


        Log.d("core1","entered method");
        Button submit;
        submit = (Button) findViewById(R.id.button_bid);
        final Spinner spinner_privacy = (Spinner) findViewById(R.id.spinner_privacy_choice_entry);

        // update question number
        Log.d("question", "current_question_number = " + current_question_number);

        TextView tv_questions = (TextView) findViewById(R.id.tv_question_window_entry);
        final QuestionStore qs = Questions.getQuestion(this,current_question_number);
        tv_questions.setText(qs.q);
        Log.d("question", " q = " + qs.q);


        final int temp_q_no = current_question_number;
        // if button clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("question", "entered button click");
                java.util.Date date = new java.util.Date();
                String time = new Timestamp(date.getTime()).toString();
                UserInstanceClass user_instance = new UserInstanceClass();
                String level = spinner_privacy.getSelectedItem().toString();


                // Set object value
                UserResponseClass ur = new UserResponseClass();
                ur.setUser_id(user_instance.getmKinveyClient().user().getId());
                ur.setLevel(ConvertStringToInt.categorizingStringToIntBid(level, v.getContext()));
                ur.setTimestamp(time);
                ur.setDay_no(settings.getInt("current_day", 1));
                ur.setSensors(qs.temp.s);
                ur.setDcs(qs.temp.d);
                ur.setContexts(qs.temp.c);
                ur.setImprove(-1); //first round no improvement choice
                StoreDbHelper db = new StoreDbHelper(v.getContext());
                Log.d("day", "day no =" + ur.getDay_no());
                db.replaceStoreAnswers(temp_q_no, ur.getLevel(), ur.getCredit(), ur.getDay_no());
                // modify this dynamically
                ur.setCredit_gain(Cost.returnReward(qs.temp.cost, ur.getLevel()));
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

                if(temp_q_no == 9){
                    // answered all first day questions
                    // go to next activity and save all things
                    StoreDbHelper q = new StoreDbHelper(v.getContext());
                    q.insertPointsTable(current_day, current_credit, current_privacy);
                    // reset all these parameters
                    editor.putInt("current_question_number",0);
                    editor.putInt("current_day", 2);
                    editor = AddDouble.putDouble(editor,"current_privacy",100);
                    editor = AddDouble.putDouble(editor,"current_credit",0);
                    editor.commit();
                    Intent intent = new Intent(v.getContext(), PauseActivity.class);
                    final SharedPreferences s_logged = getSharedPreferences("logged", Context.MODE_PRIVATE);
                    final SharedPreferences.Editor e = s_logged.edit();
                    e.putString("activity", "PauseActivity.class");
                    e.commit();
                    startActivity(intent);
                }
                else{
                    editor.putInt("current_question_number",temp_q_no+1);
                    editor.commit();
                }



                Log.d("questions", "before call again inside");
                core1();


            }
        });

        //Log.d("questions","before call again outside");
        //core1();



    }


}
