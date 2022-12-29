package com.example.hang.ui.commu.friends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.commu.posts.AddPostActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsListActivity extends AppCompatActivity {
    private String username;
    private int curr = 0;
    private ListView friends_rank_list;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        context = this;

        username = getIntent().getExtras().getString("username");
        List<Map<String, Object>> checkList = getCheckData();
        List<Map<String, Object>> processList = getProcessData();

        //2.适配器，刚刚重写的！
        CheckAdapter checkAdapter = new CheckAdapter(this, checkList, username);
        ProcessAdapter processAdapter = new ProcessAdapter(this, processList, username);

        //3.设置适配器
        friends_rank_list = findViewById(R.id.friends_rank);
        friends_rank_list.setAdapter(checkAdapter);
        friends_rank_list.smoothScrollBy(30, 200);
        setTitleBar("打卡天数排行榜");


        Button btn_friend_check_list = findViewById(R.id.btn_friend_check_list);
        Button btn_friend_process_list = findViewById(R.id.btn_friend_process_list);
        btn_friend_check_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curr == 0) {
                    return;
                }
                curr = 0;
                setTitleBar("打卡天数排行榜");
                friends_rank_list.setAdapter(checkAdapter);
                friends_rank_list.smoothScrollBy(30, 200);
                Toast.makeText(FriendsListActivity.this, "切换到打卡天数排行榜", Toast.LENGTH_SHORT).show();
            }
        });

        btn_friend_process_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curr == 1) {
                    return;
                }
                curr = 1;
                setTitleBar("学习进度排行榜");
                friends_rank_list.setAdapter(processAdapter);
                friends_rank_list.smoothScrollBy(30, 200);
                Toast.makeText(FriendsListActivity.this, "切换到学习进度排行榜", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView = findViewById(R.id.tv_friend_management);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendManagementActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    //填充数据
    public List<Map<String, Object>> getCheckData() {
        ArrayList<String> param = new ArrayList<>();
        param.add(username);
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getCheckListUrl, param, true);
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
                        map.put("friend_check_pic", R.drawable.ic_default_avatar_male);
                    } else {
                        map.put("friend_check_pic", R.drawable.ic_default_avatar_female);
                    }
                    if (jsonObject.getString("nickname").equals(username)) {
                        map.put("nickname", jsonObject.getString("nickname"));
                    } else {
                        map.put("nickname", jsonObject.getString("nickname"));
                    }
                    map.put("days", jsonObject.getInt("days"));
                    map.put("rank", jsonObject.getInt("rank"));
                    list.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static class CheckAdapter extends BaseAdapter {

        private final List<Map<String, Object>> data;
        private final LayoutInflater layoutInflater;
        private final Context context;
        private final String username;

        public CheckAdapter(Context context, List<Map<String, Object>> data, String username) {
            //传入的data，就是我们要在listview中显示的信息
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
            this.username = username;
        }

        //这里定义了一个类，用来表示一个item里面包含的东西
        public static class Info {
            private AppCompatImageView friend_check_pic;
            private TextView check_nickname;
            private TextView check_time;
            private TextView check_rank;
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
            CheckAdapter.Info info = new CheckAdapter.Info();
            convertView = layoutInflater.inflate(R.layout.friend_check_layout, null);
            info.friend_check_pic = convertView.findViewById(R.id.friend_check_pic);
            info.check_nickname = convertView.findViewById(R.id.check_nickname);
            info.check_time = convertView.findViewById(R.id.check_time);
            info.check_rank = convertView.findViewById(R.id.check_rank);


            //设置数据
            info.friend_check_pic.setImageResource((Integer) data.get(position).get("friend_check_pic"));
            info.check_nickname.setText((String) data.get(position).get("nickname"));
            info.check_time.setText("打卡天数: " + String.valueOf((Integer) data.get(position).get("days")));
            info.check_rank.setText("好友排名: " + String.valueOf((Integer) data.get(position).get("rank")));
            if (((String) data.get(position).get("nickname")).equals(username)) {
                info.check_nickname.setTextColor(Color.parseColor("#CC00FF"));
                info.check_time.setTextColor(Color.parseColor("#CC00FF"));
                info.check_rank.setTextColor(Color.parseColor("#CC00FF"));
            }
            return convertView;
        }

        private void toast(String str) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }

    //填充数据
    public List<Map<String, Object>> getProcessData() {
        ArrayList<String> param = new ArrayList<>();
        param.add(username);
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getProcessListUrl, param, true);
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
                        map.put("friend_process_pic", R.drawable.ic_default_avatar_male);
                    } else {
                        map.put("friend_process_pic", R.drawable.ic_default_avatar_female);
                    }
                    map.put("nickname", jsonObject.getString("nickname"));
                    map.put("progress", jsonObject.getDouble("process"));
                    map.put("rank", jsonObject.getInt("rank"));
                    list.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static class ProcessAdapter extends BaseAdapter {

        private final List<Map<String, Object>> data;
        private final LayoutInflater layoutInflater;
        private final Context context;
        private final String username;

        public ProcessAdapter(Context context, List<Map<String, Object>> data, String username) {
            //传入的data，就是我们要在listview中显示的信息
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
            this.username = username;
        }

        //这里定义了一个类，用来表示一个item里面包含的东西
        public static class Info {
            private AppCompatImageView friend_process_pic;
            private TextView process_nickname;
            private TextView process_value;
            private ProgressBar progress_friend_bar;
            private TextView process_rank;
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
            ProcessAdapter.Info info = new ProcessAdapter.Info();
            convertView = layoutInflater.inflate(R.layout.friend_process_layout, null);
            info.friend_process_pic = convertView.findViewById(R.id.friend_process_pic);
            info.process_nickname = convertView.findViewById(R.id.process_nickname);
            info.process_value = convertView.findViewById(R.id.process_value);
            info.progress_friend_bar = convertView.findViewById(R.id.progress_friend_bar);
            info.process_rank = convertView.findViewById(R.id.process_rank);


            //设置数据
            info.friend_process_pic.setImageResource((Integer) data.get(position).get("friend_process_pic"));
            info.process_nickname.setText((String) data.get(position).get("nickname"));
            info.process_value.setText("学习进度: " + String.format("%.2f", (double) data.get(position).get("progress") * 100) + "%");
            info.progress_friend_bar.setProgress((int) ((double) data.get(position).get("progress") * 100));
            info.progress_friend_bar.setMax(100);
            info.process_rank.setText("好友排名: " + String.valueOf((Integer) data.get(position).get("rank")));
            if (((String) data.get(position).get("nickname")).equals(username)) {
                info.process_nickname.setTextColor(Color.parseColor("#CC00FF"));
                info.process_value.setTextColor(Color.parseColor("#CC00FF"));
                info.process_rank.setTextColor(Color.parseColor("#CC00FF"));
            }

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