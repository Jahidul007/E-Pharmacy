package com.tutorial.authentication.drug;

public class Drug {

    String emailId ;
    String brandName;
    String genericName;
    String companyName;
    String strength;
    String form;

    public Drug(){

    }

    public Drug(String emailId, String brandName, String genericName, String companyName, String strength, String form) {
        this.emailId = emailId;
        this.brandName = brandName;
        this.genericName = genericName;
        this.companyName = companyName;
        this.strength = strength;
        this.form = form;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getGenericName() {
        return genericName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getStrength() {
        return strength;
    }

    public String getForm() {
        return form;
    }
}
