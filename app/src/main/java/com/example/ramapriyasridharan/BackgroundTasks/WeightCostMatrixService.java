package com.example.ramapriyasridharan.BackgroundTasks;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ramapriyasridharan.StoreValues.WeightCostAnswersMatrix;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;

/**
 * Created by ramapriyasridharan on 24.05.16.
 */
public class WeightCostMatrixService extends IntentService{

    public WeightCostMatrixService(){
        super("hi");
    }
    /**
     * Creates an IntentService.  Fill up weight and cost matrices and send to db in background
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WeightCostMatrixService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("intent", "inside");
        WeightCostAnswersMatrix.calcWeightMatrix(getApplicationContext());
        WeightCostAnswersMatrix.getCostMatrix();
        StoreDbHelper db = new StoreDbHelper(getApplicationContext());
        WeightCostAnswersMatrix.insertWeightCostDb(db);
        db.close();
        Log.d("intent", "done work");
    }
}
