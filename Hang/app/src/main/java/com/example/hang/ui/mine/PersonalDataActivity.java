package com.example.hang.ui.mine;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.hang.R;

public class PersonalDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        AppCompatButton btn_personal_data_back = findViewById(R.id.personal_data_back);
        btn_personal_data_back.setOnClickListener(view -> PersonalDataActivity.this.finish());
    }
}
