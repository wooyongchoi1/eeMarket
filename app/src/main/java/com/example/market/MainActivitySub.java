package com.example.market;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class MainActivitySub extends LinearLayout {
    public MainActivitySub(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MainActivitySub(Context context) {
        super(context);

        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listview_img,this,true);
    }
}
