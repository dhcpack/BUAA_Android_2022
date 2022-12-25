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
    private AppCompatButton btn_enter_personal_data;

    public static Mine newInstance() {
        return new Mine();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        btn_enter_personal_data = view.findViewById(R.id.btn_mine_personal);
        btn_enter_personal_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到个人资料
                startActivity(new Intent(getActivity(), PersonalDataActivity.class));
            }
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