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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hang.MainActivity;
import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.learn.util.DateUtil;

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
    private Button btn_start_review;
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
    private int book_id = -1;
    private int quesNum = 0;    //题目总数
    private int rest = 0;       //未掌握数量
    private int needReviewNum = 0;
    private TextView tv_book_name;
    private TextView tv_book_tag;
    private TextView tv_percent;
    //private ArrayList<ListBean> allQues = new ArrayList<ListBean>();
    private TextView tv_ok_num;
    private TextView tv_no_num;
    private TextView tv_review_num;

    //进度条
    private ProgressBar progressBar;

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
        progressBar = v.findViewById(R.id.progressbar_learn);
        tv_ok_num = v.findViewById(R.id.tv_okNum);
        tv_no_num = v.findViewById(R.id.tv_noNum);
        tv_review_num = v.findViewById(R.id.tv_reviewNum);
        tv_percent = v.findViewById(R.id.tv_percent);
        if (book_id != -1) {
            try {
                getQuesNum();
                //getAllQues();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


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
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btn_show_items = v.findViewById(R.id.btn_show_items);
        btn_show_items.setOnClickListener(view -> {
            if (book_id == -1) {
                toast("还没有开始学习");
            } else {
                Intent intent = new Intent(getActivity(), ShowItemsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", book_id); // != -1
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_start_study = v.findViewById(R.id.btn_start_study);
        btn_start_study.setOnClickListener(view -> {
            if (book_id == -1) {
                toast("还没有开始学习");
            } else if (process == quesNum) {
                toast("已经学完了, 去复习吧");
            } else {
                Intent intent = new Intent(getActivity(), StartStudyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", book_id); // != -1
                bundle.putString("user", username);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_start_review = v.findViewById(R.id.btn_start_review);
        btn_start_review.setOnClickListener(view -> {
            if (book_id == -1) {
                toast("还没有开始学习");
            } else if (needReviewNum == 0) {
                toast("没有需要复习的内容");
            } else {
                Intent intent = new Intent(getActivity(), StartReviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", book_id); // != -1
                bundle.putString("user", username);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ib_daka = v.findViewById(R.id.ib_daka);
        ib_daka.setOnClickListener(view -> daka());
        ib_look_books = v.findViewById(R.id.ib_look_books);
        ib_look_books.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SystemAllBooksActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            intent.putExtras(bundle);
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
        while (jsonObject == null) {
            try {
                jsonObject = (JSONObject) HttpUtil.httpGet(Ports.checkDetail, user, false);
                System.out.println(jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            //System.out.println(nowDate.substring(0, 10));
            //System.out.println(lastCheckRecord.substring(0, 10));
            if (dakaDays != 0) {
                if (nowDate.substring(0, 10).equals(lastCheckRecord.substring(0, 10))) {
                    //System.out.println("yi");
                    tv_check_state.setText("已");
                }
            }
        }
    }

    public void getLearningBook() throws JSONException {
        ArrayList<String> user = new ArrayList<>();
        user.add(username);
        JSONObject jsonObject = null;
        while (jsonObject == null) {
            try {
                jsonObject = (JSONObject) HttpUtil.httpGet(Ports.learningBookUrl, user, false);
                System.out.println(jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            //process = Integer.parseInt(jsonObject.getString("process"));
            book_id = Integer.parseInt(jsonObject.getString("id"));
        }
    }

    public void getQuesNum() throws JSONException {
        ArrayList<String> user = new ArrayList<>();
        user.add(username);
        JSONObject o = null;
        while (o == null) {
            try {
                o = (JSONObject) HttpUtil.httpGet(Ports.getReviewCount, user, false);
                System.out.println(o);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert o != null;
        rest = o.getInt("未学习");
        int learned = o.getInt("已学习");
        needReviewNum = o.getInt("待复习");
        quesNum = rest + process + needReviewNum;
        process = learned + needReviewNum;
        progressBar.setProgress(process);
        progressBar.setMax(quesNum);
        setText();
    }

    private void setText() {
        tv_ok_num.setText("已掌握\n    " + process);
        tv_no_num.setText("未掌握\n    " + rest);
        tv_review_num.setText("待复习\n    " + needReviewNum);
        double p = 0;
        if (quesNum != 0) {
            p = (double) process / (double) quesNum;
        }
        tv_percent.setText(String.format("学习进度%.2f", p * 100) + "%");
        progressBar.setProgress(process);
        progressBar.setMax(quesNum);
    }

    private void toast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getQuesNum();
            System.out.println("resume " + process);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LearnViewModel.class);
        // TODO: Use the ViewModel
    }
}