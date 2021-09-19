package com.example.market;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class RegisterRequest extends StringRequest {
    final static private String URL = "https://hmm8816.cafe24.com/dbeditor/UserRegister.php?";
    private Map<String , String> parameters;


    public RegisterRequest(String userID, String userPassword, String userEmail, Response.Listener<String> listener) {
        super(Method.POST, URL+"userID="+userID+"&userPassword="+userPassword+"&userEmail="+userEmail, listener, null);

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
