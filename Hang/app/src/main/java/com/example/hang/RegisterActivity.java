package com.example.hang;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
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
    private ListPopupWindow institution_list_popup_window;
    private EditText major_input;
    private EditText grade_input;
    private SubmitButton btn_register;
    private SubmitButton btn_back_to_login;

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
        setInstitutionListPopupWindowListener();
        setInstitutionListener();
        major_input = findViewById(R.id.major_input);
        grade_input = findViewById(R.id.grade_input);
        btn_register = findViewById(R.id.register_btn_register);
        btn_back_to_login = findViewById(R.id.register_btn_login);

        btn_register.setOnClickListener(view1 -> {
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
        btn_back_to_login.setOnClickListener(view2 -> {
            //销毁注册界面
            finish();
            //跳转到登录
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
    private void setInstitutionListener() {
        institution_input.setInputType(InputType.TYPE_NULL);
        institution_input.setOnTouchListener((view, event) -> {
            institution_list_popup_window.show();
            institution_input.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(R.drawable.ic_arrows_top), null);
            return true;
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setInstitutionListPopupWindowListener() {
        String[] list= { "材料科学与工程学院", "电子信息学院", "自动化科学与电气工程学院", "能源与动力工程学院",
                "航空科学与工程学院", "计算机学院", "机械工程及自动化学院", "经济管理学院",
                "数学科学学院", "生物与医学工程学院", "人文社会科学学院", "外国语学院",
                "交通科学与工程学院", "可靠性与系统工程学院", "宇航学院", "飞行学院",
                "仪器科学与光电工程学院", "物理学院", "法学院", "软件学院",
                "继续教育学院", "沈元学院", "中法工程师学院", "国际通用工程学院",
                "新媒体艺术与设计学院", "化学学院", "马克思主义学院", "人文与社会科学高等研究院",
                "空间与环境学院", "无人系统研究院", "航空发动机研究院", "体育部",
                "国际交叉科学研究院", "北航学院", "医学科学与工程学院", "网络空间安全学院",
                "集成电路科学与工程学院", "人工智能研究院", "能源科学技术创新研究院" };
        institution_list_popup_window = new ListPopupWindow(this);
        institution_list_popup_window.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, list));
        institution_list_popup_window.setAnchorView(institution_input);
        institution_list_popup_window.setModal(true);
        institution_list_popup_window.setOnItemClickListener((adapterView, view, i, l) -> {
            institution_input.setText(list[i]);
            institution_list_popup_window.dismiss();
        });
        institution_list_popup_window.dismiss();
        institution_list_popup_window.setOnDismissListener(() -> {
            institution_input.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_arrows_bottom), null);
        });
    }

    private void sendPostRequest() {
        JSONObject jsonObject = null;
        HashMap<String, String> params = new HashMap<>();
        params.put("nickname", username);
        params.put("password", password1);
        params.put("target", "安卓");
        params.put("stuId", student_id);
        params.put("institute", institution);
        params.put("major", major);
        params.put("grade", grade);
        params.put("sex", String.valueOf(sex));
        params.put("recommends", "hhh");
        while (jsonObject == null) {
            try {
                jsonObject = HttpUtil.httpPost(Ports.signUpUrl, params);
                System.out.println(jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert jsonObject != null;
        if (jsonObject.has("error")) {
            try {
                toast(jsonObject.getString("error"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            toast("注册成功");
            //销毁注册界面
            finish();
            //跳转到登录
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
    }

    private void toast(String str) {
        Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
        btn_register.reset();
    }
}
