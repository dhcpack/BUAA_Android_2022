package com.example.hang.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.hang.LoginActivity;
import com.example.hang.R;
import com.example.hang.RegisterActivity;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.mine.utils.layout.SettingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            if (jsonObject.has("error")) {
                try {
                    toast(jsonObject.getString("error"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sb_personal_data_id.setRightText(jsonObject.getString("stuId"));
                    sb_personal_data_institution.setRightText(jsonObject.getString("institute"));
                    sb_personal_data_major.setRightText(jsonObject.getString("major"));
                    sb_personal_data_grade.setRightText(jsonObject.getString("grade"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        AppCompatButton btn_personal_data_back = findViewById(R.id.personal_data_back);
        btn_personal_data_back.setOnClickListener(view -> PersonalDataActivity.this.finish());
    }

    private void toast(String str) {
        Toast.makeText(PersonalDataActivity.this, str, Toast.LENGTH_SHORT).show();
    }
}
