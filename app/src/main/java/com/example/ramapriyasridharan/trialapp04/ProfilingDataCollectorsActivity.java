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

import com.example.ramapriyasridharan.collections.ProfilingDataCollectorsClass;
import com.example.ramapriyasridharan.collections.ProfilingSensorsClass;
import com.example.ramapriyasridharan.collections.UserInformationClass;
import com.example.ramapriyasridharan.helpers.ConvertStringToInt;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;

import java.sql.Timestamp;

public class ProfilingDataCollectorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiling_data_collectors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      addButtonListener();
    }

    public void addButtonListener(){
        Button submit;
        submit = (Button) findViewById(R.id.button_submit_dc);
        final Spinner spinner_corp_entry = (Spinner) findViewById(R.id.spinner_corp_entry);
        final Spinner spinner_ngo_entry = (Spinner) findViewById(R.id.spinner_ngo_entry);
        final Spinner spinner_gov_entry = (Spinner) findViewById(R.id.spinner_gov_entry);
        final Spinner spinner_edu_entry = (Spinner) findViewById(R.id.spinner_edu_entry);


        // Once submit is hit, check state of each option
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String corp = spinner_corp_entry.getSelectedItem().toString();
                String gov = spinner_gov_entry.getSelectedItem().toString();
                String ngo = spinner_ngo_entry.getSelectedItem().toString();
                String edu = spinner_edu_entry.getSelectedItem().toString();
                java.util.Date date= new java.util.Date();
                String time = new Timestamp(date.getTime()).toString();
                UserInstanceClass user_instance = new UserInstanceClass();

                ProfilingDataCollectorsClass ps = new ProfilingDataCollectorsClass();
                ps.setUser_id(user_instance.getmKinveyClient().user().getId());
                ps.setCorp(ConvertStringToInt.categorizingStringToIntCat(corp, v.getContext()));
                ps.setGov(ConvertStringToInt.categorizingStringToIntCat(gov, v.getContext()));
                ps.setNgo(ConvertStringToInt.categorizingStringToIntCat(ngo, v.getContext()));
                ps.setEdu(ConvertStringToInt.categorizingStringToIntCat(edu, v.getContext()));

                SharedPreferences settings = getSharedPreferences("categorizing", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("corporation", ps.getCorp());
                editor.putInt("non_governmental_organization", ps.getNgo());
                editor.putInt("government", ps.getGov());
                editor.putInt("educational_institution", ps.getEdu());
                editor.commit();


                AsyncAppData<ProfilingDataCollectorsClass> myui = user_instance.getmKinveyClient().appData("ProfilingDataCollectors", ProfilingDataCollectorsClass.class);

                myui.save(ps, new KinveyClientCallback<ProfilingDataCollectorsClass>() {

                    @Override
                    public void onSuccess(ProfilingDataCollectorsClass userInformationClass) {

                        Toast.makeText(ProfilingDataCollectorsActivity.this, "Data successfully received", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                        Toast.makeText(ProfilingDataCollectorsActivity.this, "Data not sent error", Toast.LENGTH_SHORT).show();
                        Log.i("ERROR sending to kinvey", "ERROR");
                    }
                });

                // place intent inside button click function

                Intent intent = new Intent(v.getContext(), ProfilingContextsActivity.class);
                startActivity(intent);

            }
        });

    }

}
