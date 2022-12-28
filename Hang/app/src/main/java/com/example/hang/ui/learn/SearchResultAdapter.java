package com.example.hang.ui.learn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchResultAdapter extends BaseAdapter {
    private final List<Map<String, Object>> data;
    private final LayoutInflater layoutInflater;
    private final Context context;

    public SearchResultAdapter(Context context, List<Map<String, Object>> data) {
        //传入的data，就是我们要在listview中显示的信息
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    //这里定义了一个类，用来表示一个item里面包含的东西
    public static class Info {
        private AppCompatImageView iv_icon_book;
        private TextView tv_title_book;
        private AppCompatButton btn_book_view_content;
        private AppCompatButton book_load_public_books;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        SearchResultAdapter.Info info = new SearchResultAdapter.Info();
        convertView = layoutInflater.inflate(R.layout.activity_system_all_books_item, null);
        info.iv_icon_book = convertView.findViewById(R.id.iv_icon_book);
        info.tv_title_book = convertView.findViewById(R.id.tv_title_book);
        info.btn_book_view_content = convertView.findViewById(R.id.btn_book_view_content);
        info.book_load_public_books = convertView.findViewById(R.id.btn_book_load_public_books);

        //设置数据
        info.iv_icon_book.setImageResource((Integer) data.get(position).get("iv_icon_book"));
        info.tv_title_book.setText((String) data.get(position).get("tv_book_title"));
        info.btn_book_view_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowItemsActivity.class);
                Bundle bundle = new Bundle();
                Integer book_id = (Integer) data.get(position).get("book_id");
                bundle.putInt("book_id", book_id);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        info.book_load_public_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //importBookUrl
                ArrayList<String> para = new ArrayList<>();
                para.add((String) data.get(position).get("username"));
                para.add(String.valueOf((Integer)data.get(position).get("book_id")));
                JSONObject jo = null;
                while (jo == null) {
                    try {
                        jo = (JSONObject) HttpUtil.httpGet(Ports.importBookUrl, para, false);
                        System.out.println(jo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                assert jo != null;
                if (jo.has("error")) {
                    try {
                        toast(jo.getString("error"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    toast("导入成功, 可以去个人主页查看");
                }
            }
        });
        return convertView;
    }

    private void toast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}