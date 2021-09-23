package com.example.market;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ImgAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Pair<Bitmap, String>> sample;

    public ImgAdapter(Context context, ArrayList<Pair<Bitmap, String>> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    void setSample(ArrayList<Pair<Bitmap, String>> s){sample = s;this.notifyDataSetChanged();}
    @Override
    public int getCount() { return sample.size(); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Pair<Bitmap, String> getItem(int position) { return sample.get(position); }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview_img, null);


        ImageView mMainImage = (ImageView)view.findViewById(R.id.main_image);
        TextView mTextView = view.findViewById(R.id.img_detail);

        mMainImage.setImageBitmap(sample.get(position).first);
        mTextView.setText(sample.get(position).second);

        return view;
    }
}