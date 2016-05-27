package com.example.ramapriyasridharan.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by ramapriyasridharan on 25.05.16.
 */
public class Round {


    // round to number of places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
