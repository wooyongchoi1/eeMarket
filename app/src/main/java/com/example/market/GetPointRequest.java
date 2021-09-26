package com.example.market;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

//포인트를 가져오는 클래스
public class GetPointRequest extends StringRequest {
    final static private String URL = "https://hmm8816.cafe24.com/dbeditor/GetPoint.php?";
    private Map<String, String> parameters;


    public GetPointRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL + "userID=" + userID, listener, null);
        Log.d("userID:", userID);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}