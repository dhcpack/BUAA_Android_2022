package com.example.hang.ui.commu.friends;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;


public class FriendManagementActivity extends AppCompatActivity {
    private String username;
    private int curr = 0;
    private ListView friends_list;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_management);
        context = this;

        username = getIntent().getExtras().getString("username");
        List<Map<String, Object>> friendsList = getFriendsData();
        List<Map<String, Object>> applicantList = getApplicantData();

        //2.适配器，刚刚重写的！
        FriendsAdapter friendsAdapter = new FriendsAdapter(this, friendsList, username);
        ApplicationAdapter applicationAdapter = new ApplicationAdapter(this, applicantList, username);

        //3.设置适配器
        friends_list = findViewById(R.id.friends_list);
        friends_list.setAdapter(friendsAdapter);
        friends_list.smoothScrollBy(30, 200);
        setTitleBar("好友列表");

        Button btn_all_friend_list = findViewById(R.id.btn_all_friend_list);
        Button btn_friend_apply_list = findViewById(R.id.btn_friend_apply_list);
        btn_all_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curr == 0) {
                    return;
                }
                curr = 0;
                setTitleBar("好友列表");
                friends_list.setAdapter(friendsAdapter);
                friends_list.smoothScrollBy(30, 200);
                Toast.makeText(context, "切换到好友列表", Toast.LENGTH_SHORT).show();

            }
        });

        btn_friend_apply_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curr == 1) {
                    return;
                }
                curr = 1;
                setTitleBar("申请列表");
                friends_list.setAdapter(applicationAdapter);
                friends_list.smoothScrollBy(30, 200);
                Toast.makeText(context, "切换申请列表", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //填充数据
    public List<Map<String, Object>> getFriendsData() {
        ArrayList<String> param = new ArrayList<>();
        param.add(username);
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getAllFriendsUrl, param, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if (jsonObject.getBoolean("sex") == true) {
                        map.put("friend_info_pic", R.drawable.ic_default_avatar_male);
                    } else {
                        map.put("friend_info_pic", R.drawable.ic_default_avatar_female);
                    }
                    map.put("info_nickname", jsonObject.getString("nickname"));
                    map.put("friend_stu_id", jsonObject.getString("stuId"));
                    map.put("friend_institute_grade", jsonObject.getString("institute") + " " + jsonObject.getString("grade"));
                    map.put("friend_time", TimeSpliter.getDate((String) jsonObject.getString("time")));
                    list.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static class FriendsAdapter extends BaseAdapter {

        private final List<Map<String, Object>> data;
        private final LayoutInflater layoutInflater;
        private final Context context;
        private final String username;

        public FriendsAdapter(Context context, List<Map<String, Object>> data, String username) {
            //传入的data，就是我们要在listview中显示的信息
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
            this.username = username;
        }

        //这里定义了一个类，用来表示一个item里面包含的东西
        public static class Info {
            private AppCompatImageView friend_pic;
            private TextView friend_nickname;
            private TextView friend_stu_id;
            private TextView friend_institute_grade;
            private TextView friend_time;
            private Button friend_op;
        }

        //所有要返回的东西的数量（Id、信息等），都在data里面，从data里面取就好
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        //跟activity中的onCreate()差不多，目的就是给item布局中的各个控件对应好，并添加数据
        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            FriendsAdapter.Info info = new FriendsAdapter.Info();
            convertView = layoutInflater.inflate(R.layout.friend_info_layout, null);
            info.friend_pic = convertView.findViewById(R.id.friend_info_pic);
            info.friend_nickname = convertView.findViewById(R.id.info_nickname);
            info.friend_stu_id = convertView.findViewById(R.id.friend_stu_id);
            info.friend_institute_grade = convertView.findViewById(R.id.friend_institute_grade);
            info.friend_time = convertView.findViewById(R.id.friend_time);
            info.friend_op = convertView.findViewById(R.id.friend_op);


            //设置数据
            info.friend_pic.setImageResource((Integer) data.get(position).get("friend_info_pic"));
            info.friend_nickname.setText((String) data.get(position).get("info_nickname"));
            info.friend_stu_id.setText("学号: " + (String) data.get(position).get("friend_stu_id"));
            info.friend_institute_grade.setText("学院年级: " + (String) data.get(position).get("friend_institute_grade"));
            info.friend_time.setText("成为好友时间: " + (String) data.get(position).get("friend_time"));
            info.friend_op.setText("删除好友");

            info.friend_op.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> params = new ArrayList<>();
                    params.add(username);
                    params.add((String) data.get(position).get("info_nickname"));
                    try {
                        JSONObject jsonObject = HttpUtil.httpDelete(Ports.deleteFriendUrl, params);
                        System.out.println(jsonObject.toString());
                        Toast.makeText(context, "成功删除好友", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return convertView;
        }

        private void toast(String str) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }


    //填充数据
    public List<Map<String, Object>> getApplicantData() {
        ArrayList<String> param = new ArrayList<>();
        param.add(username);
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getAllApplicantsUrl, param, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if (jsonObject.getBoolean("sex") == true) {
                        map.put("friend_info_pic", R.drawable.ic_default_avatar_male);
                    } else {
                        map.put("friend_info_pic", R.drawable.ic_default_avatar_female);
                    }
                    map.put("info_nickname", jsonObject.getString("nickname"));
                    map.put("friend_stu_id", jsonObject.getString("stuId"));
                    map.put("friend_apply_message", jsonObject.getString("msg"));
                    map.put("friend_time", TimeSpliter.getDateTime((String) jsonObject.getString("time")));
                    list.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static class ApplicationAdapter extends BaseAdapter {
        private final List<Map<String, Object>> data;
        private final LayoutInflater layoutInflater;
        private final Context context;
        private final String username;

        public ApplicationAdapter(Context context, List<Map<String, Object>> data, String username) {
            //传入的data，就是我们要在listview中显示的信息
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
            this.username = username;
        }

        //这里定义了一个类，用来表示一个item里面包含的东西
        public static class Info {
            private AppCompatImageView friend_pic;
            private TextView friend_nickname;
            private TextView friend_stu_id;
            private TextView friend_institute_grade;
            private TextView friend_time;
            private Button friend_op;

        }

        //所有要返回的东西的数量（Id、信息等），都在data里面，从data里面取就好
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        //跟activity中的onCreate()差不多，目的就是给item布局中的各个控件对应好，并添加数据
        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            FriendsAdapter.Info info = new FriendsAdapter.Info();
            convertView = layoutInflater.inflate(R.layout.friend_info_layout, null);
            info.friend_pic = convertView.findViewById(R.id.friend_info_pic);
            info.friend_nickname = convertView.findViewById(R.id.info_nickname);
            info.friend_stu_id = convertView.findViewById(R.id.friend_stu_id);
            info.friend_institute_grade = convertView.findViewById(R.id.friend_institute_grade);
            info.friend_time = convertView.findViewById(R.id.friend_time);
            info.friend_op = convertView.findViewById(R.id.friend_op);


            //设置数据
            info.friend_pic.setImageResource((Integer) data.get(position).get("friend_info_pic"));
            info.friend_nickname.setText((String) data.get(position).get("info_nickname"));
            info.friend_stu_id.setText("学号: " + (String) data.get(position).get("friend_stu_id"));
            info.friend_institute_grade.setText("申请信息: " + (String) data.get(position).get("friend_apply_message"));
            info.friend_time.setText("申请时间: " + (String) data.get(position).get("friend_time"));

            info.friend_op.setText("接受申请");

            info.friend_op.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> body = new HashMap<>();
                    body.put("receiver", username);
                    body.put("sender", (String) data.get(position).get("info_nickname"));
                    try {
                        JSONObject jsonObject = HttpUtil.httpPost(Ports.acceptRequestUrl, body);
                        System.out.println(jsonObject.toString());
                        Toast.makeText(context, "成功添加好友", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return convertView;
        }

        private void toast(String str) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
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