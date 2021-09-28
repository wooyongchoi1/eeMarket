package com.example.market;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

public class ProductActivity extends AppCompatActivity {

    ImageView thumbnailView;
    TextView nameView;
    TextView priceView;
    Button detailButton;
    Button payoptionButton;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        thumbnailView = findViewById(R.id.activityproduct_thumbnail);
        nameView = findViewById(R.id.activityproduct_name);
        priceView = findViewById(R.id.activityproduct_price);
        detailButton = findViewById(R.id.detailimageButton);
        payoptionButton = findViewById(R.id.payoptionButton);

        ProductInfo pi = new MarketIntent(getIntent()).getProductInfo();
        userID = getIntent().getExtras().getString("userID");

        loadImageTask imageTask = new loadImageTask(pi.getImage());
        Bitmap bitmap = null;

        try {
            bitmap = imageTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thumbnailView.setImageBitmap(bitmap);
        nameView.setText(pi.getName());
        priceView.setText(pi.getPrice()+"원");

        detailButton.setOnClickListener(v->{
            MarketIntent intent = new MarketIntent(this, MainActivity.class);
            intent.putExtra("userID", userID);
            intent.putExtra("product", pi);
            startActivity(intent);
        });

        payoptionButton.setOnClickListener(v->{
            MarketIntent intent = new MarketIntent(this, PayActivity.class);
            intent.putExtra("userID", userID);
            intent.putExtra("product", pi);
            startActivity(intent);
        });
    }
}
