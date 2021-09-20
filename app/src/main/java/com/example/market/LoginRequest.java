package com.example.market;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class LoginRequest extends StringRequest {
    final static private String URL = "https://hmm8816.cafe24.com/dbeditor/UserLogin.php?";
    private Map<String , String> parameters;


    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener) {
        super(Request.Method.POST, URL+"userID="+userID+"&userPassword="+userPassword, listener, null);

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}