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

public class FriendaddList extends AppCompatActivity implements View.OnClickListener {
    private ImageFilterView back;//返回界面
    private String username;
    private ArrayList<String> friends = new ArrayList<>();
    private LinearLayout list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendadd_list);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        back = findViewById(R.id.Return);
        back.setOnClickListener(this);
        try {
            ArrayList<String> us = new ArrayList<>();
            us.add(username);
            us.add("send");
            JSONArray userInfo = (JSONArray) HttpUtil.httpGet(Ports.requestOtherUrl, us, true);
            for (int i = 0; i < userInfo.length(); i++) {
                JSONObject o = userInfo.getJSONObject(i);
                String id = o.getString("receiver");
                friends.add(id);
            }
            friends.add("1");
            friends.add("2");
            list = findViewById(R.id.list2);
            setfriends();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setfriends() {
        for (String id : friends) {
            View view = LayoutInflater.from(this).inflate(R.layout.friendunit2, null);
            TextView name = view.findViewById(R.id.id1);
            TextView context = view.findViewById(R.id.get1);
            Button b = view.findViewById(R.id.delete1);
            b.setOnClickListener(this);
            name.setText(id);
            list.addView(view);
        }
    }

    public void delete(View view) {
        try {
            ArrayList<String> us = new ArrayList<>();
            TextView id = view.findViewById(R.id.id1);
            String name = (String) id.getText();
            us.add(username);
            us.add(name);
            JSONObject userInfo = (JSONObject) HttpUtil.httpDelete(Ports.deleteFriendUrl, us);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast toast;
        toast = Toast.makeText(this, "成功删除!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.delete1)
            delete(view);
        if (view.getId() == R.id.Return)
            finish();
    }
}