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
import android.widget.Toast;

import com.example.hang.R;
import com.example.hang.ports.HttpUtil;
import com.example.hang.ports.Ports;
import com.example.hang.ui.learn.util.ListBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class ImageFragment extends Fragment {

    private final Context context;
    private final ListBean nowQues;
    private final boolean readonly;

    ImageView iv_pic_ques;
    Button btn_learned;

    public ImageFragment(Context context, ListBean q, boolean readonly) {
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
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        iv_pic_ques = view.findViewById(R.id.image_ques);
        btn_learned = view.findViewById(R.id.btn_learned_img);

        // TODO
        iv_pic_ques.setImageResource(R.drawable.card2);

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