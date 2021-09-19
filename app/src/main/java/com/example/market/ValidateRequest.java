package com.example.market;
// 현재 회원가입이 가능한지 check하는 class
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class ValidateRequest extends StringRequest {
    final static private String URL = "http://hmm8816.cafe24.com/dbeditor/UserValidate.php?";
    private Map<String , String> parameters;


    public ValidateRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL+"userID="+userID, listener, null);


    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
