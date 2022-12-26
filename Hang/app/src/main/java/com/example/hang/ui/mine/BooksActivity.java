package com.example.hang.ui.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        //给链表添加数据
        List<Map<String, Object>> list = getData();
        //适配器，刚刚重写的！
        MyAdapter myAdapter = new MyAdapter(this, list);
        //设置适配器
        ListView lv_books = findViewById(R.id.lv_books);
        lv_books.setAdapter(myAdapter);
        lv_books.smoothScrollBy(30, 200);
    }

    //填充数据
    public List<Map<String, Object>> getData() {
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getBooksUrl, new ArrayList<>(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i+=3) {
                for (int j = 1; j <= 3; j++) {
                    Map<String, Object> map = new HashMap<>();
                    try {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i + j - 1);
                        String key = "iv_icon_book_" + j;
                        map.put(key, R.drawable.ic_book);
                        key = "tv_book_title_" + j;
                        map.put(key, jsonObject.getString("bookname"));
                        list.add(map);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    public static class MyAdapter extends BaseAdapter {

        private final List<Map<String, Object>> data;
        private final LayoutInflater layoutInflater;
        private final Context context;

        public MyAdapter(Context context, List<Map<String, Object>> data) {
            //传入的data，就是我们要在listview中显示的信息
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
        }
        //这里定义了一个类，用来表示一个item里面包含的东西
        public static class Info {
            private AppCompatImageView iv_icon_book_1;
            private AppCompatImageView iv_icon_book_2;
            private AppCompatImageView iv_icon_book_3;
            private TextView tv_title_book_1;
            private TextView tv_title_book_2;
            private TextView tv_title_book_3;
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
            convertView = layoutInflater.inflate(R.layout.activity_books_item, null);
            info.iv_icon_book_1 = convertView.findViewById(R.id.iv_icon_book_1);
            info.iv_icon_book_2 = convertView.findViewById(R.id.iv_icon_book_2);
            info.iv_icon_book_3 = convertView.findViewById(R.id.iv_icon_book_3);
            info.tv_title_book_1 = convertView.findViewById(R.id.tv_title_book_1);
            info.tv_title_book_2 = convertView.findViewById(R.id.tv_title_book_2);
            info.tv_title_book_3 = convertView.findViewById(R.id.tv_title_book_3);

            //设置数据
            info.iv_icon_book_1.setImageResource((Integer) data.get(position).get("iv_icon_book_1"));
            info.iv_icon_book_2.setImageResource((Integer) data.get(position).get("iv_icon_book_1"));
            info.iv_icon_book_3.setImageResource((Integer) data.get(position).get("iv_icon_book_1"));
            info.tv_title_book_1.setText((String) data.get(position).get("tv_book_title_1"));
            info.tv_title_book_2.setText((String) data.get(position).get("tv_book_title_2"));
            info.tv_title_book_3.setText((String) data.get(position).get("tv_book_title_3"));
            return convertView;
        }

    }
}
