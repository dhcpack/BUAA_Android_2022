package com.example.hang.ui.learn.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    @SuppressLint("SimpleDateFormat")
    public static String getNowTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return sdf.format(new Date());
    }
}
