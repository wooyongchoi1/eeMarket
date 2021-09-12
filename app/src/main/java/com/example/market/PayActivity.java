package com.example.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PayActivity extends AppCompatActivity {
    String[] items={"1","2","3","4","5","6","7","8","9","10"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_start);


        // Spinner
        Spinner numSpinner = (Spinner) findViewById(R.id.spinner_num);
        // 장바구니
        Button saveBtn = (Button)findViewById(R.id.saveButton);

        // spinner
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numSpinner.setAdapter(adapter);

        // 장바구니
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"장바구니에 상품이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                //서버 장바구니에 상품 개수만큼 추가
           }
        });

        //물품 가격 추가

    }
}

