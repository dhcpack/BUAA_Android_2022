package com.example.hang.ui.learn;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hang.MainActivity;
import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.learn.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Learn extends Fragment {

    private LearnViewModel mViewModel;

    private ImageButton ib_search;
    private Button btn_show_items;
    private Button btn_start_study;
    private Button btn_learn_settings;
    private ImageButton ib_daka;
    private ImageButton ib_look_books;
    private ImageButton ib_create_book;
    private TextView tv_daka_days;
    private TextView tv_check_state;
    private String username;
    private int dakaDays;
    private String lastCheckRecord;

    //learning book
    private String book_name;
    private String book_tag;
    private int process;
    private int book_id;
    private int quesNum = 0;    //题目总数
    private int rest = 0;       //未掌握数量
    private TextView tv_book_name;
    private TextView tv_book_tag;
    private TextView tv_percent;
    private ArrayList<JSONObject> allQues = new ArrayList<>();
    private TextView tv_ok_num;
    private TextView tv_no_num;

    public static Learn newInstance() {
        return new Learn();
    }

    @SuppressLint({"MissingInflatedId", "ResourceType", "SetTextI18n"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_learn, container, false);

        username = ((MainActivity) requireActivity()).getIntent().getExtras().getString("username");

        try {
            getLearningBook();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_book_name = v.findViewById(R.id.tv_book_name);
        tv_book_tag = v.findViewById(R.id.tv_book_tag);
        tv_book_name.setText(book_name);
        tv_book_tag.setText(book_tag);
        if (book_id != -1) {
            try {
                getAllQues();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        tv_ok_num = v.findViewById(R.id.tv_okNum);
        tv_no_num = v.findViewById(R.id.tv_noNum);
        tv_ok_num.setText("已掌握\n    " + process);
        tv_no_num.setText("未掌握\n    " + rest);
        tv_percent = v.findViewById(R.id.tv_percent);
        double p = 0;
        if (quesNum != 0) {
            p = (double) process / (double) quesNum;
        }
        tv_percent.setText("学习进度" + p + "%");

        tv_daka_days = v.findViewById(R.id.tv_daka_days);
        tv_check_state = v.findViewById(R.id.tv_check_state);
        tv_check_state.setText("未");
        try {
            getInitDakaDays();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_daka_days.setText(String.valueOf(dakaDays));

        ib_search = v.findViewById(R.id.ib_search);
        ib_search.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LearnSearchActivity.class);
            startActivity(intent);
        });
        btn_show_items = v.findViewById(R.id.btn_show_items);
        btn_show_items.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ShowItemsActivity.class);
            startActivity(intent);
        });
        btn_start_study = v.findViewById(R.id.btn_start_study);
        btn_start_study.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), StartStudyActivity.class);
            startActivity(intent);
        });
        btn_learn_settings = v.findViewById(R.id.btn_learn_settings);
        btn_learn_settings.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LearnSettingsActivity.class);
            startActivity(intent);
        });

        ib_daka = v.findViewById(R.id.ib_daka);
        ib_daka.setOnClickListener(view -> daka());
        ib_look_books = v.findViewById(R.id.ib_look_books);
        ib_look_books.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SystemAllBooksActivity.class);
            startActivity(intent);
        });
        ib_create_book = v.findViewById(R.id.ib_create_book);
        ib_create_book.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CreateBookActivity.class);
            Bundle b = new Bundle();
            b.putString("username", username);
            intent.putExtras(b);
            startActivity(intent);
        });
        return v;
    }


    public void daka() {
        String nowDate = DateUtil.getNowTime();
        if (nowDate.substring(0, 10).equals(lastCheckRecord.substring(0, 10))) {
            toast("今日已打卡, 明天再来吧!");
        } else {
            dakaDays++;
            tv_daka_days.setText(String.valueOf(dakaDays));
            tv_check_state.setText("已");
            HashMap<String, String> check = new HashMap<>();
            check.put("nickname", username);
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) HttpUtil.httpPost(Ports.checkUrl, check);
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
                toast("打卡成功, 再接再厉!");
            }
        }
    }

    public void getInitDakaDays() throws JSONException {
        ArrayList<String> user = new ArrayList<>();
        user.add(username);
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) HttpUtil.httpGet(Ports.checkDetail, user, false);
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
            dakaDays = Integer.parseInt(jsonObject.getString("totalDays"));
            lastCheckRecord = jsonObject.getString("lastCheckRecord");
            String nowDate = DateUtil.getNowTime();
            System.out.println(nowDate.substring(0, 10));
            System.out.println(lastCheckRecord.substring(0, 10));
            if (nowDate.substring(0, 10).equals(lastCheckRecord.substring(0, 10))) {
                //System.out.println("yi");
                tv_check_state.setText("已");
            }
        }
    }

    public void getLearningBook() throws JSONException {
        ArrayList<String> user = new ArrayList<>();
        user.add(username);
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) HttpUtil.httpGet(Ports.learningBookUrl, user, false);
            System.out.println(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert jsonObject != null;
        if (jsonObject.has("error")) {
            book_name = "去添加记忆本吧~";
            book_tag = "未开始学习";
            process = 0;
            book_id = -1;
        } else {
            book_name = jsonObject.getString("bookname");
            book_tag = jsonObject.getString("tag");
            process = Integer.parseInt(jsonObject.getString("process"));
            book_id = Integer.parseInt(jsonObject.getString("id"));
        }
    }

    private void getAllQues() throws JSONException, IOException {
        ArrayList<String> id = new ArrayList<>();
        id.add(String.valueOf(book_id));
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) HttpUtil.httpGet(Ports.getQuestionUrl, id, true);
            System.out.println(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert jsonArray != null;
        int l = jsonArray.length();
        for (int i = 0; i < l; ++i) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            if (!jsonObject.has("error")) {
                allQues.add(jsonObject);
            }
        }
        quesNum = allQues.size();
        rest = quesNum - process;
    }

    private void toast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LearnViewModel.class);
        // TODO: Use the ViewModel
    }
}