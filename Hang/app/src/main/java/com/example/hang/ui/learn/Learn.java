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

import com.example.hang.MainActivity;
import com.example.hang.R;

import java.util.Objects;

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
    private String username;

    public static Learn newInstance() {
        return new Learn();
    }

    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_learn, container, false);

        username = ((MainActivity) requireActivity()).getIntent().getExtras().getString("username");

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
        tv_daka_days = v.findViewById(R.id.tv_daka_days);
        tv_daka_days.setText("70");
        ib_daka = v.findViewById(R.id.ib_daka);
        ib_daka.setOnClickListener(view -> {
            //TODO
        });
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LearnViewModel.class);
        // TODO: Use the ViewModel
    }
}