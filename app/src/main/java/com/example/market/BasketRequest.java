package com.example.market;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.util.Map;

public class BasketRequest extends StringRequest {
    final static private String URL = "https://hmm8816.cafe24.com/dbeditor/BasketList.php?";
    private Map<String , String> parameters;


    public BasketRequest(String userID, String name, String price, String num, String code, String image, Response.Listener<String> listener) {
        super(Request.Method.POST, URL+"userID="+userID+"&name="+name+"&price="+price+"&num="+num+"&code="+code+"&image="+image, listener, null);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}
