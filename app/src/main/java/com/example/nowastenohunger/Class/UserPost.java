package com.example.nowastenohunger.Class;

/*
    UserPost Class is used to collect TextViews data from MakeDonationsFragment Section.
*/
public class UserPost
{
    String item,amount;

    public UserPost() {
    }

    public UserPost(String item, String amount)
    {
        this.item = item;
        this.amount = amount;
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
