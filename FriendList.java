package com.example.hang.ui.commu;

import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FriendList extends AppCompatActivity implements View.OnClickListener {
    private ImageFilterView back;//返回界面
    private String username;
    private ArrayList<String> friends = new ArrayList<>();
    private LinearLayout list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        back = findViewById(R.id.Return);
        back.setOnClickListener(this);
        try {
            ArrayList<String> us = new ArrayList<>();
            us.add(username);
            JSONArray userInfo = (JSONArray) HttpUtil.httpGet(Ports.getFriendsUrl, us, true);
            for (int i = 0; i < userInfo.length(); i++) {
                JSONObject o = userInfo.getJSONObject(i);
                String id = o.getString("nickname1");
                friends.add(id);
            }
            friends.add("1");
            friends.add("2");
            friends.add("3");
            friends.add("4");
            friends.add("5");
            friends.add("6");
            list = findViewById(R.id.list);
            setfriends();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setfriends() {
        for (String id : friends) {
            View view = LayoutInflater.from(this).inflate(R.layout.friendlistunit, null);
            TextView name = view.findViewById(R.id.friendid1);
            Button b1 = view.findViewById(R.id.daka);
            b1.setOnClickListener(this);
            Button b2 = view.findViewById(R.id.share);
            b2.setOnClickListener(this);
            Button b3 = view.findViewById(R.id.get);
            b3.setOnClickListener(this);
            Button b4 = view.findViewById(R.id.delete);
            b4.setOnClickListener(this);
            name.setText(id);
            list.addView(view);
        }
    }

    public void ducudaka(View view) {
        /*try {
            HashMap<String, String> context = new HashMap<>();
            TextView id = view.findViewById(R.id.friendid1);
            String name = (String) id.getText();
            context.put("nickname", name);
            JSONObject userInfo = (JSONObject) HttpUtil.httpPost(Ports.checkUrl, context);//应为urgecheckUrl
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Toast toast;
        toast = Toast.makeText(this, "督促好友打卡成功!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void delete(View view) {
        try {
            ArrayList<String> us = new ArrayList<>();
            TextView id = view.findViewById(R.id.friendid1);
            String name = (String) id.getText();
            us.add(username);
            us.add(name);
            JSONObject userInfo = (JSONObject) HttpUtil.httpDelete(Ports.deleteFriendUrl, us);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast toast;
        toast = Toast.makeText(this, "成功删除好友!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void share(View view) {

    }

    public void get(View view) {

    }

    public void onClick(View view) {
        if (view.getId() == R.id.Return)
            finish();
        if (view.getId() == R.id.daka)
            ducudaka(view);
        if (view.getId() == R.id.share)
            share(view);
        if (view.getId() == R.id.get)
            get(view);
        if (view.getId() == R.id.delete)
            delete(view);
    }
}