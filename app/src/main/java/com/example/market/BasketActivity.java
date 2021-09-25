package com.example.market;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

// 장바구니 화면을 나타내는 액티비티
public class BasketActivity extends AppCompatActivity{

    //상품 정보를 담고있는 리스트
    ArrayList<BasketDB> BlistAll = null;
    String num = null;

    ListView BlistView = null;
    BasketAdapter BAdapter = null;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        userID = getIntent().getExtras().getString("userID");
        new BackgroundTask().execute();



        //버튼 연결
        Button AllSelBtn = findViewById(R.id.allSelectBtn);
        Button delBtn = findViewById(R.id.deleteBtn);
        Button buyBtn = findViewById(R.id.buyButton);

        //전체 결제 포인트
        //TextView purchase = (TextView)findViewById(R.id.Btext2);

        //데이터 초기화
        //DB와 연결해야함!
        BlistAll = new ArrayList<BasketDB>();
        //new BackgroundTask().execute();


        //어댑터에 데이터 추가
        BAdapter = new BasketAdapter(this,BlistAll);


        //어댑터와 리스트뷰 연결
        BlistView = (ListView)findViewById(R.id.listviewBasket);


        //전체선택
        AllSelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0 ;
                count = BAdapter.getCount() ;

                for (int i=0; i<count; i++) {
                    BlistView.setItemChecked(i, true);
                }
            }
        });

        //선택삭제
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray checkedItems = BlistView.getCheckedItemPositions();
                int count = BAdapter.getCount() ;
                ArrayList<String> id = new ArrayList<>();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = (Boolean) jsonObject.get("success");
                            if(success) {

                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        id.add(BlistAll.get(i).getID());
                        BlistAll.remove(i);
                    }
                }
                BAdapter.notifyDataSetChanged();
                DeleteRequest deleteRequest = null;
                RequestQueue queue = Volley.newRequestQueue(BasketActivity.this);
                for(int i=0;i<id.size();i++){
                    deleteRequest = new DeleteRequest(userID, id.get(i), responseListener);
                    queue.add(deleteRequest);
                }



                // 모든 선택 상태 초기화.
                BlistView.clearChoices();

                BAdapter.notifyDataSetChanged();

            }
        });


        //구매 클릭시 실행
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메시지 출력
                Toast.makeText(getApplicationContext(),"결제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                //결제 추가해야함!


                //결제된 리스트 삭제
                SparseBooleanArray checkedItems = BlistView.getCheckedItemPositions();
                int count = BAdapter.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        BlistAll.remove(i) ;
                    }
                }

                // 모든 선택 상태 초기화.
                BlistView.clearChoices();

                BAdapter.notifyDataSetChanged();

            }
        });

    }
    class BackgroundTask extends AsyncTask<Void, Void, String>{
        //접속할 주소
        String target;
        //String userID = getIntent().getExtras().getString("userID");

        @Override
        protected  void onPreExecute(){
            target = "https://hmm8816.cafe24.com/dbeditor/BasketList.php?userID="+userID;
            Log.d("url", target);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine())!=null){
                    stringBuilder.append(temp+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();

        }

        @Override
        public void onPostExecute(String result){
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String name, price, num, code, image, ID;


                //받아오는 데이터가 없음
                String length = jsonObject.toString();
                Log.d("length", result);


                while(count<jsonArray.length()){
                    // 여기서 while문 들어가지도 않음
                    Log.d("확인", "확인");
                    JSONObject object = jsonArray.getJSONObject(count);
                    name = object.getString("name");
                    price = object.getString("price");
                    num = object.getString("num");
                    code = object.getString("code");
                    image = object.getString("image");
                    ID = object.getString("ID");
                    String c = String.valueOf(count);
                    ProductInfo info = new ProductInfo(code, name, price, image);
                    BasketDB basket = new BasketDB(ID, info, num);
                    BlistAll.add(basket);
                    BlistView.setAdapter(BAdapter);
                    BAdapter.notifyDataSetChanged();

                    count++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}