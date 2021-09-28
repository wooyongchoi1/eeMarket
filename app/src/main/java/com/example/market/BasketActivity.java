package com.example.market;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

// 장바구니 화면을 나타내는 액티비티
public class BasketActivity extends AppCompatActivity {

    //상품 정보를 담고있는 리스트
    ArrayList<BasketDB> BlistAll = null;
    String num = null;

    ListView BlistView = null;
    BasketAdapter BAdapter = null;
    String userID;

    String userpoint = "0";

    String AllPrice = "0";
    String mid = "0";
    int mid1 = 0;
    TextView allP;
    Button AllSelBtn;
    Button delBtn;
    Button buyBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        userID = getIntent().getExtras().getString("userID");
        new BackgroundTask().execute();


        //버튼 연결
        AllSelBtn = findViewById(R.id.allSelectBtn);
        delBtn = findViewById(R.id.deleteBtn);
        buyBtn = findViewById(R.id.buyButton);
        //보유 포인트
        allP = findViewById(R.id.Btext4);

        //전체 결제 포인트
        //TextView purchase = (TextView)findViewById(R.id.Btext2);

        //데이터 초기화
        //DB와 연결해야함!
        BlistAll = new ArrayList<BasketDB>();
        //new BackgroundTask().execute();


        //어댑터에 데이터 추가
        BAdapter = new BasketAdapter(this, BlistAll);


        //어댑터와 리스트뷰 연결
        BlistView = (ListView) findViewById(R.id.listviewBasket);

        //보유 포인트 불러오기
        // 포인트 가져오기
        Response.Listener<String> responseListener2 = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = (Boolean) jsonObject.get("success");
                    userpoint = (String) jsonObject.get("userPoint");

                    if (success) {
                        Log.d("userpoint", userpoint);
//                        Toast.makeText(getApplicationContext(), "가져오기 성공" + userpoint, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "포인트 가져오기 실패", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                GetPointRequest getPRequest = new GetPointRequest(userID, responseListener2);
                RequestQueue queue = Volley.newRequestQueue(BasketActivity.this);
                queue.add(getPRequest);
            }
        }, 2000);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                allP.setText(userpoint);
            }
        }, 3000);


        //전체선택
        AllSelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                count = BAdapter.getCount();

                for (int i = 0; i < count; i++) {
                    BlistView.setItemChecked(i, true);
                }
            }
        });

        //선택삭제
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray checkedItems = BlistView.getCheckedItemPositions();
                int count = BAdapter.getCount();
                ArrayList<String> id = new ArrayList<>();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = (Boolean) jsonObject.get("success");
                            if (success) {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                for (int i = count - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        id.add(BlistAll.get(i).getID());
                        BlistAll.remove(i);
                    }
                }
                BAdapter.notifyDataSetChanged();
                DeleteRequest deleteRequest = null;
                RequestQueue queue = Volley.newRequestQueue(BasketActivity.this);
                for (int i = 0; i < id.size(); i++) {
                    deleteRequest = new DeleteRequest(userID, id.get(i), responseListener);
                    queue.add(deleteRequest);
                }


                // 모든 선택 상태 초기화.
//                BlistView.clearChoices();

                BAdapter.notifyDataSetChanged();

            }
        });


        //구매 클릭시 실행
        buyBtn.setOnClickListener(v-> {

                //계산
                mid1 = Integer.parseInt(userpoint);
                mid1 = mid1 - Integer.parseInt(AllPrice);

                if (mid1 < 0) {
                    Toast.makeText(getApplicationContext(), "결제 실패. 보유 포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, CheckPasswordActivity.class);
                    intent.putExtra("userID", userID);
                    startActivityForResult(intent, 5);
                }

        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data.getExtras().getBoolean("check")){
                Toast.makeText(getApplicationContext(), "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                // 포인트 차감후 다시 포인트 가져오기
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {

                    public void run() {

                        userpoint = String.valueOf(mid1);

                        Response.Listener<String> responseListener1 = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = (Boolean) jsonObject.get("success");

                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "포인트 업데이트 완료 " + userpoint + " 포인트", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "포인트 업데이트 실패", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };

                        UpdatePointRequest upPRequest = new UpdatePointRequest(userID, userpoint, responseListener1);
                        RequestQueue queue = Volley.newRequestQueue(BasketActivity.this);
                        queue.add(upPRequest);
                    }
                }, 1000);

                // 포인트 가져오기
                Response.Listener<String> responseListener2 = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = (Boolean) jsonObject.get("success");
                            userpoint = (String) jsonObject.get("userPoint");

                            if (success) {
                                Log.d("userpoint", userpoint);
//                        Toast.makeText(getApplicationContext(), "가져오기 성공" + userpoint, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "포인트 가져오기 실패", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        GetPointRequest getPRequest = new GetPointRequest(userID, responseListener2);
                        RequestQueue queue = Volley.newRequestQueue(BasketActivity.this);
                        queue.add(getPRequest);
                    }
                }, 2000);

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        allP.setText(userpoint);
                    }
                }, 3000);

                delBtn.callOnClick();

//

                BAdapter.notifyDataSetChanged();
            }
        }
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        //접속할 주소
        String target;
        //String userID = getIntent().getExtras().getString("userID");

        @Override
        protected void onPreExecute() {
            target = "https://hmm8816.cafe24.com/dbeditor/BasketList.php?userID=" + userID;
            Log.d("url", target);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();

        }

        @Override
        public void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String name, price, num, code, image, ID;


                //받아오는 데이터가 없음
                String length = jsonObject.toString();
                Log.d("length", result);


                while (count < jsonArray.length()) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            //합계 계산
            //리스트뷰 클릭시 실행
            //클릭할때마다 총 포인트를 계산함
            BlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SparseBooleanArray checkedItems = BlistView.getCheckedItemPositions();
                    int count = BAdapter.getCount();

                    mid = "0";

                    for (int i = count - 1; i >= 0; i--) {

                        if (checkedItems.get(i)) {
                            mid = String.valueOf(Integer.parseInt(mid) + Integer.parseInt(BlistAll.get(i).getPrice()) * Integer.parseInt(BlistAll.get(i).getNum()));

                        }
                    }

                    AllPrice = mid;

                    Log.d("allPoint", AllPrice);
                    //총 결제 포인트
                    TextView BAllPrice = (TextView) findViewById(R.id.Btext2);
                    BAllPrice.setText(AllPrice);

                }
            });
        }
    }
}