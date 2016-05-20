package com.example.ramapriyasridharan.collections;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by ramapriyasridharan on 14.05.16.
 */
public class UserResponseClass extends GenericJson {

    @Key("user_id")
    private String user_id;
    @Key("sensors")
    private int sensors;
    @Key("data_collectors")
    private int dcs;
    @Key("contexts")
    private int contexts;
    @Key("privacy_level")
    private int level;
    @Key("timestamp")
    private String timestamp;
    @Key("improve")
    private int improve; // (1) privacy or (2) credit
    @Key("day_no")
    private int day_no;
    @Key("privacy_percentage")
    private double privacy_percentage;
    @Key("credit")
    private double credit;

    public double getCredit_gain() {
        return credit_gain;
    }

    public void setCredit_gain(double credit_gain) {
        this.credit_gain = credit_gain;
    }

    @Key("credit_gain")
    private double credit_gain;



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getSensors() {
        return sensors;
    }

    public void setSensors(int sensors) {
        this.sensors = sensors;
    }

    public int getDcs() {
        return dcs;
    }

    public void setDcs(int dcs) {
        this.dcs = dcs;
    }

    public int getContexts() {
        return contexts;
    }

    public void setContexts(int contexts) {
        this.contexts = contexts;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getImprove() {
        return improve;
    }

    public void setImprove(int improve) {
        this.improve = improve;
    }

    public int getDay_no() {
        return day_no;
    }

    public void setDay_no(int day_no) {
        this.day_no = day_no;
    }

    public double getPrivacy_percentage() {
        return privacy_percentage;
    }

    public void setPrivacy_percentage(double privacy_percentage) {
        this.privacy_percentage = privacy_percentage;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }
}
