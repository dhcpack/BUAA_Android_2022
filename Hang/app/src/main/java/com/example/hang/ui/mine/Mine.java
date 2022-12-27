package com.example.hang.ui.mine;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.mine.MyBooks.MyBooksActivity;
import com.example.hang.ui.mine.PersonalData.PersonalDataActivity;
import com.example.hang.ui.mine.Report.ReportActivity;
import com.example.hang.ui.mine.Settings.SettingsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Mine extends Fragment {

    private MineViewModel mViewModel;
    private String username;

    public static Mine newInstance() {
        return new Mine();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        //获取传过来的数据
        Bundle bundle = getActivity().getIntent().getExtras();
        //System.out.println(bundle.getString("username"));
        username = bundle.getString("username");

        //设置个人信息
        setUserInfo(view);

        //按钮监听
        AppCompatButton btn_enter_personal_data = view.findViewById(R.id.btn_mine_personal_info);
        btn_enter_personal_data.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
            intent.putExtra("username", username);
            //跳转到个人资料
            startActivity(intent);
        });
        AppCompatButton btn_enter_books = view.findViewById(R.id.btn_mine_books);
        btn_enter_books.setOnClickListener(view2 -> {
            Intent intent = new Intent(getActivity(), MyBooksActivity.class);
            intent.putExtra("username", username);
            //跳转到展示所有记忆本
            startActivity(intent);
        });
        AppCompatButton btn_mine_gen_report = view.findViewById(R.id.btn_mine_gen_report);
        btn_mine_gen_report.setOnClickListener(view2 -> {
            Intent intent = new Intent(getActivity(), ReportActivity.class);
            intent.putExtra("username", username);
            //跳转到生成学习报告
            startActivity(intent);
        });
        AppCompatButton btn_mine_settings = view.findViewById(R.id.btn_mine_settings);
        btn_mine_settings.setOnClickListener(view2 -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            intent.putExtra("username", username);
            //跳转到设置
            startActivity(intent);
        });
        return view;
    }

    private void setUserInfo(View view) {
        ArrayList<String> params = new ArrayList<>();
        params.add(username);
        try {
            JSONObject userInfo = (JSONObject) HttpUtil.httpGet(Ports.userDetailUrl, params, false);
            try {
                String stuId = userInfo.getString("stuId");
                String username = userInfo.getString("nickname");
                TextView tv_stu_id = view.findViewById(R.id.tv_stu_id);
                TextView tv_username = view.findViewById(R.id.tv_username);
                ImageView iv_person_data_avatar = view.findViewById(R.id.iv_person_data_avatar);
                tv_stu_id.setText(stuId);
                tv_username.setText(username);
                String sex = userInfo.getString("sex");
                if (sex.equals("true")) {
                    iv_person_data_avatar.setImageResource(R.drawable.ic_default_avatar_male);
                } else {
                    iv_person_data_avatar.setImageResource(R.drawable.ic_default_avatar_female);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MineViewModel.class);
        // TODO: Use the ViewModel
    }

}