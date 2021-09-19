package com.example.market;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionActivity extends AppCompatActivity {
//    final int PERMISSION = 1;
//    Button stt, searchKeyword;
//    SpeechRecognizer mRecognizer;
//    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_);


        TextView textView = (TextView)findViewById(R.id.textview);
        textView.setText("자주 묻는 질문");

        TextView textView1 = (TextView)findViewById(R.id.textview1);
        textView1.setText("Q1) 어떤 쇼핑몰 정보가 제공되는 건가요?");

        TextView textView2 = (TextView)findViewById(R.id.textview2);
        textView2.setText("A1) 현재 지원하고 있는 쇼핑몰은 11번가입니다.");

        TextView textView3 = (TextView)findViewById(R.id.textview3);
        textView3.setText("Q2) 하단의 상품 정보가 나오지 않아요.");

        TextView textView4 = (TextView)findViewById(R.id.textview4);
        textView4.setText("A2) 상품 정보를 읽는 시간이 필요하기 때문에, 위에 있는 상품 정보부터 읽도록 구성되어 있습니다. 위에서부터 정보를 읽어주세요.");

        TextView textView5 = (TextView)findViewById(R.id.textview5);
        textView5.setText("Q3) ");

        TextView textView6 = (TextView)findViewById(R.id.textview6);
        textView6.setText("A3) ");

        TextView textView7 = (TextView)findViewById(R.id.textview7);
        textView7.setText("Q4) 문의 드리고 싶으면 어디로 전화하면 되나요?");

        TextView textView8 = (TextView)findViewById(R.id.textview8);
        textView8.setText("A4) 하단의 버튼을 클릭하시면 연락처를 확인하실 수 있습니다.");

        Button mDialogCall = (Button) findViewById(R.id.btnDialog);

        // 문의
        mDialogCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(StartActivity.this, QuestionActivity.class);
//                startActivity(intent);
                String tel = "tel:01041516527";
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));

            }
        });

    }
}
