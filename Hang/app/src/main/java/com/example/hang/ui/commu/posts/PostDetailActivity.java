package com.example.hang.ui.commu.posts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.util.TimeSpliter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class PostDetailActivity extends AppCompatActivity {
    private String username;
    private boolean favored = false;
    private String writer;
    private int postId;
    private int favor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        setTitleBar("帖子详情");
        TextView post_detail_title = findViewById(R.id.post_detail_title);
        TextView post_detail_tag = findViewById(R.id.post_detail_tag);
        TextView post_detail_content = findViewById(R.id.post_detail_content);
        TextView post_detail_favor = findViewById(R.id.post_detail_favor_count);

        username = getIntent().getExtras().getString("username");
        postId = getIntent().getExtras().getInt("post_id");
        ArrayList<String> params = new ArrayList<>();
        params.add("single");
        params.add(String.valueOf(postId));

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) HttpUtil.httpGet(Ports.searchPostUrl, params, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            try {
                jsonObject = (JSONObject) HttpUtil.httpGet(Ports.searchPostUrl, params, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            post_detail_title.setText(jsonObject.getString("title"));
            post_detail_tag.setText(jsonObject.getString("tag") + TimeSpliter.splitTime(jsonObject.getString("time")));
            post_detail_content.setText(jsonObject.getString("content") + "\n");
            post_detail_favor.setText(String.valueOf(jsonObject.getInt("favor")));
            favor = jsonObject.getInt("favor");
            writer = jsonObject.getString("nickname");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        post_detail_favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!favored) {
                    try {
                        HashMap<String, String> body = new HashMap<>();
                        body.put("nickname", writer);
                        body.put("favor", String.valueOf(favor + 1));
                        JSONObject ret = HttpUtil.httpPut(Ports.modifyPostUrl + postId + "/", body);
                        System.out.println(ret.toString());
                        post_detail_favor.setText(String.valueOf(favor + 1));
                        @SuppressLint("UseCompatLoadingForDrawables") Drawable favored = getResources().getDrawable(R.drawable.ic_iconmonstr_thumb_9);
                        favored.setBounds(0, 0, favored.getMinimumWidth(), favored.getMinimumHeight());
                        post_detail_favor.setCompoundDrawables(favored, null, null, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                favored = true;
            }
        });
    }

    public void setTitleBar(String title) {
        //设置顶部菜单栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            TextView tv = new TextView(this);
            tv.setText(title);
            tv.setTextSize(20);
            tv.setTextColor(this.getResources().getColor(R.color.white));
            tv.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
            tv.setGravity(Gravity.CENTER);
            actionBar.setHomeButtonEnabled(true);//设置左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
            actionBar.setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用
            actionBar.setCustomView(tv, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        }
    }
}