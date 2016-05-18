package com.example.ramapriyasridharan.collections;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;


import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by ramapriyasridharan on 13.05.16.
 */
public class ProfilingSensorsClass extends GenericJson {

    @Key("user_id")
    private String user_id;
    @Key("acc")
    private int acc;
    @Key("gps")
    private int gps;
    @Key("light")
    private int light;
    @Key("prox")
    private int prox;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getAcc() {
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public int getGps() {
        return gps;
    }

    public void setGps(int gps) {
        this.gps = gps;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getProx() {
        return prox;
    }

    public void setProx(int gyr) {
        this.prox = gyr;
    }
}

