package com.example.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MarketIntent extends Intent {
    public MarketIntent(Context packageContext, Class<?> cls){
        super(packageContext, cls);
    }
    public MarketIntent(){
        super();
    }
    public MarketIntent(Intent intent){
        super(intent);
    }

    @NonNull
    public Intent putExtra(String name, ProductInfo value) {
        return super.putExtra("code", value.getCode())
                .putExtra("name", value.getName())
                .putExtra("price", value.getPrice())
                .putExtra("image",value.getImage());
    }

    public ProductInfo getProductInfo(){
        ProductInfo productInfo;
        String code = getExtras().getString("code");
        String name = getExtras().getString("name");
        String price = getExtras().getString("price");
        String image = getExtras().getString("image");
        productInfo = new ProductInfo(code, name, price, image);
        return productInfo;
    }
}
