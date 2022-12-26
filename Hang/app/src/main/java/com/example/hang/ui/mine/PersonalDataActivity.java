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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class PersonalDataActivity extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        username = getIntent().getStringExtra("username");
        AppCompatButton btn_personal_data_back = findViewById(R.id.personal_data_back);
        btn_personal_data_back.setOnClickListener(view -> {
            JSONObject jsonObject;
            HashMap<String, String> params = new HashMap<>();
            params.put("nickname", username);
            try {
                jsonObject = HttpUtil.httpPost(Ports.userDetailUrl + username, params);
                System.out.println(jsonObject);
                if (jsonObject.has("error")) {
                    try {
                        toast(jsonObject.getString("error"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    PersonalDataActivity.this.finish();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void toast(String str) {
        Toast.makeText(PersonalDataActivity.this, str, Toast.LENGTH_SHORT).show();
    }
}
