package com.example.market;

public class ProductInfo {
    private String code;
    private String name;

    public String getCode() {
        return code;
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

    public ProductInfo(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
