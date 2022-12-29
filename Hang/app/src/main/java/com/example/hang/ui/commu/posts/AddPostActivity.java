package com.example.hang.ui.commu.posts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hang.R;
import com.example.hang.RegisterActivity;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.mine.utils.view.SubmitButton;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class AddPostActivity extends AppCompatActivity {
    private Context context;

    private SubmitButton upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_add_post);
        setTitleBar("写帖子");
        String username = getIntent().getExtras().getString("username");


        EditText title = findViewById(R.id.post_title_input);
        EditText tag = findViewById(R.id.post_tag_input);
        EditText content = findViewById(R.id.post_content_input);
        upload = findViewById(R.id.btn_send_post);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTitle = title.getText().toString();
                if (sTitle.length() == 0) {
                    toast("请输入文章标题");
                    return;
                }
                String sTag = tag.getText().toString();
                if (sTag.length() == 0) {
                    toast("请输入文章标签");
                    return;
                }
                String sContent = content.getText().toString();
                if (sContent.length() == 0) {
                    toast("请输入文章正文");
                    return;
                }
                HashMap<String, String> body = new HashMap<>();
                body.put("nickname", username);
                body.put("title", sTitle);
                body.put("tag", sTag);
                body.put("content", sContent);
                try {
                    JSONObject jsonObject = HttpUtil.httpPost(Ports.postUrl, body);
                    toast("发帖成功");

                    Intent intent = new Intent(context, PostDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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

    private void toast(String str) {
        Toast.makeText(AddPostActivity.this, str, Toast.LENGTH_SHORT).show();
        upload.reset();
    }
}