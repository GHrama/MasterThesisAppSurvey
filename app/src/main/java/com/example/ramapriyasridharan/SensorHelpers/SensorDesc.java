package com.example.ramapriyasridharan.SensorHelpers;

import java.util.ArrayList;

/**
 * Created by ramapriyasridharan on 06.06.16.
 */
public abstract class SensorDesc {

    private final long timestamp;

    public SensorDesc(final long timestamp) {
        this.timestamp = timestamp;
    }

    public abstract long getSensorId();

    public long getTimestamp() {
        return timestamp;
    }

    public abstract SensorData toSensorData();
}