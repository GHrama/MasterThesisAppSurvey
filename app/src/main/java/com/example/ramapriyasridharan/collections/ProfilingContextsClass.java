package com.example.ramapriyasridharan.collections;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by ramapriyasridharan on 13.05.16.
 */
public class ProfilingContextsClass extends GenericJson {

    @Key("user_id")
    private String user_id;
    @Key("educational")
    private int ed;
    @Key("entertainment")
    private int en;
    @Key("environment")
    private int env;
    @Key("navigation")
    private int nav;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getEd() {
        return ed;
    }

    public void setEd(int ed) {
        this.ed = ed;
    }

    public int getEn() {
        return en;
    }

    public void setEn(int en) {
        this.en = en;
    }

    public int getEnv() {
        return env;
    }

    public void setEnv(int env) {
        this.env = env;
    }

    public int getNav() {
        return nav;
    }

    public void setNav(int nav) {
        this.nav = nav;
    }
}
