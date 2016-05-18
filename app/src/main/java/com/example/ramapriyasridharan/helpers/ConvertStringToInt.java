package com.example.ramapriyasridharan.helpers;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.ramapriyasridharan.trialapp04.R;

/**
 * Created by ramapriyasridharan on 12.05.16.
 */
public class ConvertStringToInt {


    public static int education_level(String s, Context context){
        String mTestArray[] = context.getResources().getStringArray(R.array.education_arrays);
        for(int i = 0; i < mTestArray.length ; i++){
            if(s.equals(mTestArray[i])){
                return i+1; // return index of the list
            }
        }
        return -1; //not found in the list
    }

    public static int employment_status(String s,Context context){
        String mTestArray[] = context.getResources().getStringArray(R.array.employment_arrays);
        for(int i = 0; i < mTestArray.length ; i++){
            if(s.equals(mTestArray[i])){
                return i+1; // return index of the list
            }
        }
        return -1; //not found in the list
    }

    public static int categorizingStringToIntCat(String s,Context context){
        String mTestArray[] = context.getResources().getStringArray(R.array.categorizing_arrays);
        for(int i=0; i < mTestArray.length ; i++){
            if(s.equals(mTestArray[i])){
                return i+1;
                // higher the number the higher the intrusion felt
            }
        }
        return -1;
    }

    public static int categorizingStringToIntBid(String s,Context context){
        String mTestArray[] = context.getResources().getStringArray(R.array.bidding_arrays);
        for(int i=0; i < mTestArray.length ; i++){
            if(s.equals(mTestArray[i])){
                return i+1;
                // higher the number the higher the intrusion felt
            }
        }
        return -1;
    }


    /*<string-array name="education_arrays">
    <item>Lesser than High School</item>
    <item>High School</item>
    <item>Some College</item>
    <item>Bachelor</item>
    <item>Master</item>
    <item>PHD</item>
    </string-array>

    <string name="employment_prompt">Choose a country</string>

    <string-array name="employment_arrays">
    <item>Full time</item>
    <item>Part Time</item>
    <item>Unemployed and looking for work</item>
    <item>Unemployed and not looking for work</item>
    <item>Retired</item>
    <item>Student</item>
    <item>Disabled</item>
    </string-array>*/

}
