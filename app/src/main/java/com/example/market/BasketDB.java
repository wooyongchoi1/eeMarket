package com.example.market;

public class BasketDB extends ProductInfo{
    //    private ProductInfo Pinfo;
    //private String name;
    //private String price;
    private String num;
    private String ID;

    public String getNum() {
        return num;
    }
    public String getID() {
        return ID;
    }

    public void setNum(String num) {
        this.num = num;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public BasketDB(String ID, ProductInfo info, String num) {
        super(info.getCode(),info.getName(),info.getPrice(),info.getImage());
        this.num = num;
        this.ID = ID;
    }
}