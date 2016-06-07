package com.example.ramapriyasridharan.helpers;

import android.content.Context;
import android.util.Log;

import com.example.ramapriyasridharan.StoreValues.Contexts;
import com.example.ramapriyasridharan.StoreValues.CostQno;
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

    public static double returnTotalCost(int day){
        double cost = 0.0;
        Log.d("Cost", " day = "+day);
        StoreDbHelper db = DatabaseInstance.db;
        ArrayList<Double> costs = db.returnCostAnswersTable(day);
        Log.d("Cost", " Number of answers = "+costs.size());
        for(int i =0 ; i< costs.size();i++){
            Log.d("Cost", " costs of answered questions = "+costs.get(i));
            cost += costs.get(i);
        }
        return  cost;
    }

    public static ArrayList<Double> getUiCost(int day, double current_cost, int qno){

        StoreDbHelper db = DatabaseInstance.db;

        double c = db.getCostIfAnsweredAnswersTable(day,qno);
        double cost_question = db.getCostForQnoQuestionsTable(qno);
        ArrayList<Double> p = new ArrayList<Double>();
        Log.d("privacy", " Current cost" + current_cost);
        Log.d("privacy", " cost obtained before" + c);
        if(c == -1){
            Log.d("privacy", " question not answered");
            // question not answered
            for(int i = 0;i<5;i++){
                // not answered question this time
                p.add(returnReward(cost_question,i+1));
                Log.d("privacy", " level" + i + " improvement " + p.get(i));
            }
        }
        else{
            // question has already being answered
            for(int i = 0;i < 5;i++){
                Log.d("privacy", " question answered before");
                // new cost minus previous cost
                p.add(returnReward(cost_question,i+1)-c);
                Log.d("privacy", " level" + i + " improvement " + p.get(i));
            }
        }

        return p;
    }


}
