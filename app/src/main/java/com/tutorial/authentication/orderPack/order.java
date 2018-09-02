package com.tutorial.authentication.orderPack;

public class order {

    String id;
    String name;
    String qty;
    String photo;

    public order(){

    }

    public order(String id, String name, String qty, String photo) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.photo = photo;
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

    public String getPhoto() {
        return photo;
    }
}
