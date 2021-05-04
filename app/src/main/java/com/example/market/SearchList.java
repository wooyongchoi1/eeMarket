package com.example.market;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchList extends AppCompatActivity {
    ArrayList<String> data = new ArrayList<>();
    List<ProductInfo> list ;
    String url = "https://openapi.11st.co.kr/openapi/OpenApiService.tmall?key=7b271df5c1091d846fe1e114f48e9f0e&apiCode=ProductSearch&keyword=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchlist);
        url += getIntent().getExtras().getString("keyword");
        url += "&pageNum=1&pageSize=10";
        Log.d("url", url);
        ListView listView = (ListView)findViewById(R.id.listview);
        RestParser parser = new RestParser(url);
        try {
            list = parser.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(ProductInfo p : list){
            data.add(p.getName());
        }

        final MyAdapter myAdapter = new MyAdapter(this, data);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Intent intent = new Intent(SearchList.this, MainActivity.class);
                intent.putExtra("code", list.get(position).getCode());
                startActivity(intent);
            }
        });
    }
}
