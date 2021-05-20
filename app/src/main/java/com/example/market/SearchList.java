package com.example.market;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchList extends AppCompatActivity {
    ArrayList<ProductInfo> list = new ArrayList<>();
    ListView listView;
    MyAdapter myAdapter;
    Button preButton, postButton;
    TextView pageNumTextView;

    String url = "https://openapi.11st.co.kr/openapi/OpenApiService.tmall?key=7b271df5c1091d846fe1e114f48e9f0e&apiCode=ProductSearch&keyword=";
    int pageNum = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchlist);
        url += getIntent().getExtras().getString("keyword");

        listView = (ListView)findViewById(R.id.listview);
        preButton = findViewById(R.id.preButton);
        postButton = findViewById(R.id.postButton);
        pageNumTextView = findViewById(R.id.pageNum);
        myAdapter = new MyAdapter(this, list);
        preButton.setOnClickListener(v->{
            pageNum--;
            onResume();
        });
        postButton.setOnClickListener(v->{
            pageNum++;
            onResume();
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Intent intent = new Intent(SearchList.this, MainActivity.class);
                intent.putExtra("code", list.get(position).getCode());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(pageNum ==1){ preButton.setEnabled(false); }
        else{ preButton.setEnabled(true); }
        pageNumTextView.setText(pageNum+"페이지");

        RestParser parser = new RestParser(url + "&pageNum="+pageNum+"&pageSize=10");
        try {
            list = parser.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        
        myAdapter.setSample(list);
        listView.setAdapter(myAdapter);


        if(list.size() < 10){ postButton.setEnabled(false); }
        else{postButton.setEnabled(true);}
    }
}
