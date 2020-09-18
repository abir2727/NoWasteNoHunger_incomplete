package com.example.nowastenohunger.Class;

/*
    Post Class is used to show User's Post in SearchDonationsFragment Section & alse used to
    send User's Post data in PostAdapter Class.
*/


public class Post {

    String post,fullname,time,UID,postlocation;

    public Post() {
    }

    public Post(String post, String fullname, String time, String UID, String postlocation) {
        this.post = post;
        this.fullname = fullname;
        this.time = time;
        this.UID = UID;
        this.postlocation=postlocation;
    }

    public String getPostlocation() {
        return postlocation;
    }

    public void setPostlocation(String postlocation) {
        this.postlocation = postlocation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getfullname() {
        return fullname;
    }

    public void setfullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
