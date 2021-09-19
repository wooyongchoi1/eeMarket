package com.example.market;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

//이미지 url을 비트맵으로 변환하는 클래스, string(url)을 받고 bitmap을 반환합니다.
//MyAdapter 와 MainAtivity에서 사용됨
public class loadImageTask extends AsyncTask<Bitmap, Void, Bitmap> {

    private String url;

    public loadImageTask(String url) {

        this.url = url;
    }

    @Override
    protected Bitmap doInBackground(Bitmap... params) {

        Bitmap imgBitmap = null;

        try {
            URL url1 = new URL(url);
            URLConnection conn = url1.openConnection();
            conn.connect();
            int nSize = conn.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bit) {
        super.onPostExecute(bit);
    }
}
