package com.example.hang.ui.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hang.LoginActivity;
import com.example.hang.R;
import com.example.hang.RegisterActivity;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.mine.utils.view.SubmitButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class AddQuestionActivity extends AppCompatActivity {
    private EditText question_input;
    private EditText answer_input;
    private EditText type_input;
    private ListPopupWindow type_list_popup_window;
    private SubmitButton btn_confirm;

    private String question;
    private String answer;
    private String type;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        setTitleBar("添加题目");
        question_input = findViewById(R.id.question_input);
        answer_input = findViewById(R.id.answer_input);
        type_input = findViewById(R.id.type_input);
        setTypeListPopupWindowListener();
        setTypeListener();
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(view -> {
            question = question_input.getText().toString().trim();
            answer = answer_input.getText().toString().trim();
            type = type_input.getText().toString().trim();
            sendPostRequest();
        });
    }

    private void sendPostRequest() {
        JSONObject jsonObject;
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("ques", question);
        params.put("ans2", answer);
        try {
            jsonObject = HttpUtil.httpPost(Ports.addQuestionUrl, params);
            System.out.println(jsonObject);
            if (jsonObject.has("error")) {
                try {
                    toast(jsonObject.getString("error"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                toast("注册成功");
                //销毁注册界面
                AddQuestionActivity.this.finish();
                //跳转到登录
                startActivity(new Intent(AddQuestionActivity.this, LoginActivity.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    private void setTypeListener() {
        type_input.setInputType(InputType.TYPE_NULL);
        type_input.setOnTouchListener((view, event) -> {
            type_list_popup_window.show();
            type_input.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.ic_arrows_top), null);
            return true;
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setTypeListPopupWindowListener() {
        String[] list= { "填空题", "单选", "多选", "图片答案题"};
        type_list_popup_window = new ListPopupWindow(this);
        type_list_popup_window.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, list));
        type_list_popup_window.setAnchorView(type_input);
        type_list_popup_window.setModal(true);
        type_list_popup_window.setOnItemClickListener((adapterView, view, i, l) -> {
            type_input.setText(i); // list[i]对应list中相应String
            type_list_popup_window.dismiss();
        });
        type_list_popup_window.dismiss();
        type_list_popup_window.setOnDismissListener(() -> {
            type_input.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_arrows_bottom), null);
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
        Toast.makeText(AddQuestionActivity.this, str, Toast.LENGTH_SHORT).show();
        btn_confirm.reset();
    }
}
