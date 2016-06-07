package com.example.ramapriyasridharan.SensorHelpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ramapriyasridharan on 06.06.16.
 */
public class SensorConfiguration {

    private Context context;

    private static SensorConfiguration sensorConfiguration;

    public static SensorConfiguration getInstance(Context context) {
        if (sensorConfiguration == null) {
            sensorConfiguration = new SensorConfiguration(context);
        }
        return sensorConfiguration;
    }

    private SensorConfiguration(Context context) {
        this.context = context;
    }

    public SensorCollectStatus getInitialSensorCollectStatus(long sensorID) {
        SharedPreferences settings = context.getSharedPreferences(SensorStatics.SENSOR_PREFS, 0);
        boolean doMeasure = settings.getBoolean(Long.toHexString(sensorID) + "_doMeasure", true);
        boolean doShare = settings.getBoolean(Long.toHexString(sensorID) + "_doShare", true);

        int measureInterval = (int)context.getSharedPreferences(SensorStatics.SENSOR_FREQ, 0)
                .getInt(Long.toHexString(sensorID) + "_freqValue", 30) * 1000;
        System.out.println(measureInterval);
//		int measureInterval = settings.getInt(Long.toHexString(sensorID) + "_measureInterval", 30 * 1000);

        long measureDuration = settings.getLong(Long.toHexString(sensorID) + "_measureDuration", 5000);
        int collectAmount = settings.getInt(Long.toHexString(sensorID) + "_collectAmount", 500);
        SensorCollectStatus scs = new SensorCollectStatus(sensorID, doMeasure, doShare, measureInterval, measureDuration, collectAmount);
        return scs;
    }

}
