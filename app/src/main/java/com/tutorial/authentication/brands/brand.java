package com.tutorial.authentication.brands;

public class Brand {

    String brandName;
    int price;

    public Brand() {
    }

    public Brand(String brandName, int price) {
        this.brandName = brandName;
        this.price = price;
    }

    public String getBrandName() {
        return brandName;
    }

    public int getPrice() {
        return price;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
