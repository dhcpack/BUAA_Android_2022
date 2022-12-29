package com.example.hang.ui.learn;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.learn.util.ListBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MultiChoiceFragment extends Fragment {

    TextView question;
    CheckBox A;
    CheckBox B;
    CheckBox C;
    CheckBox D;
    Button button;
    ListBean nowQues;
    String correct;
    String answerState="确定";
    Context context;
    Button btn_learned;
    boolean readonly;
    boolean hasDelete = false;
    private Button btn_delete;

    public MultiChoiceFragment(Context context, ListBean q, boolean readonly, boolean hasDelete) {
        this.context = context;
        this.nowQues = q;
        this.readonly = readonly;
        this.hasDelete = hasDelete;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multi_choice, container, false);

        question=view.findViewById(R.id.mutiple_question);
        button=view.findViewById(R.id.next_page);
        btn_learned=view.findViewById(R.id.btn_learned);
        A=view.findViewById(R.id.mutiple_A);
        B=view.findViewById(R.id.mutiple_B);
        C=view.findViewById(R.id.mutiple_C);
        D=view.findViewById(R.id.mutiple_D);

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

        btn_delete=view.findViewById(R.id.btn_delete);
        if (hasDelete) {
            btn_delete.setVisibility(View.VISIBLE);
        } else {
            btn_delete.setVisibility(View.GONE);
        }
        btn_delete.setOnClickListener(view1 -> {
            ArrayList<String> params = new ArrayList<>();
            params.add(nowQues.getNickname());
            params.add(String.valueOf(nowQues.getBook()));
            params.add(String.valueOf(nowQues.getId()));
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) HttpUtil.httpDelete(Ports.deleteQuestionUrl, params);
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
                toast("删除成功");
                requireActivity().finish();
            }

        });

        button.setText(answerState);    //确定

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

    private void setAns() {
        if (!A.isChecked()) {
            A.setChecked(correct.contains("A"));
        }
        if (!B.isChecked()) {
            B.setChecked(correct.contains("B"));
        }
        if (!C.isChecked()) {
            C.setChecked(correct.contains("C"));
        }
        if (!D.isChecked()) {
            D.setChecked(correct.contains("D"));
        }
        A.setEnabled(correct.contains("A"));
        B.setEnabled(correct.contains("B"));
        C.setEnabled(correct.contains("C"));
        D.setEnabled(correct.contains("D"));
    }

    private void toast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    public Boolean check(){
        StringBuilder answer=new StringBuilder();
        if(A.isChecked()){
            answer.append("A-");
        }
        if(B.isChecked()){
            answer.append("B-");
        }
        if(C.isChecked()){
            answer.append("C-");
        }
        if(D.isChecked()){
            answer.append("D-");
        }
        if(answer.toString().length()!=0) {
            answer.deleteCharAt(answer.length() - 1);
        }
        if (correct.equals(answer.toString())){
            return true;
        } else{
            return false;
        }
    }
}