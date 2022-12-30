package com.example.hang.ui.mine.myBooks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.hang.R;
import com.example.hang.RegisterActivity;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.learn.ShowItemsActivity;
import com.example.hang.util.BookPicGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyBooksActivity extends AppCompatActivity {
    private String username;
    private ListView lv_books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        setTitleBar("我的记忆本");
        //设置ListView
        //1.给链表添加数据
        List<Map<String, Object>> list = getData();
        //2.适配器，刚刚重写的！
        MyBooksAdapter myBooksAdapter = new MyBooksAdapter(this, list, username);
        //3.设置适配器
        lv_books = findViewById(R.id.lv_books);
        lv_books.setAdapter(myBooksAdapter);
        lv_books.smoothScrollBy(30, 200);
    }

    //填充数据
    public List<Map<String, Object>> getData() {
        JSONArray jsonArray = null;
        try {
            username = getIntent().getStringExtra("username");
            ArrayList<String> params = new ArrayList<>();
            params.add(username);
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getBooksUrl, params,true);
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
                    map.put("book_icon", BookPicGetter.getBookPic(jsonObject.getString("bookname")));
                    map.put("book_title", jsonObject.getString("bookname"));
                    map.put("book_id", jsonObject.getString("id"));
                    map.put("nickname", jsonObject.getString("nickname"));
                    list.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static class MyBooksAdapter extends BaseAdapter {

        private final List<Map<String, Object>> data;
        private final LayoutInflater layoutInflater;
        private final Context context;
        private String username;

        public MyBooksAdapter(Context context, List<Map<String, Object>> data, String username) {
            //传入的data，就是我们要在listview中显示的信息
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
            this.username = username;
        }
        //这里定义了一个类，用来表示一个item里面包含的东西
        public static class Info {
            private AppCompatImageView iv_icon_book;
            private TextView tv_title_book;
            private AppCompatButton btn_book_add_content;
            private AppCompatButton btn_book_view_content;
            private AppCompatButton btn_book_set_learning;
            private AppCompatButton btn_book_set_public;

            private int book_id;
            private String nickname;
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
            Info info = new Info();
            convertView = layoutInflater.inflate(R.layout.activity_my_books_item, null);
            info.iv_icon_book = convertView.findViewById(R.id.iv_icon_book);
            info.tv_title_book = convertView.findViewById(R.id.tv_title_book);
            info.btn_book_add_content = convertView.findViewById(R.id.btn_book_add_content);
            info.btn_book_view_content = convertView.findViewById(R.id.btn_book_view_content);
            info.btn_book_set_learning = convertView.findViewById(R.id.btn_book_set_learning);
            info.btn_book_set_public = convertView.findViewById(R.id.btn_book_set_public);

            //设置数据
            info.iv_icon_book.setImageResource((Integer) data.get(position).get("book_icon"));
            info.tv_title_book.setText((String) data.get(position).get("book_title"));
            info.book_id = Integer.parseInt((String)
                    Objects.requireNonNull(data.get(position).get("book_id")));
            info.nickname = (String) data.get(position).get("nickname");
            info.btn_book_add_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddQuestionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("book_id", info.book_id);
                    bundle.putString("nickname", info.nickname);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            info.btn_book_view_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShowItemsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("book_id", info.book_id);
                    bundle.putBoolean("hasDelete", true);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            info.btn_book_set_learning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = Ports.setBookUrl + username + "/" + info.book_id + "/";
                    try {
                        JSONObject jsonObject = HttpUtil.httpPut(url, new HashMap<>());
                        try {
                            String returnValue = jsonObject.getString("msg");
                            toast(returnValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            info.btn_book_set_public.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, String> params = new HashMap();
                    params.put("nickname", username);
                    params.put("bookId", String.valueOf(info.book_id));
                    params.put("public", String.valueOf(true));
                    try {
                        JSONObject jsonObject = HttpUtil.httpPut(Ports.modifyBookUrl, params);
                        if (jsonObject != null) {
                            toast("设置成功！");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return convertView;
        }

        private void toast(String str) {
            Toast.makeText(context.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
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
