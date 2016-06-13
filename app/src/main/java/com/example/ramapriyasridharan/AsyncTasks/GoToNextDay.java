package com.example.ramapriyasridharan.AsyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ramapriyasridharan.StoreValues.Questions;
import com.example.ramapriyasridharan.StoreValues.Settings;
import com.example.ramapriyasridharan.StoreValues.StoreContext;
import com.example.ramapriyasridharan.helpers.AddDouble;
import com.example.ramapriyasridharan.helpers.DatabaseInstance;
import com.example.ramapriyasridharan.helpers.Round;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;
import com.example.ramapriyasridharan.trialapp04.MainActivity;
import com.example.ramapriyasridharan.trialapp04.MainQuestionsActivity;
import com.example.ramapriyasridharan.trialapp04.R;

/**
 * Created by ramapriyasridharan on 28.05.16.
 */
// call by alarm manager
public class GoToNextDay extends AsyncTask<Void, Void, Void> {

    StoreDbHelper db = DatabaseInstance.db;
    Context c = StoreContext.c;
    @Override
    public Void doInBackground(Void... params) {
        Log.d("nextday", "STARTING");
        // store into points table
        MainQuestionsActivity.getInstance().count = 0;// reset count
        SharedPreferences settings = c.getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
        Double current_privacy = AddDouble.getDouble(settings, "current_privacy", 0);
        int current_day = settings.getInt("current_day", 2);

        // insert into points table
        db.insertPointsTable(current_day, current_credit, current_privacy);

        // reset pref file
        SharedPreferences.Editor editor = settings.edit();
        editor = AddDouble.putDouble(editor, "current_credit", 0);
        editor = AddDouble.putDouble(editor, "current_privacy", 0);
        editor.putInt("current_day", current_day + 1);
        editor.putInt("prev_day", current_day);
        editor.commit();

        // insert not answered question as max privacy setting into answers table
        Questions.fillUnansweredQuestions(db, Settings.num, current_day);

        //delete from answered table
        db.emptyAnsweredTable();

        return null;
    }

    @Override
    public void onPostExecute(Void aVoid) {
        // get data from pref file
        SharedPreferences settings = c.getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        Double current_credit = AddDouble.getDouble(settings, "current_credit", 0);
        Double current_privacy = AddDouble.getDouble(settings, "current_privacy", 0);
        int current_day = settings.getInt("current_day", 2);

        Log.d("nextday", "day no =" + current_day);
        Log.d("nextday", "privacy =" + current_privacy);
        Log.d("nextday", "credit =" + current_credit);

        TextView tv_credit = (TextView) MainQuestionsActivity.getInstance().findViewById(R.id.tv_this_round_credit_1);
        TextView tv_privacy = (TextView) MainQuestionsActivity.getInstance().findViewById(R.id.tv_this_round_privacy_entry_1);
        TextView tv_day_no = (TextView) MainQuestionsActivity.getInstance().findViewById(R.id.tv_day_number_entry_1);

        tv_day_no.setText(String.valueOf(current_day));
        tv_privacy.setText(String.valueOf(Round.round(current_privacy,2)));
        tv_credit.setText(String.valueOf(Round.round(current_privacy,2)));

        //remove privacy buttons
        LinearLayout improveLayout = (LinearLayout) MainQuestionsActivity.getInstance().findViewById(R.id.improveLayout);
        improveLayout.setVisibility(View.VISIBLE);

        //bring default layout
        LinearLayout homeLayout = (LinearLayout) MainQuestionsActivity.getInstance().findViewById(R.id.homeLayout);
        homeLayout.setVisibility(View.GONE);

    }
}