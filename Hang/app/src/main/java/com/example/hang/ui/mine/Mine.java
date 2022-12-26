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

import com.example.hang.R;

public class Mine extends Fragment {

    private MineViewModel mViewModel;

    public static Mine newInstance() {
        return new Mine();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        AppCompatButton btn_enter_personal_data = view.findViewById(R.id.btn_mine_personal_info);
        btn_enter_personal_data.setOnClickListener(view1 -> {
            //跳转到个人资料
            startActivity(new Intent(getActivity(), PersonalDataActivity.class));
        });
        AppCompatButton btn_enter_books = view.findViewById(R.id.btn_mine_books);
        btn_enter_books.setOnClickListener(view2 -> {
            //跳转到展示所有记忆本
            startActivity(new Intent(getActivity(), BooksActivity.class));
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MineViewModel.class);
        // TODO: Use the ViewModel
    }

}