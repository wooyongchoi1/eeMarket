package com.example.market;

public class ProductInfo {
    private String code;
    private String name;
    private String price;
    private String image;
    private String site;

    public String getCode() {
        return code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductInfo(String code, String name, String price, String image) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.image = image;
    }
}
