package com.IrisBICS.lonelysg.Models;

public class User {

    private String email;
    private String password;
    private String username;

    public User(String email, String password){
        this.email = email;
        this.password = password;
}

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
