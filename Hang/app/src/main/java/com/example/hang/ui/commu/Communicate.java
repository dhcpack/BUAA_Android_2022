package com.example.hang.ui.commu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hang.R;
import com.example.hang.ui.commu.friends.FriendsListActivity;
import com.example.hang.ui.commu.posts.PostsListActivity;

public class Communicate extends Fragment {

    private CommunicateViewModel mViewModel;
    private String username;
    private int type = 0;

    public static Communicate newInstance() {
        return new Communicate();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communicate, container, false);


        //获取传过来的数据
        Bundle bundle = getActivity().getIntent().getExtras();
        //System.out.println(bundle.getString("username"));
        username = bundle.getString("username");

        //按钮监听
        AppCompatButton btn_comu_friends = view.findViewById(R.id.btn_comu_friends);
        btn_comu_friends.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), FriendsListActivity.class);
            intent.putExtra("username", username);
            // 聊天系统
            startActivity(intent);
        });
        AppCompatButton btn_comu_books = view.findViewById(R.id.btn_comu_posts);
        btn_comu_books.setOnClickListener(view2 -> {
            Intent intent = new Intent(getActivity(), PostsListActivity.class);
            intent.putExtra("username", username);
            // 帖子系统
            startActivity(intent);
        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CommunicateViewModel.class);
        // TODO: Use the ViewModel
    }

}