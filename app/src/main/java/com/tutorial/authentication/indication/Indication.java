package com.tutorial.authentication.indication;

public class Indication {

    private String indicationId;
    private String indicationName;

    public Indication(){

    }

    public Indication(String indicationId, String indicationName) {
        this.indicationId = indicationId;
        this.indicationName = indicationName;
    }

    public String getIndicationId() {
        return indicationId;
    }

    public String getIndicationName() {
        return indicationName;
    }
}
