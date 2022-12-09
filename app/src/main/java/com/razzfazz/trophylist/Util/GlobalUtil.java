package com.razzfazz.trophylist.Util;

public class GlobalUtil {

    // converts given minutes to hours (int)
    public static double convertMinToH(int minutes) {
        if(minutes < 60) {
            return minutes/60.0;
        }
        int hours = minutes/60;
        return hours + (minutes%60)/60.0;
    }

    // converts given minutes to hours (double)
    public static double convertMinToH(double minutes) {
        if(minutes < 60) {
            return minutes/60.0;
        }
        int hours = (int)minutes/60;
        return hours + (minutes%60)/60.0;
    }
}
