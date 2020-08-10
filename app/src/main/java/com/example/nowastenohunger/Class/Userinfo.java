package com.example.nowastenohunger.Class;

public class Userinfo {

    public String fullname, number, address, accountType, cuisineType , Title , Post;

    public Userinfo() {

    }

    public Userinfo(String fullname, String number, String address, String accountType, String cuisineType) {
        this.fullname = fullname;
        this.number = number;
        this.address = address;
        this.accountType = accountType;
        this.cuisineType = cuisineType;
    }




    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number1) {
        this.number = number1;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String number2) {
        this.accountType = accountType;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String number2) {
        this.cuisineType = cuisineType;
    }
}
