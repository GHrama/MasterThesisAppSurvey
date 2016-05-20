package com.example.ramapriyasridharan.StoreValues;

import java.text.MessageFormat;

/**
 * Created by ramapriyasridharan on 17.05.16.
 */
public class Questions {
    public static MessageFormat question = new MessageFormat("The Data Collector {1} would like to get your {0} sensor data for the purpose of {2}.\nPlease choose one of the following data sharing levels :");


    /**
     * TO DO
     * makes the underscores into spaces
     * and makes the first letter of each word capital (Presentation)
     * @param s
     * @return
     */
    static public String refineString(String s){
        s  = s.replaceAll("_", " ").toLowerCase();
        String[] words = s.split(" ");
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < words.length; i++) {
            ret.append(Character.toUpperCase(words[i].charAt(0)));
            ret.append(words[i].substring(1));
            if(i < words.length - 1) {
                ret.append(' ');
            }
        }
        return ret.toString();
    }

    public static String returnQuestion(String a, String b, String c){
        Object[] args = {refineString(a),refineString(b),refineString(c)};
        String q = question.format(args);
        return q;
    }


}
