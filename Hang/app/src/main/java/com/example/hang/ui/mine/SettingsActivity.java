package com.example.hang.ui.mine;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hang.R;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        String username = getIntent().getStringExtra("username");
        ArrayList<String> params = new ArrayList<>();
        params.add(username);
    }
}
