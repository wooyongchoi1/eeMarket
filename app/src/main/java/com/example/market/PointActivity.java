package com.example.market;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PointActivity extends AppCompatActivity {
//포인트를 충전하는 클래스

    int mid = 0;
    String userpoint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        Button PCBtn1 = findViewById(R.id.PointChargeButton1);
        Button PCBtn2 = findViewById(R.id.PointChargeButton2);
        Button PCBtn3 = findViewById(R.id.PointChargeButton3);
        Button PCBtn4 = findViewById(R.id.PointChargeButton4);
        Button PCBtn5 = findViewById(R.id.PointChargeButton5);
        TextView APoint = (TextView) findViewById(R.id.AllPoint);

        String userID = getIntent().getExtras().getString("userID");

        // 포인트 가져오기
        Response.Listener<String> responseListener2 = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = (Boolean) jsonObject.get("success");
                    userpoint = (String) jsonObject.get("userPoint");

                    if (success) {
//                        Toast.makeText(getApplicationContext(), "가져오기 성공" + userpoint, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "포인트 가져오기 실패", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        GetPointRequest getPRequest = new GetPointRequest(userID, responseListener2);
        RequestQueue queue = Volley.newRequestQueue(PointActivity.this);
        queue.add(getPRequest);


        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() { public void run() {
            APoint.setText(userpoint);
        } }, 1000);


        //1000 버튼 클릭시 실행(모든 버튼은 코드구조가 동일함)
        PCBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHandler.postDelayed(new Runnable() {

                    public void run() {
                        mid = Integer.parseInt(userpoint);
                        mid = mid + 1000;

                        userpoint = String.valueOf(mid);

                        Response.Listener<String> responseListener1 = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = (Boolean) jsonObject.get("success");

                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "포인트 충전 완료 " + userpoint + " 포인트", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "포인트 업데이트 실패", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };


                        UpdatePointRequest upPRequest = new UpdatePointRequest(userID, userpoint, responseListener1);
                        queue.add(upPRequest);
                    }
                }, 1000);

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        GetPointRequest getPRequest = new GetPointRequest(userID, responseListener2);
                        queue.add(getPRequest);
                    }
                }, 2000);

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        APoint.setText(userpoint);
                    }
                }, 3000);

            }
        });

        //5000 버튼 클릭시 실행
        PCBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHandler.postDelayed(new Runnable() {

                    public void run() {
                        mid = Integer.parseInt(userpoint);
                        mid = mid + 5000;

                        userpoint = String.valueOf(mid);

                        Response.Listener<String> responseListener1 = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = (Boolean) jsonObject.get("success");

                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "포인트 충전 완료 " + userpoint + " 포인트", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "포인트 업데이트 실패", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };


                        UpdatePointRequest upPRequest = new UpdatePointRequest(userID, userpoint, responseListener1);
                        queue.add(upPRequest);
                    }
                }, 1000);

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        GetPointRequest getPRequest = new GetPointRequest(userID, responseListener2);
                        queue.add(getPRequest);
                    }
                }, 2000);

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        APoint.setText(userpoint);
                    }
                }, 3000);



            }
        });

        //10000
        PCBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHandler.postDelayed(new Runnable() {

                    public void run() {
                        mid = Integer.parseInt(userpoint);
                        mid = mid + 10000;

                        userpoint = String.valueOf(mid);

                        Response.Listener<String> responseListener1 = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = (Boolean) jsonObject.get("success");

                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "포인트 충전 완료 " + userpoint + " 포인트", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "포인트 업데이트 실패", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };

                        UpdatePointRequest upPRequest = new UpdatePointRequest(userID, userpoint, responseListener1);
                        queue.add(upPRequest);
                    }
                }, 1000);

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        GetPointRequest getPRequest = new GetPointRequest(userID, responseListener2);
                        queue.add(getPRequest);
                    }
                }, 2000);

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        APoint.setText(userpoint);
                    }
                }, 3000);


            }
        });
        //50000
        PCBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHandler.postDelayed(new Runnable() {

                    public void run() {
                        mid = Integer.parseInt(userpoint);
                        mid = mid + 50000;

                        userpoint = String.valueOf(mid);

                        Response.Listener<String> responseListener1 = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = (Boolean) jsonObject.get("success");

                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "포인트 충전 완료 " + userpoint + " 포인트", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "포인트 업데이트 실패", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };

                        UpdatePointRequest upPRequest = new UpdatePointRequest(userID, userpoint, responseListener1);
                        queue.add(upPRequest);
                    }
                }, 1000);

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        GetPointRequest getPRequest = new GetPointRequest(userID, responseListener2);
                        queue.add(getPRequest);
                    }
                }, 2000);

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        APoint.setText(userpoint);
                    }
                }, 3000);


            }
        });

        //100000
        PCBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHandler.postDelayed(new Runnable() {

                    public void run() {
                        mid = Integer.parseInt(userpoint);
                        mid = mid + 100000;

                        userpoint = String.valueOf(mid);

                        Response.Listener<String> responseListener1 = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = (Boolean) jsonObject.get("success");

                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "포인트 충전 완료 " + userpoint + " 포인트", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "포인트 업데이트 실패", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };


                        UpdatePointRequest upPRequest = new UpdatePointRequest(userID, userpoint, responseListener1);
                        queue.add(upPRequest);
                    }
                }, 1000);

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        GetPointRequest getPRequest = new GetPointRequest(userID, responseListener2);
                        queue.add(getPRequest);
                    }
                }, 2000);

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        APoint.setText(userpoint);
                    }
                }, 3000);

            }
        });

    }

}