package com.whitebear.buaa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class Home_shouye extends Fragment {
    int[] progress;
    String subject;
    Context context;
    private Button start_practice,practice_test;
    View view;

    DBOpenHelper myDbHelper;
    LocalBroadcastManager broadcastManager;
    IntentFilter intentFilter;
    BroadcastReceiver mReceiver;

    public Home_shouye(int[] progress,String subject,Context context){
        this.progress=progress;
        this.subject=subject;
        this.context=context;
    }
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction("refresh1");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                //收到广播后所作的操作
                    progress = intent.getIntArrayExtra("progress");
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home_shouye, container, false);
        TextView totalProgress=view.findViewById(R.id.total_progress);
        TextView currentProgress = view.findViewById(R.id.current_progress);
        final Switch subject1 =view.findViewById(R.id.subject);
        start_practice=view.findViewById(R.id.start_practice);
        practice_test=view.findViewById(R.id.practice_test);

        subject1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    subject="art";
                    Intent intent = new Intent("refresh");
                    intent.putExtra("subject", "art");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                }else {
                    subject="science";
                    Intent intent = new Intent("refresh");
                    intent.putExtra("subject", "science");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                }

                myDbHelper=new DBOpenHelper(context);
                progress = myDbHelper.loadMainPage(subject);
            }
        });

        start_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("subject",subject);
                bundle.putString("column","CHAPTER");
                bundle.putString("value",progress[1]+"");
                bundle.putInt("position",progress[2]-1);
                bundle.putInt("dataposition",progress[4]);
                Intent intent=new Intent(context,PracticingPage.class);;
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        practice_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("subject",subject);
                bundle.putString("column","test");
                bundle.putString("value","0");
                bundle.putInt("position",0);
                Intent intent=new Intent(context,PracticingPage.class);;
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        if(subject.equals("art")) {
            subject1.setText("文科题库：");
        }else {
            subject1.setText("理科题库：");
        }
        currentProgress.setText("当前进度:  第"+progress[1]+"章  第"+progress[2]+"题");
        totalProgress.setText("总进度    "+progress[0]+"/"+progress[3]);
        return view;
    }




}
