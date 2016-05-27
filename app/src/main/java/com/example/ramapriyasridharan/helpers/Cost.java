package com.example.ramapriyasridharan.helpers;

import android.content.Context;
import android.util.Log;

import com.example.ramapriyasridharan.StoreValues.Contexts;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;

import java.util.ArrayList;

/**
 * Created by ramapriyasridharan on 19.05.16.
 */
public class Cost {

    // return rewards
    public static double returnReward(double cost, int level){

        return ((double) cost / level);
    }

    public static double returnTotalCost(int day,StoreDbHelper db){
        double cost = 0.0;
        Log.d("Cost", " day = "+day);
        ArrayList<Double> costs = db.returnCostAnswersTable(day);
        Log.d("Cost", " Number of answers = "+costs.size());
        for(int i =0 ; i< costs.size();i++){
            Log.d("Cost", " costs of answered questions = "+costs.get(i));
            cost += costs.get(i);
        }
        return  cost;
    }


}
