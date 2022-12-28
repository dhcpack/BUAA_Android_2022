package com.example.hang.ui.commu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.example.hang.R;
import com.example.hang.cl.User;
import com.example.hang.database.UserHelper;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddFriends extends AppCompatActivity implements View.OnClickListener {
    private ImageFilterView back;//返回界面
    private Button add;//添加好友
    private EditText search;//搜索的好友id
    private static UserHelper helper;
    private String username;//个人名字
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        Bundle bundle=getIntent().getExtras();
        String pid=bundle.getString("username");
        back = findViewById(R.id.Return);
        add = findViewById(R.id.addfriend);
        search = findViewById(R.id.select);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    protected void onStart() {
        super.onStart();
        helper = UserHelper.getInstance(this);
        helper.openwrite();
        helper.openread();
    }

    /*protected void onStop() {
        super.onStop();
        helper.close();
    }*/

    public void onClick(View view) {
        if (view.getId() == R.id.Return)
            finish();
        if (view.getId() == R.id.addfriend) {
            if (!search.getText().toString().equals("")) {
                String id = search.getText().toString();
                try {
                    HashMap<String, String> context = new HashMap<>();
                    context.put("sender",username);
                    context.put("receiver",id);
                    JSONObject userInfo = (JSONObject) HttpUtil.httpPost(Ports.postRequestUrl,context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast toast;
                toast = Toast.makeText(AddFriends.this, "成功申请添加好友!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast;
                toast = Toast.makeText(AddFriends.this, "请输入好友id!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}