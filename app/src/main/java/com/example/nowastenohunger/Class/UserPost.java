package com.example.nowastenohunger.Class;

/*
    UserPost Class is used to collect TextViews data from MakeDonationsFragment Section.
*/
public class UserPost
{

    String item,amount,userID;

    public UserPost() {
    }

    public UserPost(String item, String amount, String userID)
    {
        this.item = item;
        this.amount = amount;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
