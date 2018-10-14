package com.tutorial.authentication.company;

public class company {

    String id;
    String CompanyName;

    public company() {

    }


    public company(String id, String companyName) {
        this.id = id;
        CompanyName = companyName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getId() {
        return id;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}
