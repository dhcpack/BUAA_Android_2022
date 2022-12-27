package com.example.hang.ui.learn.util;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ListBean implements Serializable {
    private JSONObject jo;
    private int id;
    private int type;
    private String ques;
    private String ans1;
    private String ans2;
    private String ans3;
    private String ans4;
    private int review;
    private String next_time;
    private String nickname;
    private int book; //book id


    public ListBean(JSONObject jo) {
        if (this.jo == null) {
            System.out.println("nulllllll");
        }
        this.jo = jo;
    }

    public ListBean(int id, int type, String ques, String ans1, String ans2,String ans3,
                    String ans4, int review, String next_time, String nickname, int book) {
        this.id = id;
        this.type = type;
        this.ques = ques;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.review = review;
        this.next_time = next_time;
        this.nickname = nickname;
        this.book  = book;
    }

    protected ListBean(Parcel in) {
    }


    /*public String getString(String s) throws JSONException {
        return jo.getString(s);
    }

    public int getInt(String s) throws JSONException {
        return jo.getInt(s);
    }*/

    public int getType() {
        return type;
    }

    public String getQues() {
        return ques;
    }

    public int getId() {
        return id;
    }

    public String getAns1() {
        return ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public String getAns4() {
        return ans4;
    }

    public String getAnsByType() {
        if (type == 1) {
            return ans1;
        } else if (type == 2) {
            return ans2;
        } else if (type == 3) {
            return ans3;
        } else {
            return ans4;
        }
    }

    public int getBook() {
        return book;
    }

    public int getReview() {
        return review;
    }

    public String getNext_time() {
        return next_time;
    }

    public String getNickname() {
        return nickname;
    }

    public JSONObject getJo() {
        return jo;
    }


}
