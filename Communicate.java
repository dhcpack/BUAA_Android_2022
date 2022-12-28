package com.example.hang.ui.commu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.hang.R;
import com.example.hang.ui.mine.Settings.SettingsActivity;

public class Communicate extends Fragment implements View.OnClickListener {

    private CommunicateViewModel mViewModel;
    private String name;

    public static Communicate newInstance() {
        return new Communicate();
    }

    private Button addfriends;//添加好友界面
    private Button friendrequest;//好友申请界面
    private Button friendadd;//已请求的好友申请
    private String friendid;//获取的朋友id(名字）
    private Button friendlist;
    private String myid;//自身id(名字）
    private EditText friendid_input;//输入朋友id搜索
    private ImageFilterView back;//返回界面
    private Intent intent;
    private FragmentActivity a;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communicate, container, false);
        a=getActivity();
        Bundle bundle = a.getIntent().getExtras();
        name = bundle.getString("username");
        addfriends = view.findViewById(R.id.addfriend);
        //好友列表界面
        friendlist = view.findViewById(R.id.friendlist);
        friendrequest = view.findViewById(R.id.friendrequest);
        friendadd = view.findViewById(R.id.friendadd);
        addfriends.setOnClickListener(this);
        friendlist.setOnClickListener(this);
        friendrequest.setOnClickListener(this);
        friendadd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CommunicateViewModel.class);
        // TODO: Use the ViewModel
    }

    public void onClick(View view) {
        if (view.getId() == R.id.addfriend) {
            intent = new Intent(a, com.example.hang.ui.commu.AddFriends.class);
            intent.putExtra("username", name);
            startActivity(intent);
        }
        if (view.getId() == R.id.friendlist) {
            intent = new Intent(a, com.example.hang.ui.commu.FriendList.class);
            intent.putExtra("username", name);
            startActivity(intent);
        }
        if (view.getId() == R.id.friendrequest)
        {
            intent = new Intent(a, com.example.hang.ui.commu.Friendrequestlist.class);
            intent.putExtra("username", name);
            startActivity(intent);
        }

        if (view.getId() == R.id.friendadd) {
            intent = new Intent(a, com.example.hang.ui.commu.FriendaddList.class);
            intent.putExtra("username", name);
            startActivity(intent);
        }
    }
}