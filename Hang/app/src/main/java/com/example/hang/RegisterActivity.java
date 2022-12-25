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
    private EditText userNameInput;
    private EditText passwordInput1;
    private EditText passwordInput2;
    private EditText studentIdInput;
    private EditText institutionInput;
    private EditText majorInput;
    private EditText gradeInput;
    private SubmitButton submitButton;

    private String userName;
    private String password1;
    private String password2;
    private String studentId;
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
        userNameInput = findViewById(R.id.username_input);
        passwordInput1 = findViewById(R.id.password_input1);
        passwordInput2 = findViewById(R.id.password_input2);
        studentIdInput = findViewById(R.id.student_id_input);
        institutionInput = findViewById(R.id.institution_input);
        majorInput = findViewById(R.id.major_input);
        gradeInput = findViewById(R.id.grade_input);
        submitButton = findViewById(R.id.register_btn_register);

        submitButton.setOnClickListener(view -> {
            //开始登录，获取用户名和密码 getText().toString().trim();
            userName = userNameInput.getText().toString().trim();
            password1 = passwordInput1.getText().toString().trim();
            password2 = passwordInput2.getText().toString().trim();
            studentId = studentIdInput.getText().toString().trim();
            institution = institutionInput.getText().toString().trim();
            major = majorInput.getText().toString().trim();
            grade = gradeInput.getText().toString().trim();
            RadioGroup radioGroup = findViewById(R.id.radioGroup);
            RadioButton radioButton1 = findViewById(R.id.register_btn_male);
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> sex = radioButton1.isChecked());

            // TextUtils.isEmpty
            if (TextUtils.isEmpty(userName)) {
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
        params.put("nickname", userName);
        params.put("password", password1);
        params.put("target", "安卓");
        params.put("stuId", studentId);
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
        submitButton.reset();
    }
}
