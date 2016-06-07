package com.example.ramapriyasridharan.StoreValues;

import com.example.ramapriyasridharan.collections.UserResponseClass;
import com.kinvey.java.User;

/**
 * Created by ramapriyasridharan on 03.06.16.
 * Used for UserResponseTableCache
 */
public class UserResponseTableStore {

    public UserResponseClass ur;
    public int key;
    public int is_sent;

    public UserResponseTableStore(UserResponseClass ur,int key,int is_sent){
        this.ur = ur;
        this.key = key;
        this.is_sent = is_sent;
    }
}
