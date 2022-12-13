package com.example.hang;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.mine.utils.view.CountdownView;
import com.example.hang.ui.mine.utils.view.SubmitButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity {
    private EditText mPhoneView;
    private CountdownView mCountdownView;

    private EditText mCodeView;

    private EditText mFirstPassword;
    private EditText mSecondPassword;

    private SubmitButton mCommitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    protected void initView() {
        mPhoneView = findViewById(R.id.et_register_phone);
        mCountdownView = findViewById(R.id.cv_register_countdown);
        mCodeView = findViewById(R.id.et_register_code);
        mFirstPassword = findViewById(R.id.et_register_password1);
        mSecondPassword = findViewById(R.id.et_register_password2);
        mCommitView = findViewById(R.id.btn_register_commit);

//        mCommitView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (view == mCountdownView) {
//                    if (mPhoneView.getText().toString().length() != 11) {
//                        //mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
//                        toast(String.valueOf(R.string.common_phone_input_error));
//                        return;
//                    }
//
//                    if (true) {
//                        toast(String.valueOf(R.string.common_code_send_hint));
//                        mCountdownView.start();
//                        return;
//                    }
//
//                    // 获取验证码
//                    //
//                } else if (view == mCommitView) {
//                    if (mPhoneView.getText().toString().length() != 11) {
//                        //mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
//                        mCommitView.showError(3000);
//                        toast(String.valueOf(R.string.common_phone_input_error));
//                        return;
//                    }
//
//                    if (mCodeView.getText().toString().length() != getResources().getInteger(R.integer.sms_code_length)) {
//                        //mCodeView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
//                        mCommitView.showError(3000);
//                        toast(String.valueOf(R.string.common_code_error_hint));
//                        return;
//                    }
//
//                    if (!mFirstPassword.getText().toString().equals(mSecondPassword.getText().toString())) {
//                        //mFirstPassword.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
//                        //mSecondPassword.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
//                        mCommitView.showError(3000);
//                        toast(String.valueOf(R.string.common_password_input_unlike));
//                        return;
//                    }
//
//                    // 提交注册
//                    //
//                }
//            }
//        });
//        mSecondPassword.setOnEditorActionListener(this);
    }

    private void toast(String str) {
        Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
    }
}
