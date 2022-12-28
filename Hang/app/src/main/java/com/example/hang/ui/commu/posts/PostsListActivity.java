package com.example.hang.ui.commu.posts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

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

public class PostsListActivity extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        setTitleBar("经验贴");

        setPost(this);

        //设置ListView
        //1.给链表添加数据
        username = getIntent().getExtras().getString("username");
        List<Map<String, Object>> list = getData();

        //2.适配器，刚刚重写的！
        PostsAdapter postsAdapter = new PostsAdapter(this, list);
        //3.设置适配器
        ListView post_list = findViewById(R.id.posts);
        post_list.setAdapter(postsAdapter);
        post_list.smoothScrollBy(30, 200);
    }

    public void setPost(Context context) {
        TextView tv_add_post = findViewById(R.id.post_post);
        tv_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPostActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }


    //填充数据
    public List<Map<String, Object>> getData() {
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getPostUrl, new ArrayList<>(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    //map.put("iv_icon_book", jsonObject.getString("pic"));
                    map.put("iv_pic_post", R.drawable.ic_book);
                    map.put("tv_post_title", jsonObject.getString("title"));
                    map.put("tv_post_content", jsonObject.getString("content"));
                    map.put("tv_post_tag", jsonObject.getString("tag"));
                    map.put("tv_post_favor", jsonObject.get("favor"));
                    map.put("post_time", jsonObject.get("time"));
                    map.put("post_id", jsonObject.get("id"));

                    //                    map.put("book_id", jsonObject.getInt("id"));
                    map.put("username", username);
                    list.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    public static class PostsAdapter extends BaseAdapter {

        private final List<Map<String, Object>> data;
        private final LayoutInflater layoutInflater;
        private final Context context;

        public PostsAdapter(Context context, List<Map<String, Object>> data) {
            //传入的data，就是我们要在listview中显示的信息
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }

        //这里定义了一个类，用来表示一个item里面包含的东西
        public static class Info {
            private AppCompatImageView iv_pic_post;
            private TextView tv_post_title;
            private TextView tv_post_tag;
            private TextView tv_favor_count;
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
            PostsListActivity.PostsAdapter.Info info = new PostsAdapter.Info();
            convertView = layoutInflater.inflate(R.layout.posts_layout, null);
            info.iv_pic_post = convertView.findViewById(R.id.post_image);
            info.tv_post_title = convertView.findViewById(R.id.tv_post_title);
            info.tv_post_tag = convertView.findViewById(R.id.tv_post_tag);
            info.tv_favor_count = convertView.findViewById(R.id.favor_count);

            /*
            *       map.put("iv_pic_post", R.drawable.ic_book);
                    map.put("tv_post_title", jsonObject.getString("title"));
                    map.put("tv_post_content", jsonObject.getString("content"));
                    map.put("tv_post_tag", jsonObject.getString("tag"));
                    map.put("tv_post_favor", jsonObject.get("favor"));
            * */

            //设置数据
            info.iv_pic_post.setImageResource((Integer) data.get(position).get("iv_pic_post"));
            info.tv_post_title.setText((String) data.get(position).get("tv_post_title"));
            info.tv_post_tag.setText(((String) data.get(position).get("tv_post_tag")) + " " + TimeSpliter.getDate((String) data.get(position).get("post_time")) + "发布");
            info.tv_favor_count.setText(String.valueOf(data.get(position).get("tv_post_favor")));
            info.tv_post_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    Bundle bundle = new Bundle();
                    Integer post_id = (Integer) data.get(position).get("post_id");
                    bundle.putInt("post_id", post_id);
                    bundle.putString("username", (String) data.get(position).get("username"));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            info.tv_post_tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    Bundle bundle = new Bundle();
                    Integer post_id = (Integer) data.get(position).get("post_id");
                    bundle.putInt("post_id", post_id);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
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