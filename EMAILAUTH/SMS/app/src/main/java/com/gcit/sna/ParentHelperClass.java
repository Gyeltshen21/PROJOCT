package com.gcit.sna;

public class ParentHelperClass {
    private String Name, StdCode, Email, Phone, Password;

    public ParentHelperClass(){}


    public ParentHelperClass(String name, String stdCode, String email, String phone, String password) {
        Name = name;
        StdCode = stdCode;
        Email = email;
        Phone = phone;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStdCode() {
        return StdCode;
    }

    public void setStdCode(String stdCode) {
        StdCode = stdCode;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
