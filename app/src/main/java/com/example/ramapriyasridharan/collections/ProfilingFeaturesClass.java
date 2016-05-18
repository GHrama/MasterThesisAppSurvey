package com.example.ramapriyasridharan.collections;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by ramapriyasridharan on 13.05.16.
 */
public class ProfilingFeaturesClass extends GenericJson{

    @Key("user_id")
    private String user_id;
    @Key("sensor")
    private int sensor;
    @Key("data_collector")
    private int dc;
    @Key("context")
    private int context;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getSensor() {
        return sensor;
    }

    public void setSensor(int sensor) {
        this.sensor = sensor;
    }

    public int getDc() {
        return dc;
    }

    public void setDc(int dc) {
        this.dc = dc;
    }

    public int getContext() {
        return context;
    }

    public void setContext(int context) {
        this.context = context;
    }
}
