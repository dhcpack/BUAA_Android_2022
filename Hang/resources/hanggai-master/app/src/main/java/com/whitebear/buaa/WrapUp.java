package com.whitebear.buaa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WrapUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrap_up);

        TextView score=findViewById(R.id.defen1);
        TextView chapterLable=findViewById(R.id.chapter);
        TextView time=findViewById(R.id.yongshi1);
        TextView correct=findViewById(R.id.zhengque1);
        TextView wrong=findViewById(R.id.cuowu1);
        Button button=findViewById(R.id.fanhui);
        TextView remind=findViewById(R.id.remind);

        Intent intent=getIntent();
        String column=intent.getStringExtra("column");
        String value=intent.getStringExtra("value");

        int minute = Integer.parseInt(intent.getStringExtra("time").split(":")[0]);
        if(column.equals("test")|minute>=35){
            remind.setText("航概正式考试的时间是35min，要加油哦");
        }else{
            remind.setText("注意不要熬夜刷题哦");
        }
        score.setText(intent.getIntExtra("score",0)+"");
        time.setText(intent.getStringExtra("time"));
        correct.setText(intent.getIntExtra("correct",0)+"");
        wrong.setText(intent.getIntExtra("wrong",0)+"");
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
