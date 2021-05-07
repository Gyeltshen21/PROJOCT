package com.gcit.app;

public class TeacherPDFHelperClass {
    private String Name, Url;

    public TeacherPDFHelperClass() {
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

    public TeacherPDFHelperClass(String Name, String Url){
        this.Name = Name;
        this.Url = Url;
    }
}
