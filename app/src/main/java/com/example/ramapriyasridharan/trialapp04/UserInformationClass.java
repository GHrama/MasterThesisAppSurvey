package com.example.ramapriyasridharan.trialapp04;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by ramapriyasridharan on 10.03.16.
 */
public class UserInformationClass extends GenericJson {

    // used to denote which field goes into which column of the kinvey table
    @Key("_UserId")
    private String user_id;
    @Key("UserFirstName")
    private String user_first_name;
    @Key("UserSex")
    private String user_sex;

    public UserInformationClass(){}

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name = user_first_name;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_first_name() {
        return user_first_name;
    }

    public String getUser_sex() {
        return user_sex;
    }
}






