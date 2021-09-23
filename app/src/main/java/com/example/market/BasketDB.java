package com.example.market;

public class BasketDB extends ProductInfo{
    //    private ProductInfo Pinfo;
    //private String name;
    //private String price;
    private String num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public BasketDB(ProductInfo info, String num) {
        super(info.getCode(),info.getName(),info.getPrice(),info.getImage());
        this.num = num;
    }
}