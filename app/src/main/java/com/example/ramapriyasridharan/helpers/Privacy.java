package com.example.ramapriyasridharan.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ramapriyasridharan.localstore.StoreDbHelper;

import java.util.ArrayList;

/**
 * Created by ramapriyasridharan on 19.05.16.
 */
public class Privacy {

    public static double returnPrivacyPercentage(int day, Context c){
        //obtain number of questions answered till now
        //obtain all the levels for each question answered till now
        StoreDbHelper db = new StoreDbHelper(c);
        ArrayList<Double> levels = db.getAllLevelsFromAnswersTable(day);
        int number = levels.size(); // number of answered questions today
        Log.d("privacy","questions answered ="+number);
        double privacy = 0;
        for(int i = 0;i < number ; i++){
            if(levels.get(i) == 1){
                privacy += 0;
            }
            else if(levels.get(i) == 2){
                privacy += 25;
            }
            else if(levels.get(i) == 3){
                privacy += 50;
            }
            else if(levels.get(i) == 4){
                privacy += 75;
            }
            else if(levels.get(i) == 5){
                privacy += 100;
            }
        }
        privacy = privacy / number ;
        Log.d("privacy","privacy ="+privacy);
        return privacy;
    }
}
