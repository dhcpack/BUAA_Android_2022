package com.example.hang.ui.learn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
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

public class SingleChoiceFragment extends Fragment {

    TextView question;
    RadioButton A;
    RadioButton B;
    RadioButton C;
    RadioButton D;
    Button button;
    ListBean nowQues;
    String correct;
    String answerState="确定";
    Context context;
    Button btn_learned;
    boolean readonly;

    public SingleChoiceFragment(Context context, ListBean q, boolean readonly) {
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
        View view =  inflater.inflate(R.layout.fragment_single_choice, container, false);

        question=view.findViewById(R.id.single_question);
        button=view.findViewById(R.id.next_page);
        btn_learned=view.findViewById(R.id.btn_learned);
        A=view.findViewById(R.id.single_A);
        B=view.findViewById(R.id.single_B);
        C=view.findViewById(R.id.single_C);
        D=view.findViewById(R.id.single_D);

        question.setText(nowQues.getQues());
        String format = nowQues.getAnsByType();
        String[] ABCDans = format.split("%%%");
        A.setText("  A  " + ABCDans[0]);
        B.setText("  B  " + ABCDans[1]);
        C.setText("  C  " + ABCDans[2]);
        D.setText("  D  " + ABCDans[3]);
        correct = ABCDans[4];

        if (readonly) { //查看状态
            button.setText("只读,不应该出现");
            button.setVisibility(View.GONE);
            btn_learned.setVisibility(View.GONE);
            setAns();
        }

        button.setText(answerState);    //确定
        button.setOnClickListener(View -> {
            if (answerState.equals("确定")) {
                if (check()) {
                    answerState = "回答正确";
                    button.setText("回答正确");
                } else {
                    answerState = "回答错误";
                    button.setText("正确答案: " + correct);
                }
                button.setEnabled(false);
                setAns();
            }
        });

        btn_learned.setOnClickListener(view1 -> {
            /*HashMap<String, String> para = new HashMap<>();
            para.put("quesId", String.valueOf(nowQues.getId()));
            System.out.println("debug: quesId " + para.get("quesId"));*/
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

    public void setAns() {
        if (correct.equals("A")) {
            A.setChecked(true);
            B.setEnabled(false);
            C.setEnabled(false);
            D.setEnabled(false);
        } else if (correct.equals("B")) {
            B.setChecked(true);
            A.setEnabled(false);
            C.setEnabled(false);
            D.setEnabled(false);
        } else if (correct.equals("C")) {
            C.setChecked(true);
            A.setEnabled(false);
            B.setEnabled(false);
            D.setEnabled(false);
        } else {
            D.setChecked(true);
            A.setEnabled(false);
            B.setEnabled(false);
            C.setEnabled(false);
        }
    }

    public Boolean check() {
        String answer = "";
        if (A.isChecked()) {
            answer = "A";
        } else if (B.isChecked()) {
            answer = "B";
        } else if (C.isChecked()) {
            answer = "C";
        } else if (D.isChecked()) {
            answer = "D";
        }
        if (correct.equals(answer)) {
            return true;
        } else {
            return false;
        }
    }

    private void toast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }
}