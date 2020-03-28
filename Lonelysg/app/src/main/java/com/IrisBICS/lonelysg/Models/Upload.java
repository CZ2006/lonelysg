package com.IrisBICS.lonelysg.Models;

public class Upload {
    private String name;
    private String imageUrl;

    public Upload() {
        //empty constructor needed
    }

    ;

    public Upload(String userName, String userImageUrl) {
        if (userName.trim().equals("")) {
            userName = "No name";
        }

        name = userName;
        imageUrl = userImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}