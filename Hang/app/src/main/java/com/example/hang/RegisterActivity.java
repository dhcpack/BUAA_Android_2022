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

        initView();

        mSecondPassword.setOnEditorActionListener(this);
    }

    protected void initView() {
        mPhoneView = findViewById(R.id.et_register_phone);
        mCountdownView = findViewById(R.id.cv_register_countdown);
        mCodeView = findViewById(R.id.et_register_code);
        mFirstPassword = findViewById(R.id.et_register_password1);
        mSecondPassword = findViewById(R.id.et_register_password2);
        mCommitView = findViewById(R.id.btn_register_commit);

        mCommitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == mCountdownView) {
                    if (mPhoneView.getText().toString().length() != 11) {
                        mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                        toast(R.string.common_phone_input_error);
                        return;
                    }

                    if (true) {
                        toast(R.string.common_code_send_hint);
                        mCountdownView.start();
                        return;
                    }

                    // 获取验证码
                    EasyHttp.post(this)
                            .api(new GetCodeApi()
                                    .setPhone(mPhoneView.getText().toString()))
                            .request(new HttpCallback<HttpData<Void>>(this) {

                                @Override
                                public void onSucceed(HttpData<Void> data) {
                                    toast(R.string.common_code_send_hint);
                                    mCountdownView.start();
                                }

                                @Override
                                public void onFail(Exception e) {
                                    super.onFail(e);
                                    mCountdownView.start();
                                }
                            });
                } else if (view == mCommitView) {
                    if (mPhoneView.getText().toString().length() != 11) {
                        mPhoneView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                        mCommitView.showError(3000);
                        toast(R.string.common_phone_input_error);
                        return;
                    }

                    if (mCodeView.getText().toString().length() != getResources().getInteger(R.integer.sms_code_length)) {
                        mCodeView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                        mCommitView.showError(3000);
                        toast(R.string.common_code_error_hint);
                        return;
                    }

                    if (!mFirstPassword.getText().toString().equals(mSecondPassword.getText().toString())) {
                        mFirstPassword.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                        mSecondPassword.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                        mCommitView.showError(3000);
                        toast(R.string.common_password_input_unlike);
                        return;
                    }

                    // 提交注册
                    EasyHttp.post(this)
                            .api(new RegisterApi()
                                    .setPhone(mPhoneView.getText().toString())
                                    .setCode(mCodeView.getText().toString())
                                    .setPassword(mFirstPassword.getText().toString()))
                            .request(new HttpCallback<HttpData<RegisterApi.Bean>>(this) {

                                @Override
                                public void onStart(Call call) {
                                    mCommitView.showProgress();
                                }

                                @Override
                                public void onEnd(Call call) {}

                                @Override
                                public void onSucceed(HttpData<RegisterApi.Bean> data) {
                                    postDelayed(() -> {
                                        mCommitView.showSucceed();
                                        postDelayed(() -> {
                                            setResult(RESULT_OK, new Intent()
                                                    .putExtra(INTENT_KEY_PHONE, mPhoneView.getText().toString())
                                                    .putExtra(INTENT_KEY_PASSWORD, mFirstPassword.getText().toString()));
                                            finish();
                                        }, 1000);
                                    }, 1000);
                                }

                                @Override
                                public void onFail(Exception e) {
                                    super.onFail(e);
                                    postDelayed(() -> {
                                        mCommitView.showError(3000);
                                    }, 1000);
                                }
                            });
                }
            }
        });
        mSecondPassword.setOnEditorActionListener(this);
    }

    private void toast(String str) {
        Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
    }
}
