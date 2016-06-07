package com.example.ramapriyasridharan.trialapp04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.example.ramapriyasridharan.BackgroundTasks.WeightCostMatrixService;
import com.example.ramapriyasridharan.collections.ProfilingContextsClass;
import com.example.ramapriyasridharan.helpers.ConvertStringToInt;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;

import java.sql.Timestamp;

public class ProfilingContextsActivity extends AppCompatActivity {


    // Loading sign from android while calculating & storing weight matrix
    // FIND IT
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_profiling_contexts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addButtonListener();
    }

    long last_clicked = 0;

    public void addButtonListener(){
        Button submit;
        submit = (Button) findViewById(R.id.button_submit_context);
        final Spinner spinner_ed_entry = (Spinner) findViewById(R.id.spinner_ed_entry);
        final Spinner spinner_en_entry = (Spinner) findViewById(R.id.spinner_en_entry);
        final Spinner spinner_env_entry = (Spinner) findViewById(R.id.spinner_env_entry);
        final Spinner spinner_nav_entry = (Spinner) findViewById(R.id.spinner_nav_entry);


        // Once submit is hit, check state of each option
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SystemClock.elapsedRealtime() - last_clicked < 1000){
                    return;
                }
                last_clicked = SystemClock.elapsedRealtime();

                String ed = spinner_ed_entry.getSelectedItem().toString();
                String en = spinner_en_entry.getSelectedItem().toString();
                String env = spinner_env_entry.getSelectedItem().toString();
                String nav = spinner_nav_entry.getSelectedItem().toString();
                java.util.Date date= new java.util.Date();
                String time = new Timestamp(date.getTime()).toString();
                UserInstanceClass user_instance = new UserInstanceClass();

                ProfilingContextsClass ps = new ProfilingContextsClass();
                ps.setUser_id(user_instance.getmKinveyClient().user().getId());
                ps.setEd(ConvertStringToInt.categorizingStringToIntCat(ed, v.getContext()));
                ps.setEn(ConvertStringToInt.categorizingStringToIntCat(en, v.getContext()));
                ps.setEnv(ConvertStringToInt.categorizingStringToIntCat(env, v.getContext()));
                ps.setNav(ConvertStringToInt.categorizingStringToIntCat(nav, v.getContext()));

                //store values locally
                SharedPreferences settings = getSharedPreferences("categorizing", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("education", ps.getEd());
                editor.putInt("entertainment", ps.getEn());
                editor.putInt("environment", ps.getEnv());
                editor.putInt("navigation", ps.getNav());
                editor.commit();

                // now compute the cost matrix

                //SendToKinvey.sendProfilingContexts(user_instance,"ProfilingContexts", ps);

                // start background process
                Intent weight_cost_background = new Intent(v.getContext(), WeightCostMatrixService.class);
                startService(weight_cost_background);

                Intent intent = new Intent(v.getContext(), QuestionsActivity.class);
                final SharedPreferences s_logged = getSharedPreferences("logged", Context.MODE_PRIVATE);
                final SharedPreferences.Editor e = s_logged.edit();
                e.putInt("activity",7);
                e.commit();
                startActivity(intent);



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

}
