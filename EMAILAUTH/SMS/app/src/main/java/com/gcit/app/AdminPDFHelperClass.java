package com.gcit.app;

public class AdminPDFHelperClass {
    private String PDFName, PDFUrl;

    public AdminPDFHelperClass() {
    }

    public String getPDFName() {
        return PDFName;
    }

    public void setPDFName(String PDFName) {
        this.PDFName = PDFName;
    }

    public String getPDFUrl() {
        return PDFUrl;
    }

    public void setPDFUrl(String PDFUrl) {
        this.PDFUrl = PDFUrl;
    }

    public AdminPDFHelperClass(String name, String url){
        PDFName = name;
        PDFUrl = url;
    }
}
