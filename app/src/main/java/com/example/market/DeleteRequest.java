package com.example.market;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest {
    final static private String URL = "https://hmm8816.cafe24.com/dbeditor/BasketDelete.php?";
    private Map<String , String> parameters;


    public DeleteRequest(String userID, String ID, Response.Listener<String> listener) {
        super(Request.Method.POST, URL+"userID="+userID+"&ID="+ID, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("ID", ID);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
