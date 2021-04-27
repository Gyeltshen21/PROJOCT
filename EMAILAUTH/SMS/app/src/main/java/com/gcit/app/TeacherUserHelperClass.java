package com.gcit.app;

public class TeacherUserHelperClass {
    private String EmployeeID, Email, Phone, Password;

    public TeacherUserHelperClass(){

    }
    public TeacherUserHelperClass(String employeeid, String email, String phone, String password) {
        EmployeeID = employeeid;
        Email = email;
        Phone = phone;
        Password = password;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
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
