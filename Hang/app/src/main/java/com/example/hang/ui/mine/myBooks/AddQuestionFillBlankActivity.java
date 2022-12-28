package com.example.hang.ui.mine.myBooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hang.R;
import com.example.hang.ui.mine.utils.view.SubmitButton;

public class AddQuestionFillBlankActivity extends AppCompatActivity {
    private EditText question_input;
    private EditText answer_input;
    private SubmitButton btn_confirm;

    private String question;
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_fill_blank);

        setTitleBar("添加题目");
        question_input = findViewById(R.id.question_input);
        answer_input = findViewById(R.id.answer_input);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(view -> {
            question = question_input.getText().toString().trim();
            answer = answer_input.getText().toString().trim();
            if (question.equals("")) {
                toast("题目不能为空");
            }
            else if (answer.equals("")) {
                toast("答案不能为空");
            } else {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("ques", question);
                bundle.putString("ans", answer);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                toast("提交成功");
                finish();
            }
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
        Toast.makeText(AddQuestionFillBlankActivity.this, str, Toast.LENGTH_SHORT).show();
        btn_confirm.reset();
    }
}