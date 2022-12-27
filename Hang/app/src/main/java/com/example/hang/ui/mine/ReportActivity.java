package com.example.hang.ui.mine;

import static android.view.View.DRAWING_CACHE_QUALITY_AUTO;
import static android.view.View.DRAWING_CACHE_QUALITY_HIGH;
import static android.view.View.DRAWING_CACHE_QUALITY_LOW;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.mine.utils.function.BitMapUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        setTitleBar("学习报告");
        username = getIntent().getStringExtra("username");
        ArrayList<String> params = new ArrayList<>();
        params.add(username);
        setUserInfo(params);
        setCheck(params);
        setLearning(params);
        View view = this.getWindow().getDecorView();
        generateImage(view);
    }

    private void setUserInfo(ArrayList<String> params) {
        try {
            JSONObject userInfo = (JSONObject) HttpUtil.httpGet(Ports.userDetailUrl, params, false);
            try {
                String stuId = userInfo.getString("stuId");
                String username = userInfo.getString("nickname");
                TextView tv_stu_id = findViewById(R.id.tv_stu_id);
                TextView tv_username = findViewById(R.id.tv_username);
                tv_stu_id.setText(stuId);
                tv_username.setText(username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCheck(ArrayList<String> params) {
        try {
            JSONObject check = (JSONObject) HttpUtil.httpGet(Ports.checkUrl, params, false);
            try {
                int totalDays = check != null ? Integer.parseInt(check.getString("totalDays")) : 0;
                String lastCheckRecord = check != null ? check.getString("lastCheckRecord") : "无";
                TextView tv_check_days = findViewById(R.id.tv_check_days);
                TextView tv_check_last_record = findViewById(R.id.tv_check_last_record);
                String checkDays = "已打卡：" + totalDays + "天";
                String checkLastRecord = "上一次打卡时间：" + lastCheckRecord;
                tv_check_days.setText(checkDays);
                tv_check_last_record.setText(checkLastRecord);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLearning(ArrayList<String> params) {
        try {
            JSONObject learningBook = (JSONObject) HttpUtil.httpGet(Ports.learningBookUrl, params, false);
            try {
                String learningBookTitle = learningBook.getString("bookname");
                int learningBookProcess = Integer.parseInt(learningBook.getString("process"));
                TextView tv_title_book = findViewById(R.id.tv_title_book);
                tv_title_book.setText(learningBookTitle);
                TextView tv_learning_percent = findViewById(R.id.tv_learning_percent);
                String learningProcess = "学习进度" + learningBookProcess + "%";
                tv_learning_percent.setText(learningProcess);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateImage(View view) {
        AppCompatButton btn_gen_report = findViewById(R.id.btn_gen_report);
        btn_gen_report.setOnClickListener(view1 -> {
            Bitmap bitmap = getBitmap(view1);
            BitMapUtils.saveBitmap("learningReport", bitmap, getApplicationContext());
        });
    }

    private Bitmap getBitmap(View view) {
        if (null == view) {
            return null;
        }
        Bitmap bitmap = null;

        // 步骤一：获取视图的宽和高
        int width = view.getRight() - view.getLeft();
        int height = view.getBottom() - view.getTop();
        // 判断背景是否为不透明
        final boolean opaque = view.getDrawingCacheBackgroundColor() != 0 || view.isOpaque();
        Bitmap.Config quality;
        if (!opaque) {
            switch (view.getDrawingCacheQuality()) {
                case DRAWING_CACHE_QUALITY_AUTO:
                case DRAWING_CACHE_QUALITY_LOW:
                case DRAWING_CACHE_QUALITY_HIGH:
                default:
                    quality = Bitmap.Config.ARGB_8888;
                    break;
            }
        } else {
            quality = Bitmap.Config.RGB_565;
        }

        // 步骤二：生成bitmap
        bitmap = Bitmap.createBitmap(getResources().getDisplayMetrics(), width, height, quality);
        bitmap.setDensity(getResources().getDisplayMetrics().densityDpi);
        if (opaque){
            bitmap.setHasAlpha(false);
        }
        boolean clear = view.getDrawingCacheBackgroundColor() != 0;

        // 步骤三：绘制canvas
        Canvas canvas = new Canvas(bitmap);
        if (clear) {
            bitmap.eraseColor(view.getDrawingCacheBackgroundColor());
        }

        view.computeScroll();
        final int restoreCount = canvas.save();
        canvas.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(canvas);
        canvas.restoreToCount(restoreCount);
        canvas.setBitmap(null);

        return bitmap;
    }

    public void setTitleBar(String title) {
        //设置顶部菜单栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.title_layout);//设置标题样式
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
}
