package com.example.hang.ui.commu.posts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
            post_detail_tag.setText(jsonObject.getString("tag") + TimeSpliter.getDate(jsonObject.getString("time")));
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
                        favor += 1;
                        HashMap<String, String> body = new HashMap<>();
                        body.put("nickname", writer);
                        body.put("favor", String.valueOf(favor));
                        JSONObject ret = HttpUtil.httpPut(Ports.modifyPostUrl + postId + "/", body);
                        System.out.println(ret.toString());
                        post_detail_favor.setText(String.valueOf(favor));
                        @SuppressLint("UseCompatLoadingForDrawables") Drawable favored = getResources().getDrawable(R.drawable.ic_iconmonstr_thumb_9);
                        favored.setBounds(0, 0, favored.getMinimumWidth(), favored.getMinimumHeight());
                        post_detail_favor.setCompoundDrawables(favored, null, null, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    favored = true;
                } else {
                    try {
                        favor -= 1;
                        HashMap<String, String> body = new HashMap<>();
                        body.put("nickname", writer);
                        body.put("favor", String.valueOf(favor));
                        JSONObject ret = HttpUtil.httpPut(Ports.modifyPostUrl + postId + "/", body);
                        System.out.println(ret.toString());
                        post_detail_favor.setText(String.valueOf(favor));
                        @SuppressLint("UseCompatLoadingForDrawables") Drawable favored = getResources().getDrawable(R.drawable.ic_iconmonstr_thumb_10);
                        favored.setBounds(0, 0, favored.getMinimumWidth(), favored.getMinimumHeight());
                        post_detail_favor.setCompoundDrawables(favored, null, null, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    favored = false;
                }
            }
        });

        List<Map<String, Object>> list = getData();

        //2.适配器，刚刚重写的！
        CommentAdapter commentAdapter = new CommentAdapter(this, list);
        //3.设置适配器
        ListView comment_list = findViewById(R.id.post_detail_comments);
        comment_list.setAdapter(commentAdapter);
        comment_list.smoothScrollBy(30, 200);

        EditText editText = findViewById(R.id.ed_comment);
        Button comment_button = findViewById(R.id.btn_comment);
        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject response = null;
                HashMap<String, String> body = new HashMap<>();
                body.put("content", editText.getText().toString());
                body.put("nickname", username);
                body.put("postId", String.valueOf(postId));
                try {
                    response = HttpUtil.httpPost(Ports.addCommentUrl, body);
                    Toast.makeText(PostDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    editText.setText("已评论");
                    editText.clearFocus();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //填充数据
    public List<Map<String, Object>> getData() {
        ArrayList<String> param = new ArrayList<>();
        param.add(String.valueOf(postId));
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.searchCommentUrl, param, true);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                System.out.println(jsonObject.toString());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    map.put("commenter", jsonObject.getString("nickname"));
                    map.put("content", jsonObject.getString("content"));
                    map.put("time", jsonObject.getString("time"));
                    list.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    public static class CommentAdapter extends BaseAdapter {

        private final List<Map<String, Object>> data;
        private final LayoutInflater layoutInflater;
        private final Context context;

        public CommentAdapter(Context context, List<Map<String, Object>> data) {
            //传入的data，就是我们要在listview中显示的信息
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }

        //这里定义了一个类，用来表示一个item里面包含的东西
        public static class Info {
            private TextView tv_comment_user;
            private TextView tv_comment_content;
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
        @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            CommentAdapter.Info info = new CommentAdapter.Info();
            convertView = layoutInflater.inflate(R.layout.comment_layout, null);
            info.tv_comment_user = convertView.findViewById(R.id.tv_comment_user);
            info.tv_comment_content = convertView.findViewById(R.id.tv_comment_content);

            //设置数据
            String s_c = ((String) data.get(position).get("commenter"));
            String s_t = TimeSpliter.getDateTime(((String) data.get(position).get("time")));
            String s = s_c + "    " + s_t;
            System.out.println(s);
            info.tv_comment_user.setText(s);
            info.tv_comment_content.setText((String) data.get(position).get("content"));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}