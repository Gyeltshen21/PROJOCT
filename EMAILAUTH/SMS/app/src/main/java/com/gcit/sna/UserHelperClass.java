package com.gcit.sna;

public class UserHelperClass {
    private String Name, SchoolCode, Email, Phone, Password;
    public UserHelperClass(){

    }

    public UserHelperClass(String name, String schoolCode, String email, String phone, String password) {
        Name = name;
        SchoolCode = schoolCode;
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

    public String getSchoolCode() {
        return SchoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        SchoolCode = schoolCode;
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
