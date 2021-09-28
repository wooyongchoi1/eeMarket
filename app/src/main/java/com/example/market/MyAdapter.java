package com.example.market;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//리스트뷰의 어댑터 클래스입니다. 이 어댑터가 리스트뷰의 아이템 구성상태를 결정합니다.

public class MyAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ProductInfo> sample;

    public MyAdapter(Context context, ArrayList<ProductInfo> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    void setSample(ArrayList<ProductInfo> s){sample = s;this.notifyDataSetChanged();}
    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ProductInfo getItem(int position) { return sample.get(position); }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview_custom, null);


        TextView productName = (TextView)view.findViewById(R.id.productname);
        TextView productPrice = (TextView)view.findViewById(R.id.price);
        ImageView productImage = view.findViewById(R.id.thumbnail);
        loadImageTask imageTask = new loadImageTask(sample.get(position).getImage());
        Bitmap bitmap = null;

        try {
            bitmap = imageTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        productName.setText(sample.get(position).getName());
        productPrice.setText(sample.get(position).getPrice()+"원");
        productImage.setImageBitmap(bitmap);

        return view;
    }
}
