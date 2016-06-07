package com.example.ramapriyasridharan.SensorHelpers;

import java.util.ArrayList;

/**
 * Created by ramapriyasridharan on 06.06.16.
 */
public class SensorDescAccelerometer extends SensorDesc {

    public static final long SENSOR_ID = 0x0000000000000000L;

    private final float accX;
    private final float accY;
    private final float accZ;

    public SensorDescAccelerometer(final long timestamp, final float accX, final float accY, final float accZ) {
        super(timestamp);
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
    }

    public SensorDescAccelerometer(final long timestamp, ArrayList<Float> temp) {
        super(timestamp);
        this.accX = temp.get(0);
        this.accY = temp.get(1);
        this.accZ = temp.get(2);
    }

    public float getAccX() {
        return accX;
    }

    public float getAccY() {
        return accY;
    }

    public float getAccZ() {
        return accZ;
    }

    @Override
    public long getSensorId() {
        return SENSOR_ID;
    }

    @Override
    public SensorData toSensorData() {
        ArrayList<Float> temp = new ArrayList<Float>();
        temp.add(getAccX());
        temp.add(getAccY());
        temp.add(getAccZ());
        return new SensorData(getTimestamp(),getSensorId(),temp);
    }

}