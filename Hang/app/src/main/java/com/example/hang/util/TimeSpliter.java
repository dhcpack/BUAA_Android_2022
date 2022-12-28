package com.example.hang.util;

public class TimeSpliter {
    public static String getDate(String s) {
        String[] strings = s.split("T");
        s = strings[0];
        strings = s.split("-");
        s = strings[0] + "年" + strings[1] + "月" + strings[2] + "日";
        return s;
    }

    public static String getDateTime(String s) {
        String date = getDate(s);
        String[] strings = s.split("T");
        String time = strings[1];
        strings = time.split("/.");
        return date + " " + strings[0];
    }
}
