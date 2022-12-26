package com.example.hang.ui.learn;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hang.LoginActivity;
import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateBookActivity extends AppCompatActivity {

    private EditText et_create_book_name;
    private EditText et_create_book_tag;
    private Button btn_create_book;
    private String username;

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (TextUtils.isEmpty(editable.toString().trim())) {
                btn_create_book.setEnabled(false);
            } else {
                String name = et_create_book_name.getText().toString().trim();
                String tag = et_create_book_tag.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(tag)) {
                    btn_create_book.setEnabled(true);
                }
            }
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);
        username = getIntent().getExtras().getString("username");
        //System.out.println(username);
        String menuTitle = "创建记忆本";
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.title_layout);//设置标题样式
            TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.display_title);//获取标题布局的textview
            textView.setText(menuTitle);//设置标题名称，menuTitle为String字符串
            actionBar.setHomeButtonEnabled(true);//设置左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
            actionBar.setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用
        }
        et_create_book_name = findViewById(R.id.et_create_book_name);
        et_create_book_tag = findViewById(R.id.et_create_book_tag);
        btn_create_book = findViewById(R.id.btn_create_book);
        btn_create_book.setEnabled(false);
        create_book();
    }

    private void create_book() {
        et_create_book_name.addTextChangedListener(textWatcher);
        et_create_book_tag.addTextChangedListener(textWatcher);
        btn_create_book.setOnClickListener(view -> {
            String book_name = et_create_book_name.getText().toString().trim();
            String book_tag = et_create_book_tag.getText().toString().trim();
            /*System.out.println("onclick");
            System.out.println(book_name);
            System.out.println(book_tag);*/
            if (!TextUtils.isEmpty(book_name) && !TextUtils.isEmpty(book_tag)) {
                if (!HttpUtil.checkConnectNetwork(getApplicationContext())) {
                    toast("请检查网络设置");
                } else {
                    HashMap<String, String> book = new HashMap<>();
                    book.put("bookname", book_name);
                    /*System.out.println("用户名2");
                    System.out.println(username);*/
                    book.put("nickname", username);
                    book.put("tag", book_tag);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = (JSONObject) HttpUtil.httpPost(Ports.addBookUrl, book);
                        System.out.println(jsonObject);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert jsonObject != null;
                    if (jsonObject.has("error")) {
                        try {
                            toast(jsonObject.getString("error"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        toast("创建成功, 去主页添加内容吧!");
                        finish();
                    }
                }
            }
        });
    }

    private void toast(String str) {
        Toast.makeText(CreateBookActivity.this, str, Toast.LENGTH_SHORT).show();
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