package com.example.hang.ui.learn;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

public class LearnSearchActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private RadioButton rb_tag;
    private RadioButton rb_name;
    private TextView tv_search_result;
    private ListView lv;
    private String[] hint = {"请输入标签...", "请输入名称"};
    private String input = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_search);
        setTitleBar("搜索");
        mSearchView = findViewById(R.id.sv_search);
        rb_tag = findViewById(R.id.rb_search_by_tag);
        rb_name = findViewById(R.id.rb_search_by_name);
        tv_search_result = findViewById(R.id.tv_search_result);
        lv = findViewById(R.id.lv_search_books);

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchView.setQueryHint(hint[0]);
        //mSearchView.setIconified(false);

        rb_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setQueryHint(hint[0]);
            }
        });
        rb_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setQueryHint(hint[1]);
            }
        });
        tv_search_result.setVisibility(View.GONE);

        setSearch();
    }

    public void setSearch() {
        //搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //System.out.println("submit");
                if ((s == null) || (s.equals(""))) {
                    toast("请输入内容");
                } else {
                    ArrayList<String> para = new ArrayList<>();
                    if (rb_tag.isChecked()) {
                        para.add("tag");
                    } else if (rb_name.isChecked()) {
                        para.add("name");
                    } else {
                        para.add("tag");
                        toast("error 没选 rb");
                    }
                    para.add(input);
                    JSONArray jsonArray = null;
                    while (jsonArray == null) {
                        try {
                            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.searchBookUrl, para, true);
                            System.out.println(jsonArray);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    assert jsonArray != null;
                    int l = jsonArray.length();
                    tv_search_result.setVisibility(View.VISIBLE);
                    tv_search_result.setText(String.format("查找到 %d 个记忆本", l));
                    String username = getIntent().getExtras().getString("username");
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < l; ++i) {
                        Map<String, Object> map = new HashMap<>();
                        try {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            map.put("iv_icon_book", R.drawable.ic_book);
                            map.put("tv_book_title", jsonObject.getString("bookname"));
                            map.put("book_id", jsonObject.getInt("id"));
                            map.put("username", username);
                            list.add(map);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    SearchResultAdapter searchResultAdapter = new SearchResultAdapter(LearnSearchActivity.this, list);
                    //3.设置适配器
                    lv = findViewById(R.id.lv_search_books);
                    lv.setAdapter(searchResultAdapter);
                    lv.smoothScrollBy(30, 200);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                input = s;
                return false;
            }
        });
    }

    public void setTitleBar(String title) {
        //设置顶部菜单栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.title_layout);//设置标题样式
            TextView tv = new TextView(this);
            tv.setText(title);
            tv.setTextSize(20);
            tv.setTextColor(this.getResources().getColor(R.color.white));
            tv.setLayoutParams(new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
            tv.setGravity(Gravity.CENTER);
//            TextView tv = actionBar.getCustomView().findViewById(R.id.display_title);//获取标题布局的textview
//            tv.setText(title);//设置标题名称，menuTitle为String字符串
            actionBar.setHomeButtonEnabled(true);//设置左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
            actionBar.setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用
            actionBar.setCustomView(tv, new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
        }
    }

    private void toast(String str) {
        Toast.makeText(LearnSearchActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}