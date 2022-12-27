package com.example.hang.ui.learn;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.learn.util.ListBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;

public class ShowItemsActivity extends AppCompatActivity {

    private ArrayList<ListBean> allQues = new ArrayList<>();
    private int book_id = -1;
    private ViewPager vp_content;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);
        setTitleBar("查看列表");
        // book id != -1
        book_id = getIntent().getExtras().getInt("book_id");

        try {
            getAllQues();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initPagerStrip();
        initPagerView();
    }

    private void getAllQues() throws JSONException, IOException {
        ArrayList<String> id = new ArrayList<>();
        id.add(String.valueOf(book_id));
        JSONArray jsonArray = null;
        while (jsonArray == null) {
            try {
                jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getQuestionUrl, id, true);
                System.out.println(jsonArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert jsonArray != null;
        int l = jsonArray.length();
        for (int i = 0; i < l; ++i) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            ListBean listBean = new ListBean(jsonObject.getInt("id"), jsonObject.getInt("type"), jsonObject.getString("ques"),
                    jsonObject.getString("ans1"), jsonObject.getString("ans2"), jsonObject.getString("ans3"),  jsonObject.getString("ans4"),
                    jsonObject.getInt("review"), jsonObject.getString("next_time"), jsonObject.getString("nickname"), jsonObject.getInt("book") );
            allQues.add(listBean);
        }
        System.out.println("show items size" + allQues.size());
    }

    public void initPagerStrip() {
        PagerTabStrip pts_tab = findViewById(R.id.pts_tab);
        pts_tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        pts_tab.setTextColor(Color.BLACK);
    }

    public void initPagerView() {
        LearnCardPagerAdapater adapter = new LearnCardPagerAdapater(getSupportFragmentManager(), this, allQues, true);
        vp_content = findViewById(R.id.vp_content_showItems);
        vp_content.setAdapter(adapter);
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //翻页状态改变时触发，0-静止  1-正在滑动  2-滑动完毕
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            //翻页过程中触发
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //翻页结束后触发
            @Override
            public void onPageSelected(int position) {
                /*PracticingPage.this.position = position;
                score.setText(score1 + "");
                progress.setText(position + 1 + "");
                loadCollectState();*/
            }
        });
        //vp_content.setCurrentItem(3);
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
            tv.setLayoutParams(new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
            tv.setGravity(Gravity.CENTER);
//            TextView tv = actionBar.getCustomView().findViewById(R.id.display_title);//获取标题布局的textview
//            tv.setText(title);//设置标题名称，menuTitle为String字符串
            actionBar.setHomeButtonEnabled(true);//设置左上角的图标是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
            actionBar.setDisplayShowCustomEnabled(true);// 使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用
            actionBar.setCustomView(tv, new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
        }
    }

    private void toast(String str) {
        Toast.makeText(ShowItemsActivity.this, str, Toast.LENGTH_SHORT).show();
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