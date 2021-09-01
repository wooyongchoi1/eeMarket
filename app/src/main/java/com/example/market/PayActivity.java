package com.example.market;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class PayActivity extends AppCompatActivity {
    String[] items={"1","2","3","4","5","6","7","8","9","10"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_start);


        // Spinner
        Spinner numSpinner = (Spinner) findViewById(R.id.spinner_num);

        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numSpinner.setAdapter(adapter);


    }
}

