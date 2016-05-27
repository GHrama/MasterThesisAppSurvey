package com.example.ramapriyasridharan.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.ramapriyasridharan.StoreValues.WhichClicked;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ramapriyasridharan on 27.05.16.
 */
public class Answer {

    // given q_no return the previous answer
    public static int getPreviousAnswer(int q_no,StoreDbHelper db, Context c){
        SharedPreferences settings =  c.getSharedPreferences("bid_window_values", Context.MODE_PRIVATE);
        int current_day = settings.getInt("current_day", 2);
        // questions class takes care of updating that value
        // resetted every day
        int prev_day = settings.getInt("prev_day", (current_day-1));
        return db.getLevelFromAnswersTable(q_no,prev_day);
    }

    // array with list of suggested options
    // first is previous answer, rest are improvement options
    public static ArrayList<Integer> getSuggestions(WhichClicked wc,int q_no,Context c, StoreDbHelper db){
        ArrayList<Integer> ans = new ArrayList<Integer>();
        int prev_answer = getPreviousAnswer(q_no,db,c);
        Log.d("previous answer"," ans = "+prev_answer);
        if(wc.isPrivacy()){
            int level = 5;
            ans.add(prev_answer);
            while(level > prev_answer){
                ans.add(level);
                Log.d("suggestions", " levels = " + level);
                level --;
            }
        }
        else if(wc.isCredit()){
            int level = 1;
            ans.add(prev_answer);
            while(level < prev_answer){
                ans.add(level);
                Log.d("suggestions", " levels = " + level);
                level ++;
            }
        }
        return ans;
    }
}
