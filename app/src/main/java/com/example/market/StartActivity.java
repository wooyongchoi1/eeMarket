package com.example.market;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // 버튼
        Button searchBtn = findViewById(R.id.searchButton);
        Button questionBtn = findViewById(R.id.QuestionButton);

        // 상품 검색
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // 문의
        questionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, QuestionActivity.class);
                startActivity(intent);
            }
        });

    }
}
