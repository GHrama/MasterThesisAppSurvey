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
import android.widget.Toast;

import com.example.ramapriyasridharan.StoreValues.WeightCostAnswersMatrix;
import com.example.ramapriyasridharan.collections.ProfilingContextsClass;
import com.example.ramapriyasridharan.collections.ProfilingDataCollectorsClass;
import com.example.ramapriyasridharan.helpers.ConvertStringToInt;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;

import java.sql.Timestamp;

public class ProfilingContextsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiling_contexts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addButtonListener();
    }

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


                AsyncAppData<ProfilingContextsClass> myui = user_instance.getmKinveyClient().appData("ProfilingContexts", ProfilingContextsClass.class);

                myui.save(ps, new KinveyClientCallback<ProfilingContextsClass>() {

                    @Override
                    public void onSuccess(ProfilingContextsClass userInformationClass) {

                        Toast.makeText(ProfilingContextsActivity.this, "Data successfully received", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                        Toast.makeText(ProfilingContextsActivity.this, "Data not sent error", Toast.LENGTH_SHORT).show();
                        Log.i("ERROR sending to kinvey", "ERROR");
                    }
                });

                // place intent inside button click function
                // now computer the weight matrix


                // now compute the cost matrix



                WeightCostAnswersMatrix.calcWeightMatrix(v.getContext());
                WeightCostAnswersMatrix.getCostMatrix();
                WeightCostAnswersMatrix.insertWeightCostDb(v.getContext());

                Intent intent = new Intent(v.getContext(), QuestionsActivity.class);
                startActivity(intent);



            }
        });

    }

}
