package com.example.ramapriyasridharan.helpers;

import android.content.Context;
import android.content.Intent;

import com.example.ramapriyasridharan.trialapp04.GetUserInformation;
import com.example.ramapriyasridharan.trialapp04.MainActivity;
import com.example.ramapriyasridharan.trialapp04.MainQuestionsActivity;
import com.example.ramapriyasridharan.trialapp04.PauseActivity;
import com.example.ramapriyasridharan.trialapp04.ProfilingContextsActivity;
import com.example.ramapriyasridharan.trialapp04.ProfilingDataCollectorsActivity;
import com.example.ramapriyasridharan.trialapp04.ProfilingFeaturesActivity;
import com.example.ramapriyasridharan.trialapp04.ProfilingSensorsActivity;
import com.example.ramapriyasridharan.trialapp04.QuestionsActivity;

/**
 * Created by ramapriyasridharan on 25.05.16.
 */
public class GotoActivity {

    public static void go(Context context, int a){
        switch(a){
            case 1: context.startActivity(new Intent(context, MainActivity.class));
            case 2: context.startActivity(new Intent(context, GetUserInformation.class));
            case 3: context.startActivity(new Intent(context, ProfilingFeaturesActivity.class));
            case 4: context.startActivity(new Intent(context, ProfilingSensorsActivity.class));
            case 5: context.startActivity(new Intent(context, ProfilingDataCollectorsActivity.class));
            case 6: context.startActivity(new Intent(context, ProfilingContextsActivity.class));
            case 7: context.startActivity(new Intent(context, QuestionsActivity.class));
            case 8: context.startActivity(new Intent(context, PauseActivity.class));
            case 9: context.startActivity(new Intent(context, MainQuestionsActivity.class));
        }
    }
}
