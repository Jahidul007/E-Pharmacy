package com.tutorial.authentication.generics;

public class generics {

    String id;
    String genericName;

    public generics() {
    }

    public generics(String id, String genericName) {
        this.id = id;
        this.genericName = genericName;
    }

    public String getId() {
        return id;
    }

    public String getGenericName() {
        return genericName;
    }
}
