package com.example.hang.ui.recom;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.learn.ShowItemsActivity;
import com.example.hang.ui.learn.SystemAllBooksActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recommend extends Fragment {
    private ListView recommendBooks;
    private RecommendViewModel mViewModel;
    private String username;

    public static Recommend newInstance() {
        return new Recommend();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        //setTitleBar("为您推荐");
        //获取传过来的数据
        Bundle bundle = getActivity().getIntent().getExtras();
        username = bundle.getString("username");

        // 得到推荐书籍
        List<Map<String, Object>> recommendList = getRecommendBooks(view);
        RecommendAdapter recommendAdapter = new RecommendAdapter(this.getActivity(), recommendList, username);
        recommendBooks = view.findViewById(R.id.reco_books);
        recommendBooks.setAdapter(recommendAdapter);
        recommendBooks.smoothScrollBy(30, 200);
        recommendBooks.setBottom(-300);

        return view;
    }


    private List<Map<String, Object>> getRecommendBooks(View view) {
        ArrayList<String> params = new ArrayList<>();
        params.add(username);
        JSONArray recommendBooks = null;
        try {
            recommendBooks = (JSONArray) HttpUtil.httpGet(Ports.recommendUrl, params, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (recommendBooks != null) {
            for (int i = 0; i < recommendBooks.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                try {
                    JSONObject jsonObject = (JSONObject) recommendBooks.get(i);
                    System.out.println(jsonObject.toString());
                    map.put("book_icon", R.drawable.ic_book);
                    map.put("book_title", jsonObject.getString("bookname"));
                    map.put("book_id", jsonObject.getString("id"));
                    list.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static class RecommendAdapter extends BaseAdapter {
        private final List<Map<String, Object>> data;
        private final LayoutInflater layoutInflater;
        private final Context context;
        private final String userName;

        public RecommendAdapter(Context context, List<Map<String, Object>> data, String userName) {
            //传入的data，就是我们要在listview中显示的信息
            this.context = context;
            this.data = data;
            this.layoutInflater = LayoutInflater.from(context);
            this.userName = userName;
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

        private class ImportButton implements View.OnClickListener {
            private String bookId;

            public ImportButton(String bookId) {
                this.bookId = bookId;
            }

            @Override
            public void onClick(View view) {
                //importBookUrl
                ArrayList<String> para = new ArrayList<>();
                para.add(userName);
                para.add(bookId);
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
        }

        //跟activity中的onCreate()差不多，目的就是给item布局中的各个控件对应好，并添加数据
        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            Recommend.RecommendAdapter.Info info = new Recommend.RecommendAdapter.Info();
            convertView = layoutInflater.inflate(R.layout.activity_system_all_books_item, null);
            info.iv_icon_book = convertView.findViewById(R.id.iv_icon_book);
            info.tv_title_book = convertView.findViewById(R.id.tv_title_book);
            info.btn_book_view_content = convertView.findViewById(R.id.btn_book_view_content);
            info.book_load_public_books = convertView.findViewById(R.id.btn_book_load_public_books);

            //设置数据
            info.iv_icon_book.setImageResource((Integer) data.get(position).get("book_icon"));
            info.tv_title_book.setText((String) data.get(position).get("book_title"));
            info.btn_book_view_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShowItemsActivity.class);
                    Bundle bundle = new Bundle();
                    Integer book_id = Integer.parseInt((String) data.get(position).get("book_id"));
                    bundle.putInt("book_id", book_id);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            info.book_load_public_books.setOnClickListener(new ImportButton((String) data.get(position).get("book_id")));
            return convertView;
        }

        private void toast(String str) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }

    public void setTitleBar(String title) {
        //设置顶部菜单栏
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        TextView tv = new TextView(this.getActivity());
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RecommendViewModel.class);
        // TODO: Use the ViewModel
    }

}