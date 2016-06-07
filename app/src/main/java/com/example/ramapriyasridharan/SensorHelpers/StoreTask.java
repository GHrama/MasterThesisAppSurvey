package com.example.ramapriyasridharan.SensorHelpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ramapriyasridharan.helpers.DatabaseInstance;
import com.example.ramapriyasridharan.localstore.StoreDbHelper;

/**
 * Created by ramapriyasridharan on 06.06.16.
 */
public class StoreTask extends AsyncTask<SensorDesc, Void, Void> {

    private Context context;

    public StoreTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(SensorDesc... params) {
        Log.d("sensor", "StoreTask");
        if (params != null && params.length > 0) {
            Log.d("sensor", "params != null && params.length > 0");
            StoreDbHelper db = DatabaseInstance.db;
            for (int i = 0; i < params.length; i++) {

                SensorData sd = params[i].toSensorData();
                Log.d("sensor", "Sensor Data = "+sd);
                // if accelerometer enter intro STORE_ACCELEROMETER TABLE
                if(params[i].getSensorId() == 0x0000000000000000L){
                    db.insertIntoAccelerometerTable(sd.timestamp, sd.values.get(0), sd.values.get(1), sd.values.get(2));
                }

            }
        }
        return null;
    }

}