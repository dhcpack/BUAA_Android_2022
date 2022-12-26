package com.example.hang.ui.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hang.R;

import java.util.Objects;

public class CreateBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);
        Objects.requireNonNull(getSupportActionBar()).setTitle("创建记忆本");

    }
}