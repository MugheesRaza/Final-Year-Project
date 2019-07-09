package com.example.mughees.chat_app.Model;

public class account {
    String Gmail;
    String Image;
    String PhoneNumber;
    String Name;
    String status;
    String REG;

    public account(String gmail, String image, String phoneNumber, String name, String status, String REG) {
        Gmail = gmail;
        Image = image;
        PhoneNumber = phoneNumber;
        Name = name;
        this.status = status;
        this.REG = REG;
    }

    public account() {
    }

    public String getGmail() {
        return Gmail;
    }

    public void setGmail(String gmail) {
        Gmail = gmail;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getREG() {
        return REG;
    }

    public void setREG(String REG) {
        this.REG = REG;
    }
}
