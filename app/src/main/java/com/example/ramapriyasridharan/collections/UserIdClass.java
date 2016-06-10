package com.example.ramapriyasridharan.collections;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by ramapriyasridharan on 10.06.16.
 */
public class UserIdClass extends GenericJson {

    @Key("creator")
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
