package com.example.ramapriyasridharan.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ramapriyasridharan.StoreValues.PrivacyQno;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;

import java.util.ArrayList;

/**
 * Created by ramapriyasridharan on 19.05.16.
 */
public class Privacy {

    public static double returnPrivacyPercentage(int day){
        //obtain number of questions answered till now
        //obtain all the levels for each question answered till now
        StoreDbHelper db = DatabaseInstance.db;
        ArrayList<Double> levels = db.getAllLevelsFromAnswersTable(day);
        int number = levels.size(); // number of answered questions today
        Log.d("privacy"," questions answered ="+number);
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
        Log.d("privacy"," privacy ="+ privacy);
        privacy = privacy / number ;
        Log.d("privacy","privacy =" + privacy);
        return privacy;
    }


    public static double convertLevelToPrivacy(int level){
        int privacy = 0;

        if(level == 1){
            privacy = 0;
        }
        else if(level == 2){
            privacy = 25;
        }
        else if(level == 3){
            privacy = 50;
        }
        else if(level == 4){
            privacy = 75;
        }
        else if(level == 5){
            privacy = 100;
        }

        return privacy;
    }

    // return privacy improvement for each option chosen
    // from verylow privacy option to very high privacy option
    public static ArrayList<Double> getUiPrivacy(double current_privacy,int current_question_no,int day_no){

        StoreDbHelper db = DatabaseInstance.db;

        // get pirvacy %tage and question numbers
        ArrayList<PrivacyQno> p = db.getPrivacyConvertedQnoAnswersTable(day_no);
        double temp = 0;
        boolean flag = false;

        for(int i=0; i< p.size();i++){
            if(p.get(i).qno == current_question_no){
                flag = true;
                continue;
            }
            temp += p.get(i).privacy;
        }
        Log.d("privacy"," temp ="+ temp);
        int no;
        double meek = 0;
        ArrayList<Double> p1 = new ArrayList<Double>();

        if(flag){
            // the answer has already been answered
            no = p.size();
            for(int i=0;i<5;i++){
                meek = (temp + convertLevelToPrivacy(i+1))/no;
                p1.add(i,(meek-current_privacy));
                Log.d("privacy", " level" +i+" improvement "+(meek-current_privacy));
            }
        }
        else{
            // the answer has not been answered today, hence add + 1 to array size
            no = p.size()+1;
            for(int i=0;i<5;i++){
                meek = (temp + convertLevelToPrivacy(i+1))/no;
                p1.add(i,(meek-current_privacy));
                Log.d("privacy", " level" + i + " improvement " + (meek - current_privacy));
            }
        }

        return  p1;
    }
}
