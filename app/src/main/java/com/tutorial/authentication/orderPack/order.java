package com.tutorial.authentication.orderPack;

public class order {

    String id;
    String name;
    String qty;
    String price;

    public order(){

    }

    public order(String id, String name, String qty,String price) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getQty() {
        return qty;
    }
    public String getPrice() {
        return price;
    }
}
