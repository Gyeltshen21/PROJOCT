package com.gcit.sna;

public class AdminNotificationHelperClass {
   private String SenderName, Date, Time, Message;
    public AdminNotificationHelperClass(){

    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public AdminNotificationHelperClass(String senderName, String date, String time, String message){
        SenderName = senderName;
        Date = date;
        Time = time;
        Message = message;
    }
}
