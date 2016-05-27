package com.example.ramapriyasridharan.trialapp04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramapriyasridharan.collections.UserInformationClass;
import com.example.ramapriyasridharan.helpers.ConvertStringToInt;
import com.example.ramapriyasridharan.helpers.SendToKinvey;
import com.example.ramapriyasridharan.helpers.UserInstanceClass;
import com.kinvey.android.AsyncAppData;
import com.kinvey.java.core.KinveyClientCallback;


public class GetUserInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set user_id
        final TextView tv_id_entry = (TextView) findViewById(R.id.tv_id_entry);
        UserInstanceClass ui = new UserInstanceClass();
        tv_id_entry.setText(ui.mKinveyClient.user().getId());
        addListenerOnButton();

    }

    // Once the user clicks submit
    public void addListenerOnButton(){
        final UserInformationClass ui = new UserInformationClass();
        Button submit;
        submit = (Button) findViewById(R.id.submit_button);
        final Switch switch_gender_entry = (Switch) findViewById(R.id.switch_gender_entry);
        final Switch switch_background_entry = (Switch) findViewById(R.id.switch_background_entry);
        final EditText edittext_year_entry = (EditText) findViewById(R.id.editText_birth_entry);
        final Spinner spinner_education_entry = (Spinner) findViewById(R.id.spinner_education_entry);
        final Spinner spinner_employment_entry = (Spinner) findViewById(R.id.spinner_employment_entry);

        // Once submit is hit, check state of each option
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HI","IN the setonclick method");
                //get gender
                if (switch_gender_entry.isChecked()) {
                    //on = female
                    ui.setGender(Integer.toString(1));
                    Log.d("HI", "set female");
                } else {
                    //off = male
                    ui.setGender(Integer.toString(2));
                    Log.d("HI", "Set male");
                }

                // get year of birth
                String birth = edittext_year_entry.getText().toString();
                ui.setBirth_year(birth);
                if(Integer.parseInt(birth) > 2016 && Integer.parseInt(birth) < 1900){
                    Toast.makeText(GetUserInformation.this, "Re-enter your date of birth Correctly", Toast.LENGTH_SHORT).show();
                }

                Log.d("HI", "set birth year");

                // get the chosen option of educational level
                String educational_level = spinner_education_entry.getSelectedItem().toString();
                ui.setEducation_level(Integer.toString(ConvertStringToInt.education_level(educational_level,v.getContext())));
                Log.d("HI", "set education level"+ui.getEducation_level());
                String employment_level = spinner_employment_entry.getSelectedItem().toString();
                ui.setEmployment_status(Integer.toString(ConvertStringToInt.employment_status(employment_level, v.getContext())));
                Log.d("HI", "set employment"+ui.getEmployment_status());
                if (switch_background_entry.isChecked()) {
                    // on = cs background
                    ui.setEducation_background(Integer.toString(1));
                    Log.d("HI", "set background");
                } else {
                    // off = other
                    ui.setEducation_background(Integer.toString(2));
                    Log.d("HI", "set background");
                }
                Log.d("HI", "done getting the data");
                UserInstanceClass user_instance = new UserInstanceClass();
                Log.d("HI", "get user_instance");

                //SendToKinvey.sendUserInformation(user_instance,"UserInformation",ui);
                // place intent inside button click function
                Intent intent = new Intent(v.getContext(), ProfilingFeaturesActivity.class);
                final SharedPreferences s_logged = getSharedPreferences("logged", Context.MODE_PRIVATE);
                final SharedPreferences.Editor e = s_logged.edit();
                e.putInt("activity",3);
                e.commit();
                startActivity(intent);


            }
        });

        // send this information to kinvey collection InformationUsers


            }

}
