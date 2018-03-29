package com.example.helm.hw3;

import java.util.Calendar;

public class DateUtil {

    public static String getToday(){
        Calendar calendar = Calendar.getInstance();
        StringBuilder sbDate = new StringBuilder();

        String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1); // List of months start with 0

        sbDate.append(calendar.get(Calendar.YEAR)).append("-");
        sbDate.append(month).append("-");
        sbDate.append(day);

        return sbDate.toString();
    }

}
