package com.example.ramapriyasridharan.trialapp04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

import com.example.ramapriyasridharan.collections.ProfilingFeaturesClass;
import com.example.ramapriyasridharan.collections.ProfilingSensorsClass;
import com.example.ramapriyasridharan.collections.UserInformationClass;
import com.example.ramapriyasridharan.helpers.AddDouble;
import com.example.ramapriyasridharan.helpers.ConvertStringToInt;
import com.example.ramapriyasridharan.helpers.SendToKinvey;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;

import java.sql.Timestamp;

public class ProfilingSensorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_profiling_sensors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



       addButtonListener();
    }

    public void addButtonListener(){
        final UserInformationClass ui = new UserInformationClass();
        Button submit;
        submit = (Button) findViewById(R.id.button_submit_sensors);
        final Spinner spinner_acc_entry = (Spinner) findViewById(R.id.spinner_acc_entry);
        final Spinner spinner_gps_entry = (Spinner) findViewById(R.id.spinner_gps_entry);
        final Spinner spinner_light_entry = (Spinner) findViewById(R.id.spinner_light_entry);
        final Spinner spinner_prox_entry = (Spinner) findViewById(R.id.spinner_prox_entry);


        // Once submit is hit, check state of each option
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String acc = spinner_acc_entry.getSelectedItem().toString();
                String gps = spinner_gps_entry.getSelectedItem().toString();
                String light = spinner_light_entry.getSelectedItem().toString();
                String prox = spinner_prox_entry.getSelectedItem().toString();
                java.util.Date date = new java.util.Date();
                String time = new Timestamp(date.getTime()).toString();
                UserInstanceClass user_instance = new UserInstanceClass();

                ProfilingSensorsClass ps = new ProfilingSensorsClass();
                ps.setUser_id(user_instance.getmKinveyClient().user().getId());
                ps.setAcc(ConvertStringToInt.categorizingStringToIntCat(acc, v.getContext()));
                ps.setGps(ConvertStringToInt.categorizingStringToIntCat(gps, v.getContext()));
                ps.setLight(ConvertStringToInt.categorizingStringToIntCat(light, v.getContext()));
                ps.setProx(ConvertStringToInt.categorizingStringToIntCat(prox, v.getContext()));

                //store values locally
                SharedPreferences settings = getSharedPreferences("categorizing", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("accelerometer", ps.getAcc());
                editor.putInt("gps", ps.getGps());
                editor.putInt("light", ps.getLight());
                editor.putInt("proximity", ps.getProx());
                editor.commit();


                //SendToKinvey.sendProfilingSensors(user_instance,"ProfilingSensors",ps);
                Intent intent = new Intent(v.getContext(), ProfilingDataCollectorsActivity.class);
                final SharedPreferences s_logged = getSharedPreferences("logged", Context.MODE_PRIVATE);
                final SharedPreferences.Editor e = s_logged.edit();
                e.putInt("activity", 5);
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
