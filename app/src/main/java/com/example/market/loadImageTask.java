package com.example.market;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

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
