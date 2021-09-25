package com.example.market;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

//포인트를 업데이트하는 클래스
public class UpdatePointRequest extends StringRequest {
    final static private String URL = "https://hmm8816.cafe24.com/dbeditor/UpdatePoint.php?";
    private Map<String , String> parameters;


    public UpdatePointRequest(String userID, String point, Response.Listener<String> listener) {
        super(Method.POST, URL+"userID="+userID+"&point="+point, listener, null);
        Log.d("userID:",userID);
        Log.d("point:",point);

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}