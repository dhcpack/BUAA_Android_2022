package com.example.hang.ui.mine.report;

import static android.view.View.DRAWING_CACHE_QUALITY_AUTO;
import static android.view.View.DRAWING_CACHE_QUALITY_HIGH;
import static android.view.View.DRAWING_CACHE_QUALITY_LOW;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        setTitleBar("学习报告");
        String username = getIntent().getStringExtra("username");
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
                String sex = userInfo.getString("sex");
                ImageView avatar = findViewById(R.id.iv_person_data_avatar);
                if (sex.equals("true")) {
                    avatar.setImageResource(R.drawable.ic_default_avatar_male);
                } else {
                    avatar.setImageResource(R.drawable.ic_default_avatar_female);
                }
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
        TextView tv_check_days = findViewById(R.id.tv_check_days);
        TextView tv_check_last_record = findViewById(R.id.tv_check_last_record);
        try {
            JSONObject check = (JSONObject) HttpUtil.httpGet(Ports.checkDetail, params, false);
            try {
                int totalDays = check != null ? Integer.parseInt(check.getString("totalDays")) : 0;
                String lastCheckRecord = check != null ? check.getString("lastCheckRecord") : "无";
                String checkDays;
                String checkLastRecord;
                if (check != null && !lastCheckRecord.equals("None")) {
                    String[] strings = lastCheckRecord.split("T");
                    lastCheckRecord = strings[0];
                    strings = lastCheckRecord.split("-");
                    lastCheckRecord = strings[0] + "年" + strings[1] + "月" + strings[2] + "日";
                    checkDays = "已打卡：" + totalDays + "天";
                    checkLastRecord = "上一次打卡时间：" + lastCheckRecord;
                } else if (lastCheckRecord.equals("None")) {
                    checkDays = "还未开始打卡，快去打卡吧~";
                    checkLastRecord = "";
                } else {
                    checkDays = "";
                    checkLastRecord = "";
                }
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
        ProgressBar progressBar = findViewById(R.id.progressbar_learn);
        TextView tv_title_book = findViewById(R.id.tv_title_book);
        try {
            JSONObject learningBook = (JSONObject) HttpUtil.httpGet(Ports.learningBookUrl, params, false);
            try {
                String learningBookTitle = learningBook.getString("bookname");
                tv_title_book.setText(learningBookTitle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject learnCount = (JSONObject) HttpUtil.httpGet(Ports.getReviewCount, params, false);
            try {
                int total = Integer.parseInt(learnCount.getString("待复习")) + Integer.parseInt(learnCount.getString("未学习")) + Integer.parseInt(learnCount.getString("已学习"));
                int learned = Integer.parseInt(learnCount.getString("已学习"));
                double learningBookProcess = ((double) learned / total);
                learningBookProcess = learningBookProcess * 100;
                progressBar.setProgress((int) (learningBookProcess));
                progressBar.setMax(100);
                TextView tv_learning_percent = findViewById(R.id.tv_learning_percent);
                @SuppressLint("DefaultLocale") String learningProcess = String.format("学习进度%.2f", learningBookProcess) + "%";
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
            Bitmap bitmap = getBitmap(view);
            Boolean hasSucceed = BitMapUtils.saveBitmap("learningReport.png", bitmap, getApplicationContext());
            if (hasSucceed) {
                toast("生成图片成功，已保存");
            } else {
                toast("生成图片失败");
            }
        });
    }

    private Bitmap getBitmap(View view) {
        if (null == view) {
            return null;
        }
        Bitmap bitmap;

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
        if (opaque) {
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

    private void toast(String str) {
        Toast.makeText(ReportActivity.this, str, Toast.LENGTH_SHORT).show();
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
}
