package com.gcit.app;

public class AdminPDFHelperClass {
    private String Name, Url;

    public AdminPDFHelperClass() {
    }

    public String getName() {

        return Name;
    }

    public void setName(String name) {

        Name = name;
    }

    public String getUrl() {

        return Url;
    }

    public void setUrl(String url) {

        Url = url;
    }

    public AdminPDFHelperClass(String Name, String Url){
        this.Name = Name;
        this.Url = Url;
    }
}
