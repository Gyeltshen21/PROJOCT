package com.gcit.app;

public class AdminProfilePhoto {
    private String adminImageUrl;

    public AdminProfilePhoto(){}

    public AdminProfilePhoto(String adminImageUrl){
        this.adminImageUrl = adminImageUrl;
    }

    public String getAdminImageUrl() {
        return adminImageUrl;
    }

    public void setAdminImageUrl(String adminImageUrl) {
        this.adminImageUrl = adminImageUrl;
    }
}
