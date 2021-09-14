package com.example.market;

public class BasketDB {
    private String name;
    private String price;
    private String num;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String image) {
        this.num = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BasketDB(String name, String price, String num) {
        this.name = name;
        this.price = price;
        this.num = num;
    }
}
