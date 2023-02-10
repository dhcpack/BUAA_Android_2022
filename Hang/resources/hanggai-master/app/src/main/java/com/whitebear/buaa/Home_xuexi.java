package com.whitebear.buaa;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.ConditionVariable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Home_xuexi extends Fragment {
    Button chapter1;
    Button chapter2;
    Button chapter3;
    Button chapter4;
    Button chapter5;
    Button chapter6;
    Button collection;
    Button wrong;
    String subject;
    Context context;

    LocalBroadcastManager broadcastManager;
    IntentFilter intentFilter;
    BroadcastReceiver mReceiver;

    public Home_xuexi(String subject,Context context) {
        // Required empty public constructor
        this.subject=subject;
        this.context=context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction("refresh");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                //收到广播后所作的操作
                subject=intent.getStringExtra("subject");
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_xuexi, container, false);
        chapter1=view.findViewById(R.id.chapter1);
        chapter2=view.findViewById(R.id.chapter2);
        chapter3=view.findViewById(R.id.chapter3);
        chapter4=view.findViewById(R.id.chapter4);
        chapter5=view.findViewById(R.id.chapter5);
        chapter6=view.findViewById(R.id.chapter6);
        collection=view.findViewById(R.id.button_collection);
        wrong=view.findViewById(R.id.button_wrong);
        // Inflate the layout for this fragment
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("subject",subject);
                bundle.putInt("position",0);
                Intent intent=new Intent(context,PracticingPage.class);;
                switch (v.getId()){
                    case R.id.chapter1:
                        bundle.putString("value","1");
                        bundle.putString("column","CHAPTER");
                        break;
                    case R.id.chapter2:
                        bundle.putString("value","2");
                        bundle.putString("column","CHAPTER");
                        break;
                    case R.id.chapter3:
                        bundle.putString("value","3");
                        bundle.putString("column","CHAPTER");
                        break;
                    case R.id.chapter4:
                        bundle.putString("value","4");
                        bundle.putString("column","CHAPTER");
                        break;
                    case R.id.chapter5:
                        bundle.putString("value","5");
                        bundle.putString("column","CHAPTER");
                        break;
                    case R.id.chapter6:
                        bundle.putString("value","6");
                        bundle.putString("column","CHAPTER");
                        break;
                    case R.id.button_collection:
                        bundle.putString("column","COLLECTION");
                        bundle.putString("value","1");
                        break;
                    case R.id.button_wrong:
                        bundle.putString("column","WRONG");
                        bundle.putString("value","1");
                        break;
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };


        chapter1.setOnClickListener(onClickListener);
        chapter2.setOnClickListener(onClickListener);
        chapter3.setOnClickListener(onClickListener);
        chapter4.setOnClickListener(onClickListener);
        chapter5.setOnClickListener(onClickListener);
        chapter6.setOnClickListener(onClickListener);
        collection.setOnClickListener(onClickListener);
        wrong.setOnClickListener(onClickListener);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
    }
}
