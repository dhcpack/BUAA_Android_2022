package com.example.hang.ui.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.SearchView;

import com.example.hang.R;

public class LearnSearchActivity extends AppCompatActivity {

    private SearchView sv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_search);
        sv = findViewById(R.id.sv_search);
        sv.onActionViewExpanded();
    }
}