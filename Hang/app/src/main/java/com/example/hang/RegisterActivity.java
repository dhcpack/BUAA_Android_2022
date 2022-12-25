package com.example.hang;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.mine.utils.view.SubmitButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText username_input;
    private EditText password_input1;
    private EditText password_input2;
    private EditText student_id_input;
    private EditText institution_input;
    private EditText major_input;
    private EditText grade_input;
    private SubmitButton submit_button;

    private String username;
    private String password1;
    private String password2;
    private String student_id;
    private String institution;
    private String major;
    private String grade;
    private Boolean sex = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        doRegister();
    }

    private void doRegister() {
        //获取用户输入
        username_input = findViewById(R.id.username_input);
        password_input1 = findViewById(R.id.password_input1);
        password_input2 = findViewById(R.id.password_input2);
        student_id_input = findViewById(R.id.student_id_input);
        institution_input = findViewById(R.id.institution_input);
        major_input = findViewById(R.id.major_input);
        grade_input = findViewById(R.id.grade_input);
        submit_button = findViewById(R.id.register_btn_register);

        submit_button.setOnClickListener(view -> {
            //开始登录，获取用户名和密码 getText().toString().trim();
            username = username_input.getText().toString().trim();
            password1 = password_input1.getText().toString().trim();
            password2 = password_input2.getText().toString().trim();
            student_id = student_id_input.getText().toString().trim();
            institution = institution_input.getText().toString().trim();
            major = major_input.getText().toString().trim();
            grade = grade_input.getText().toString().trim();
            RadioGroup radioGroup = findViewById(R.id.radioGroup);
            RadioButton radioButton1 = findViewById(R.id.register_btn_male);
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> sex = radioButton1.isChecked());

            // TextUtils.isEmpty
            if (TextUtils.isEmpty(username)) {
                toast("请输入用户名");
            } else if (TextUtils.isEmpty(password1)) {
                toast("请输入第一次密码");
            } else if (TextUtils.isEmpty(password2)) {
                toast("请输入第二次密码");
            } else if (!password1.equals(password2)) {
                toast("两次密码不一致");
            } else {
                if (!HttpUtil.checkConnectNetwork(getApplicationContext())) {
                    toast("请检查网络设置");
                } else {
                    sendPostRequest();
                }
            }
        });
    }

    private void sendPostRequest() {
        JSONObject jsonObject;
        HashMap<String, String> params = new HashMap<>();
        params.put("nickname", username);
        params.put("password", password1);
        params.put("target", "安卓");
        params.put("stuId", student_id);
        params.put("institute", institution);
        params.put("major", major);
        params.put("grade", grade);
        params.put("sex", String.valueOf(sex));
        try {
            jsonObject = HttpUtil.httpPost(Ports.signUpUrl, params);
            System.out.println(jsonObject);
            if (jsonObject.has("error")) {
                try {
                    toast(jsonObject.getString("error"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                toast("注册成功");
                //销毁登录界面
                RegisterActivity.this.finish();
                //跳转到主界面，登录成功的状态传递到 MainActivity 中
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toast(String str) {
        Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
        submit_button.reset();
    }
}
