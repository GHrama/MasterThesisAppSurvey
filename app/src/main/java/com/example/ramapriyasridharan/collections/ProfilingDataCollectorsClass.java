package com.example.ramapriyasridharan.collections;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by ramapriyasridharan on 13.05.16.
 */
public class ProfilingDataCollectorsClass extends GenericJson {

    @Key("user_id")
    private String user_id;
    @Key("corp")
    private int corp;
    @Key("ngo")
    private int ngo;
    @Key("gov")
    private int gov;
    @Key("edu")
    private int edu;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getCorp() {
        return corp;
    }

    public void setCorp(int corp) {
        this.corp = corp;
    }

    public int getNgo() {
        return ngo;
    }

    public void setNgo(int ngo) {
        this.ngo = ngo;
    }

    public int getGov() {
        return gov;
    }

    public void setGov(int gov) {
        this.gov = gov;
    }

    public int getEdu() {
        return edu;
    }

    public void setEdu(int edu) {
        this.edu = edu;
    }
}
