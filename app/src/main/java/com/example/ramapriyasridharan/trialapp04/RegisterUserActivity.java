package com.example.ramapriyasridharan.trialapp04;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.json.GenericJson;
import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;


public class RegisterUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addListenerOnButton();







    }

    public void addListenerOnButton(){

        Button form_filled;
        form_filled = (Button) findViewById(R.id.button_form_filled);



        form_filled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extra = getIntent().getExtras();


                EditText user_first_name;
                RadioGroup radio_sex;
                String user_sex = "";
                String first_name;


                user_first_name = (EditText) findViewById(R.id.editText_name);

                radio_sex = (RadioGroup) findViewById(R.id.radioGroup_sex);
                int selected_sex = radio_sex.getCheckedRadioButtonId(); // get id of selected radio button
                switch (selected_sex) {
                    case R.id.radioButton_female_option:
                        user_sex = "f";
                        break;
                    case R.id.radioButton_male_option:
                        user_sex = "m";
                        break;


                }
                first_name = user_first_name.getText().toString(); // get value entered in field

                Bundle extras = getIntent().getExtras();
                String value = extras.getString("user_id");
                // send data to kinvey backend
                //first set values to class
                UserInformationClass ui = new UserInformationClass();
                ui.setUser_first_name(first_name);
                ui.setUser_id(value);
                ui.setUser_sex(user_sex);
                UserInstanceClass user_instance = new UserInstanceClass();

                AsyncAppData<UserInformationClass> myui = user_instance.getmKinveyClient().appData("InformationUsers",UserInformationClass.class);
                myui.save(ui, new KinveyClientCallback<UserInformationClass>() {
                    @Override
                    public void onSuccess(UserInformationClass userInformationClass) {
                        Toast.makeText(RegisterUserActivity.this, "Data successfully received", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(RegisterUserActivity.this, "Data not sent error", Toast.LENGTH_SHORT).show();
                        Log.i("ERROR sending to kinvey","ERROR");
                    }
                });

                // place intent inside button click function
                Intent intent = new Intent(v.getContext(), user_info.class);
                intent.putExtra("first_name", first_name);
                intent.putExtra("sex", user_sex);
                intent.putExtra("user_id", value);
                startActivity(intent);


            }
        });
        // derive user_id


    }

}
