package com.example.hang.ui.learn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.learn.util.ListBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class FillBlankFragment extends Fragment {

    TextView tv_question;
    TextView tv_answer;
    Button btn_show_hint;
    Button btn_learned;
    ListBean nowQues;
    boolean isShowAnswer = false;
    Context context;
    boolean readonly;
    int index = 0; // 0:提示一下  1:隐藏答案
    String[] btn_text = {"提示一下", "隐藏答案"};
    int[] ans_visibility = {View.GONE, View.VISIBLE};

    public FillBlankFragment(Context context, ListBean q, boolean readonly) {
        this.context = context;
        this.nowQues = q;
        this.readonly = readonly;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fill_blank, container, false);
        tv_question = view.findViewById(R.id.blank_question);
        tv_answer = view.findViewById(R.id.tv_blank_answer);
        btn_show_hint = view.findViewById(R.id.show_hint);
        btn_learned = view.findViewById(R.id.btn_learned);

        tv_question.setText(nowQues.getQues());

        if (readonly) {
            btn_show_hint.setVisibility(View.GONE);
            btn_learned.setVisibility(View.GONE);
            tv_answer.setVisibility(View.VISIBLE);
            tv_answer.setText(nowQues.getAnsByType());
        } else {
            btn_show_hint.setText(btn_text[index]);
            tv_answer.setVisibility(ans_visibility[index]);
        }

        btn_show_hint.setOnClickListener(v -> {
            index = 1 - index;
            tv_answer.setVisibility(ans_visibility[index]);
            tv_answer.setText(nowQues.getAnsByType());
            btn_show_hint.setText(btn_text[index]);
        });

        btn_learned.setOnClickListener(view1 -> {
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) HttpUtil.httpPut(Ports.reviewQues + nowQues.getId() + "/", new HashMap<>());
                System.out.println(jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert jsonObject != null;
            if (jsonObject.has("error")) {
                try {
                    toast(jsonObject.getString("error"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                toast("设置成功, 记得按时复习");
            }
        });

        return view;
    }

    private void toast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }
}