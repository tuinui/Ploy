package com.nos.ploy.utils;

/**
 * Created by Saran on 4/2/2560.
 */

public class LanguageTextUtils {

    public static String getPloyeeCountDisplay(long count,String suffix) {
        String ployeeCounts = "0 "+suffix;
        if (count == 1) {
            ployeeCounts = "1 "+suffix;
        } else if (count > 1) {
            ployeeCounts = count + " "+suffix;
        }
        return ployeeCounts;
    }
}
