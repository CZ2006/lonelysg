package com.IrisBICS.lonelysg.Models;

public class User {
    private String username;
    private String gender;
    private String age;
    private String occupation;
    private String interests;
    private String userID;
    private String password;
    private String email;

//    public User(String username, String gender, String age, String occupation, String interests) {
//        this.username = username;
//        this.gender = gender;
//        this.age = age;
//        this.occupation = occupation;
//        this.interests = interests;
//    }

    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) { this.gender = gender; }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
