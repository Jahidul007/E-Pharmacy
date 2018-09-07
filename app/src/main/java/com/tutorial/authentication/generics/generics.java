package com.tutorial.authentication.generics;

public class Generics {

    String id;
    String genericName;

    public Generics() {
    }

    public Generics(String id, String genericName) {
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
