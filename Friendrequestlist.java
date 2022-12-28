package com.example.hang.ui.commu;

import android.os.Bundle;
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

public class Friendrequestlist extends AppCompatActivity implements View.OnClickListener {
    private ImageFilterView back;//返回界面
    private String username;
    private ArrayList<String> friends = new ArrayList<>();
    private LinearLayout list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendrequestlist);
        back = findViewById(R.id.Return);
        back.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        try {
            ArrayList<String> us = new ArrayList<>();
            us.add(username);
            us.add("receive");
            JSONArray userInfo = (JSONArray) HttpUtil.httpGet(Ports.requestMeUrl, us, true);
            for (int i = 0; i < userInfo.length(); i++) {
                JSONObject o = userInfo.getJSONObject(i);
                String id = o.getString("sender");
                friends.add(id);
            }
            friends.add("1");
            friends.add("2");
            list = findViewById(R.id.list3);
            setfriends();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setfriends() {
        for (String id : friends) {
            View view = LayoutInflater.from(this).inflate(R.layout.friendunit3, null);
            TextView name = view.findViewById(R.id.id3);
            Button b1 = view.findViewById(R.id.agree);
            b1.setOnClickListener(this);
            Button b2 = view.findViewById(R.id.refuse);
            b2.setOnClickListener(this);
            Button b3 = view.findViewById(R.id.delete3);
            b3.setOnClickListener(this);
            name.setText(id);
            list.addView(view);
        }
    }

    public void agree(View view) {
        try {
            TextView id = view.findViewById(R.id.id3);
            String name = (String) id.getText();
            HashMap<String, String> context = new HashMap<>();
            context.put("sender",name);
            context.put("receiver",username);
            JSONObject userInfo = (JSONObject) HttpUtil.httpPost(Ports.acceptRequestUrl, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast toast;
        toast = Toast.makeText(this, "接受好友申请!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.Return)
            finish();
        if (view.getId() == R.id.agree)
            agree(view);
        if (view.getId() == R.id.delete3 || view.getId() == R.id.refuse)
            delete(view);
    }

    public void delete(View view) {
        try {
            ArrayList<String> us = new ArrayList<>();
            TextView id = view.findViewById(R.id.id3);
            String name = (String) id.getText();
            us.add(username);
            us.add(name);
            JSONObject userInfo = (JSONObject) HttpUtil.httpDelete(Ports.deleteFriendUrl, us);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast toast;
        toast = Toast.makeText(this, "拒绝添加好友!", Toast.LENGTH_SHORT);
        toast.show();
    }


}