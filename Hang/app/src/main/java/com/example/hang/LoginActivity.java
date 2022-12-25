package com.example.hang;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.mine.utils.view.SubmitButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private SubmitButton btn_login; //登录按钮
    private SubmitButton btn_register; //注册按钮
    private String username, password; //获取的用户名，密码，加密密码
    private EditText user_input, password_input; //编辑框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setContentView(R.layout.activity_login_old);
        //设置此界面为竖屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {

        btn_login = findViewById(R.id.login_btn_login);
        btn_register = findViewById(R.id.login_btn_register);
        user_input = findViewById(R.id.user_input);
        password_input = findViewById(R.id.password_input);

        btn_login.setOnClickListener(v -> {
            //开始登录，获取用户名和密码 getText().toString().trim();
            username = user_input.getText().toString().trim();
            password = password_input.getText().toString().trim();

            // TextUtils.isEmpty
            if (TextUtils.isEmpty(username)) {
                toast("请输入用户名");
            } else if (TextUtils.isEmpty(password)) {
                toast("请输入密码");
            } else {
                if (!HttpUtil.checkConnectNetwork(getApplicationContext())) {
                    toast("请检查网络设置");
                } else {
                    ArrayList<String> signIn = new ArrayList<>();
                    signIn.add(username);
                    signIn.add(password);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = (JSONObject) HttpUtil.httpGet(Ports.signInUrl, signIn, false);
                        System.out.println(jsonObject);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert jsonObject != null;
                    if (jsonObject.has("error")) {
                        try {
                            Toast.makeText(LoginActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        toast("登录成功");

                        //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
                        //saveLoginStatus(true, userName);
                        //登录成功后关闭此页面进入主页
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putString("password", password);
                        intent.putExtras(bundle);
                        //intent.putExtra("isLogin", true);
                        //RESULT_OK为Activity系统常量，状态码为-1
                        // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                        //setResult(RESULT_OK, intent);
                        //销毁登录界面
                        LoginActivity.this.finish();
                        //跳转到主界面，登录成功的状态传递到 MainActivity 中
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                }
            }
        });
        btn_register.setOnClickListener(view -> {
            //销毁登录界面
            LoginActivity.this.finish();
            //跳转到主界面，登录成功的状态传递到 RegisterActivity 中
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

//    /**
//     * 保存登录状态和登录用户名到SharedPreferences中
//     */
//    private void saveLoginStatus(boolean status, String userName) {
//        //saveLoginStatus(true, userName);
//        //loginInfo表示文件名  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
//        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
//        //获取编辑器
//        SharedPreferences.Editor editor = sp.edit();
//        //存入boolean类型的登录状态
//        editor.putBoolean("isLogin", status);
//        //存入登录状态时的用户名
//        editor.putString("loginUserName", userName);
//        //提交修改
//        editor.commit();
//    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null) {
//            //是获取注册界面回传过来的用户名
//            // getExtra().getString("***");
//            String userName = data.getStringExtra("userName");
//            if (!TextUtils.isEmpty(userName)) {
//                //设置用户名到 et_user_name 控件
//                user_input.setText(userName);
//                //et_user_name控件的setSelection()方法来设置光标位置
//                user_input.setSelection(userName.length());
//            }
//        }
//    }

    private void toast(String str) {
        Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
        btn_login.reset();
        btn_register.reset();
    }
}


