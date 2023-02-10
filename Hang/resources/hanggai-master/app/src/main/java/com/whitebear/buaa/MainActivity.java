package com.whitebear.buaa;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.SQLException;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        OnPageChangeListener {

    private String subject= "science";

    private RadioGroup rg_tab_bar;
    private RadioButton tab_menu_shouye;
    private RadioButton tab_menu_xuexi;
    private RadioButton tab_menu_bangzhu;
    private ViewPager vpager;

    private HomePageAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    DBOpenHelper myDbHelper=new DBOpenHelper(this);
    int[] progress;
    int onResume1=0;

    TextView textView;
    TextView totalProgress;
    TextView currentProgress;
    Switch subjectText;

    @Override
    protected void onResume() {
        super.onResume();
        if(onResume1>0){
            refresh();
            progress=myDbHelper.loadMainPage(subject);
            Intent intent = new Intent("refresh1");
            intent.putExtra("progress", progress);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        onResume1++;
    }

    public void refresh(){
        try {
            View view = mAdapter.getItem(0).getView();
            totalProgress = view.findViewById(R.id.total_progress);
            currentProgress = view.findViewById(R.id.current_progress);
            subjectText = view.findViewById(R.id.subject);

            progress = myDbHelper.loadMainPage(subject);
            if (subject.equals("art")) {
                subjectText.setText("文科题库：");
            } else {
                subjectText.setText("理科题库：");
            }
            currentProgress.setText("当前进度:  第" + progress[1] + "章  第" + progress[2] + "题");
            totalProgress.setText("总进度    " + progress[0] + "/" + progress[3]);
        }catch (Exception e){
        }
    }

    private BroadcastReceiver mAReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            new Handler().post(new Runnable() {
                public void run() {
                    //在这里来写你需要刷新的地方
                    //例如：testView.setText("恭喜你成功了");
                    MainActivity.this.subject=intent.getStringExtra("subject");
                    refresh();
                }
            });
        }
    };

    private void registerReceiver(Activity activity) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(activity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("refresh");
        broadcastManager.registerReceiver(mAReceiver, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(this);

        //初始化数据库
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        progress=myDbHelper.loadMainPage(subject);
        mAdapter = new HomePageAdapter(getSupportFragmentManager(),progress,subject,this);

        //设定RadioButton集合
        bindViews();
        RadioButton[] rb = new RadioButton[3];
        rb[0] = tab_menu_shouye;
        rb[1] = tab_menu_xuexi;
        rb[2] = tab_menu_bangzhu;

        //调整Tab的图片的大小
        for(RadioButton r:rb) {
            Drawable[] drawables = r.getCompoundDrawables();
            Rect rect = new Rect(0, 0, drawables[1].getMinimumWidth() / 3, drawables[1].getMinimumHeight() / 3);
            drawables[1].setBounds(rect);
            r.setCompoundDrawables(null, drawables[1], null, null);
        }

    }


    private void bindViews() {
        rg_tab_bar = findViewById(R.id.rg_tab_bar);
        tab_menu_shouye = findViewById(R.id.tab_menu_shouye);
        tab_menu_xuexi =  findViewById(R.id.tab_menu_xuexi);
        tab_menu_bangzhu = findViewById(R.id.tab_menu_bangzhu);
        rg_tab_bar.setOnCheckedChangeListener(this);
        vpager =  findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    //设定通过点击Tab切换下一页
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tab_menu_shouye:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.tab_menu_xuexi:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.tab_menu_bangzhu:
                vpager.setCurrentItem(PAGE_THREE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }


    //设定滑动Viewpager时同时切换tab的选中状态
    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    tab_menu_shouye.setChecked(true);
                    break;
                case PAGE_TWO:
                    tab_menu_xuexi.setChecked(true);
                    break;
                case PAGE_THREE:
                    tab_menu_bangzhu.setChecked(true);
                    break;
            }
        }
    }
}