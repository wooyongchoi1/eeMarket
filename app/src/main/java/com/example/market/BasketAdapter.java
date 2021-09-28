package com.example.market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//장바구니 화면에서 쓰이는 어댑터이다.
//리스트뷰와 데이터를 연결

public class BasketAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<BasketDB> sample;
    LayoutInflater mLayoutInflater = null;


    public BasketAdapter(Context context, ArrayList<BasketDB> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) { return sample.get(position); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview_basketcustom, null);


        TextView productNum = (TextView)view.findViewById(R.id.BNum);
        TextView productName = (TextView)view.findViewById(R.id.BName);
        TextView productPrice = (TextView)view.findViewById(R.id.BPrice);
        ImageView imageView = view.findViewById(R.id.basketthumbnail);

//        TextView productCode = (TextView)view.findViewById(R.id.BName);
//        TextView productImage = (TextView)view.findViewById(R.id.BPrice);


        productNum.setText(sample.get(position).getNum()+"개");
        productName.setText(sample.get(position).getName());
        productPrice.setText(sample.get(position).getPrice()+"원");

        loadImageTask imageTask = new loadImageTask(sample.get(position).getImage());

        try {
            imageView.setImageBitmap(imageTask.execute().get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        productCode.setText(sample.get(position).getCode());
//        productCode.setText(sample.get(position).getImage());


        return view;
    }
}