package com.example.ramapriyasridharan.helpers;

/**
 * Created by ramapriyasridharan on 17.05.16.
 */
public class Weights {

    public static double getWeights(int rank, int s){
       double w = ((double)1/s)*rank;
        return w;
    }
}
