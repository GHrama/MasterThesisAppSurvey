package com.example.ramapriyasridharan.StoreValues;

/**
 * Created by ramapriyasridharan on 22.05.16.
 */
public class WhichClicked {
    public  boolean privacy;
    public  boolean credit;

    public WhichClicked(){

        this.privacy = false;
        this.credit = false;

    }

    public boolean isPrivacy() {
        return privacy;
    }

    // privacy clicked
    public void setPrivacy() {
        this.privacy = true;
        this.credit = false;
    }

    public boolean isCredit() {
        return credit;
    }

    //credit clicked
    public void setCredit() {
        this.credit = true;
        this.privacy = false;
    }
}
