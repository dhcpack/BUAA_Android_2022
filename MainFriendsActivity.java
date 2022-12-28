package com.example.hang.ui.commu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.hang.MainActivity;
import com.example.hang.R;
import com.example.hang.database.UserHelper;

public class MainFriendsActivity extends AppCompatActivity {//默认使用者id为0
    private Button friendlist;//好友列表界面
    private Button addfriends;//添加好友界面
    private Button friendrequest;//好友申请界面
    private Button friendadd;//已请求的好友申请
    private String friendid;//获取的朋友id(名字）
    private String myid;//自身id(名字）
    private EditText friendid_input;//输入朋友id搜索
    private ImageFilterView back;//返回界面
    private static UserHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.fragment_communicate);
        back = findViewById(R.id.Return);
        addfriends = findViewById(R.id.addfriend);
        friendlist = findViewById(R.id.friendlist);
        friendrequest = findViewById(R.id.friendrequest);
        friendadd = findViewById(R.id.friendadd);
        addfriends.setOnClickListener(this);
        friendlist.setOnClickListener(this);
        friendrequest.setOnClickListener(this);
        friendadd.setOnClickListener(this);
        back.setOnClickListener(this);*/
    }


    /*public void onClick(View view) {
        if (view.getId() == R.id.addfriend)
            startActivity(new Intent(MainFriendsActivity.this, com.example.hang.ui.commu.AddFriends.class));
        if (view.getId() == R.id.friendlist)
            startActivity(new Intent(this, com.example.hang.ui.commu.FriendList.class));
        if (view.getId() == R.id.friendrequest)
            startActivity(new Intent(this, com.example.hang.ui.commu.Friendrequestlist.class));
        if (view.getId() == R.id.friendadd)
            startActivity(new Intent(this, com.example.hang.ui.commu.FriendaddList.class));
    }*/
}