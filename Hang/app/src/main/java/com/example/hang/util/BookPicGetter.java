package com.example.hang.util;

import com.example.hang.R;

public class BookPicGetter {
    public static Object getBookPic(String bookname) {
        if (bookname.equals("大学英语六级词汇（乱序版）")) {
            return R.drawable.cet6;
        } else if (bookname.equals("大学英语六级词汇（正序版）")) {
            return R.drawable.cet6;
        } else if (bookname.equals("大学英语四级词汇（正序版）")) {
            return R.drawable.cet4;
        } else if (bookname.equals("大学英语四级词汇（乱序版）")) {
            return R.drawable.cet4;
        } else if (bookname.equals("航概文科题库") || bookname.equals("航概理科题库")) {
            return R.drawable.hg;
        } else {
            return R.drawable.ic_book2;
        }
    }
}
