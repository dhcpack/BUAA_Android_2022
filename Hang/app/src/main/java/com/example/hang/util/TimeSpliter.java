package com.example.hang.util;

public class TimeSpliter {
    public static String splitTime(String s) {
        String[] strings = s.split("T");
        s = strings[0];
        strings = s.split("-");
        s = strings[0] + "年" + strings[1] + "月" + strings[2] + "日";
        return s;
    }
}
