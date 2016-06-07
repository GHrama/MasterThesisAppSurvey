package com.example.ramapriyasridharan.SensorHelpers;

import java.util.ArrayList;

/**
 * Created by ramapriyasridharan on 06.06.16.
 */
public class SensorData {

    public long timestamp;
    public long sensor_id;
    public ArrayList<Float> values;

    public SensorData(long timestamp, long sensor_id, ArrayList<Float> values){
        this.timestamp = timestamp;
        this.sensor_id = sensor_id;
        this.values = values;
    }

}
