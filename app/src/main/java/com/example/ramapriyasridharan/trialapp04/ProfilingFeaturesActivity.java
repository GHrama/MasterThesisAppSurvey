package com.example.ramapriyasridharan.trialapp04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramapriyasridharan.collections.ProfilingFeaturesClass;
import com.example.ramapriyasridharan.collections.UserInformationClass;
import com.example.ramapriyasridharan.helpers.ConvertStringToInt;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;

import java.sql.Timestamp;

public class ProfilingFeaturesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiling_features);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addListenerButtonClick();
    }

    public void addListenerButtonClick(){


        Button submit;
        submit = (Button) findViewById(R.id.button_submit_features);
        final Spinner spinner_sensors = (Spinner) findViewById(R.id.spinner_sensor_entry);
        final Spinner spinner_dc = (Spinner) findViewById(R.id.spinner_dc_entry);
        final Spinner spinner_context = (Spinner) findViewById(R.id.spinner_context_entry);
        TextView tv_instructions_label = (TextView) findViewById(R.id.tv_instruction_label);

        // Once submit is hit, check state of each option
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sensor_level = spinner_sensors.getSelectedItem().toString();
                String dc_level = spinner_dc.getSelectedItem().toString();
                String context_level = spinner_context.getSelectedItem().toString();
                java.util.Date date = new java.util.Date();
                String time = new Timestamp(date.getTime()).toString();
                UserInstanceClass user_instance = new UserInstanceClass();

                ProfilingFeaturesClass pf = new ProfilingFeaturesClass();
                pf.setUser_id(user_instance.getmKinveyClient().user().getId());
                pf.setSensor(ConvertStringToInt.categorizingStringToIntCat(sensor_level, v.getContext()));
                pf.setDc(ConvertStringToInt.categorizingStringToIntCat(dc_level, v.getContext()));
                pf.setContext(ConvertStringToInt.categorizingStringToIntCat(context_level, v.getContext()));
                // save user answers locally
                SharedPreferences settings = getSharedPreferences("categorizing", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("sensors", pf.getSensor());
                editor.putInt("data_collectors", pf.getDc());
                editor.putInt("contexts", pf.getContext());
                editor.commit();

                AsyncAppData<ProfilingFeaturesClass> myui = user_instance.getmKinveyClient().appData("ProfilingFeatures", ProfilingFeaturesClass.class);

                myui.save(pf, new KinveyClientCallback<ProfilingFeaturesClass>() {

                    @Override
                    public void onSuccess(ProfilingFeaturesClass userInformationClass) {

                        Toast.makeText(ProfilingFeaturesActivity.this, "Data successfully received", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                        Toast.makeText(ProfilingFeaturesActivity.this, "Data not sent error", Toast.LENGTH_SHORT).show();
                        Log.i("ERROR sending to kinvey", "ERROR");
                    }
                });

                // place intent inside button click function
                Intent intent = new Intent(v.getContext(), ProfilingSensorsActivity.class);
                startActivity(intent);


            }
        });


    }

}
