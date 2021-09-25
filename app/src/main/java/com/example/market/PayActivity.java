package com.example.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PayActivity extends AppCompatActivity {
    String[] items={"1","2","3","4","5","6","7","8","9","10"};
    public String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Boolean jsonResponse = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_start);
        String userID = getIntent().getExtras().getString("userID");
        ProductInfo productInfo = new MarketIntent(getIntent()).getProductInfo();
        // Spinner
        Spinner numSpinner = (Spinner) findViewById(R.id.spinner_num);
        // 장바구니
        Button saveBtn = (Button)findViewById(R.id.saveButton);
        //전체 결제 포인트
        TextView APointText = (TextView)findViewById(R.id.Ptext2);
        //바로 구매
        Button purchase = (Button)findViewById(R.id.payButton);

        // spinner
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numSpinner.setAdapter(adapter);



        //제품 1개 따른 가격
        //int ProductSum = Integer.parseInt(price)*Integer.parseInt(num);

        // 장바구니
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //서버 장바구니에 상품 개수만큼 추가
                String num = numSpinner.getSelectedItem().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response){

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = (Boolean) jsonObject.get("success");

                            if(success){
                                Toast.makeText(getApplicationContext(),"장바구니에 상품이 추가완료", Toast.LENGTH_SHORT).show();

                                numSpinner.setEnabled(false);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                PayRequest payRequest = new PayRequest(userID, productInfo.getName(), productInfo.getPrice(), num, productInfo.getCode(), productInfo.getImage(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(PayActivity.this);
                queue.add(payRequest);

           }
        });

        numSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            //스피너에서 개수 선택시 실행
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //결제 합계 계산
                int mid = Integer.parseInt(numSpinner.getSelectedItem().toString());
                //상품 가격 가져옴
                mid = mid * Integer.parseInt(productInfo.getPrice());

                APointText.setText(String.valueOf(mid));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //바로 구매 버튼 누르면, 장바구니로 들어가기
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//서버 장바구니에 상품 개수만큼 추가
                String num = numSpinner.getSelectedItem().toString();


                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response){

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = (Boolean) jsonObject.get("success");

                            if(success){
                                Toast.makeText(getApplicationContext(),"장바구니에 상품이 추가완료", Toast.LENGTH_SHORT).show();

                                numSpinner.setEnabled(false);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                PayRequest payRequest = new PayRequest(userID, productInfo.getName(), productInfo.getPrice(), num, productInfo.getCode(), productInfo.getImage(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(PayActivity.this);
                queue.add(payRequest);
                Intent intent = new Intent(PayActivity.this, BasketActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);

            }
        });


    }
}

