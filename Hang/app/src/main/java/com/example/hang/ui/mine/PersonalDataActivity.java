package com.example.hang.ui.mine;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.mine.utils.layout.SettingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class PersonalDataActivity extends AppCompatActivity {
    private String username;
    private SettingBar sb_personal_data_id;
    private SettingBar sb_personal_data_institution;
    private SettingBar sb_personal_data_major;
    private SettingBar sb_personal_data_grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        setTitleBar("个人资料");
        username = getIntent().getStringExtra("username");
        sb_personal_data_id = findViewById(R.id.personal_data_id);
        sb_personal_data_institution = findViewById(R.id.personal_data_institution);
        sb_personal_data_major = findViewById(R.id.personal_data_major);
        sb_personal_data_grade = findViewById(R.id.personal_data_grade);

        JSONObject jsonObject;
        ArrayList<String> params = new ArrayList<>();
        params.add(username);
        try {
            jsonObject = (JSONObject) HttpUtil.httpGet(Ports.userDetailUrl, params, false);
            System.out.println(jsonObject);
            try {
                sb_personal_data_id.setRightText(jsonObject.getString("stuId"));
                sb_personal_data_institution.setRightText(jsonObject.getString("institute"));
                sb_personal_data_major.setRightText(jsonObject.getString("major"));
                sb_personal_data_grade.setRightText(jsonObject.getString("grade"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //AppCompatButton btn_personal_data_back = findViewById(R.id.personal_data_back);
        //btn_personal_data_back.setOnClickListener(view -> PersonalDataActivity.this.finish());
    }

    public void setTitleBar(String title) {
        //设置顶部菜单栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.title_layout);//设置标题样式
            TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.display_title);//获取标题布局的textview
            textView.setText(title);//设置标题名称，menuTitle为String字符串
            actionBar.setHomeButtonEnabled(true);//设置左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
            actionBar.setDisplayShowCustomEnabled(true);//使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用
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
