package com.IrisBICS.lonelysg.Models;

public class Invitation {

    private String category, date, desc, host, startTime, title, invitationID;

    public Invitation() {
    }

    public Invitation(String category, String date, String desc, String host, String startTime, String title, String invitationID) {
        this.category = category;
        this.date = date;
        this.desc = desc;
        this.host = host;
        this.startTime = startTime;
        this.title = title;
        this.invitationID = invitationID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInvitationID() {
        return invitationID;
    }

    public void setInvitationID(String invitationID) {
        this.invitationID = invitationID;
    }
}
