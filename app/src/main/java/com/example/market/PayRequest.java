package com.example.market;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class PayRequest extends StringRequest {
    final static private String URL = "https://hmm8816.cafe24.com/dbeditor/PayRegister.php?";
    private Map<String , String> parameters;


    public PayRequest(String userID,String name,String price, String num, String code,String image, Response.Listener<String> listener) {
        super(Method.POST, URL+"userID="+userID+"&name="+name+"&price="+price+"&num="+num+"&code="+code+"&image="+image, listener, null);
        /*Log.d("userID(TEST):",userID);
        Log.d("name(TEST):",name);
        Log.d("price(TEST):",price);
        Log.d("num(TEST):",num);
        Log.d("code(TEST):",code);
        Log.d("image(TEST):",image);*/
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
