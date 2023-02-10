package com.whitebear.buaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class PracticingPage extends AppCompatActivity {

    int score1=0;
    int correct = 0;
    int wrong=0;
    int position = 0,dataposition;
    int length = 0;
    String subject, column, value;
    Button collect, goback;
    TextView progress, totalQnumber, chapterLable,score;
    Chronometer time;
    DBOpenHelper myDbHelper = new DBOpenHelper(PracticingPage.this);
    private PracticingPageAdapter mAdapter;
    private ViewPager vpager;
    LocalBroadcastManager broadcastManager;

    //questions只有在activity开始时才会加载一次，可能（其实并不）是整个activity唯一的数据存储单位
    String[][] questions;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mAReceiver);
    }

    private BroadcastReceiver mAReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals("onlynext")){
                vpager.arrowScroll(View.FOCUS_RIGHT);
            }else {
                final Boolean aFinal = intent.getBooleanExtra("final", false);
                if (aFinal) {
                    writedata();
                    time.stop();
                    Intent warpUp = new Intent(PracticingPage.this, WrapUp.class);
                    warpUp.putExtra("time", time.getText().toString());
                    warpUp.putExtra("score", score1);
                    warpUp.putExtra("correct", correct);
                    warpUp.putExtra("wrong", wrong);
                    warpUp.putExtra("column", column);
                    warpUp.putExtra("value", value);
                    startActivity(warpUp);
                } else {
                    score1 += intent.getIntExtra("score", 0);
                    String answer = intent.getStringExtra("answer");
                    if ("right".equals(answer)) {
                        // 这地方只能在主线程中刷新UI,子线程中无效，因此用Handler来实现
                        new Handler().post(new Runnable() {
                            public void run() {
                                //在这里来写你需要刷新的地方
                                //例如：testView.setText("恭喜你成功了");
                                correct += 1;
                                vpager.arrowScroll(View.FOCUS_RIGHT);
                            }
                        });

                    } else {
                        wrong += 1;
                    }
                }
            }
        }
    };

    private void registerReceiver(Activity activity) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("nextpage");
        intentFilter.addAction("onlynext");
        broadcastManager.registerReceiver(mAReceiver, intentFilter);
    }

    private void loadCollectState() {
        if (questions[position][8].equals("1")) {
            collect.setBackground(getDrawable(R.mipmap.shoucang_1));
        } else {
            collect.setBackground(getDrawable(R.mipmap.shoucang));
        }
    }

    private void writedata(){
        if(column.equals("CHAPTER")) {
            String sql = "update currentprogress set chapter=" + value + " where subject='" + subject + "'";
            String sql3 = "update currentprogress set position="+ (position+1)+" where subject='"+subject+"'";
            myDbHelper.openDataBase();
            myDbHelper.getMyDataBase().execSQL(sql);
            if(dataposition<position+1) {
                String sql2 = "update progress set progress="+(position+1)+" where subject='"+subject+"' and CHAPTER='"+value+"'";
                myDbHelper.getMyDataBase().execSQL(sql2);
            }
            myDbHelper.getMyDataBase().execSQL(sql3);
            myDbHelper.getMyDataBase().close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practicing_page);
        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerReceiver(this);

        {
            //获取从bundle传来的三个参数
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            subject = bundle.getString("subject");
            column = bundle.getString("column");
            value = bundle.getString("value");
            position = bundle.getInt("position");
            dataposition=bundle.getInt("dataposition");

            score=findViewById(R.id.score);
            progress = findViewById(R.id.practicing_progress);
            chapterLable = findViewById(R.id.chapterlable);
            totalQnumber = findViewById(R.id.totalQnumber);
            collect = findViewById(R.id.collect);
            goback = findViewById(R.id.goback);
            time=findViewById(R.id.chronometer);
            time.start();

            if (!column.equals("test")) {

                if (column.equals("CHAPTER")) {
                    if (value.equals("1")) {
                        chapterLable.setText("第一单元");
                    }
                    if (value.equals("2")) {
                        chapterLable.setText("第二单元");
                    }
                    if (value.equals("3")) {
                        chapterLable.setText("第三单元");
                    }
                    if (value.equals("4")) {
                        chapterLable.setText("第四单元");
                    }
                    if (value.equals("5")) {
                        chapterLable.setText("第五单元");
                    }
                    if (value.equals("6")) {
                        chapterLable.setText("第六单元");
                    }
                } else if (column.equals("COLLECTION")) {
                    chapterLable.setText("收藏题目");
                } else if (column.equals("WRONG")) {
                    chapterLable.setText("错题重做");
                } else if (column.equals("TEST")) {
                    chapterLable.setText("模拟考试");
                }
                questions = myDbHelper.loadQuestionByColumn(subject, column, value);

            } else {
                chapterLable.setText("模拟考试");
                int number=new Random().nextInt(10)+10;
                questions = myDbHelper.loadTest(subject,number);
            }
            length = questions.length;

            if(length==0){
                Toast.makeText(PracticingPage.this, "这里还没有题目哦", Toast.LENGTH_LONG).show();
                finish();
            }else {
                mAdapter = new PracticingPageAdapter(getSupportFragmentManager(),subject,questions, this);
                vpager = findViewById(R.id.practice_vp);
                vpager.setAdapter(mAdapter);
                vpager.setCurrentItem(position);

                progress.setText(position + 1 + "");
                totalQnumber.setText("/" + length);
                loadCollectState();

                collect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (questions[position][8].equals("1")) {
                            String sql = "update " + subject + "_questions set collection=0 where id=" + questions[position][7];
                            myDbHelper.openDataBase();
                            myDbHelper.getMyDataBase().execSQL(sql);
                            myDbHelper.getMyDataBase().close();
                            collect.setBackground(getDrawable(R.mipmap.shoucang));
                            questions[position][8] = "0";
                            Toast.makeText(PracticingPage.this, "取消收藏啦", Toast.LENGTH_LONG).show();
                        } else {
                            String sql = "update " + subject + "_questions set collection=1 where id=" + questions[position][7];
                            myDbHelper.openDataBase();
                            myDbHelper.getMyDataBase().execSQL(sql);
                            myDbHelper.getMyDataBase().close();
                            collect.setBackground(getDrawable(R.mipmap.shoucang_1));
                            questions[position][8] = "1";
                            Toast.makeText(PracticingPage.this, "收藏成功", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                goback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                vpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        PracticingPage.this.position = position;
                        score.setText(score1 + "");
                        progress.setText(position + 1 + "");
                        loadCollectState();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            }
        }
    }
        @Override



        public void onBackPressed () {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("做完所有题目可以看到成绩单哦");
            builder.setNegativeButton("那继续吧", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("我就要走", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    writedata();
                    PracticingPage.super.onBackPressed();
                }
            });
            builder.show();
            //super.onBackPressed();
        }
}

