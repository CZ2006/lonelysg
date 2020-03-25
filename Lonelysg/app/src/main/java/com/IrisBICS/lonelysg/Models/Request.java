package com.IrisBICS.lonelysg.Models;

public class Request {

    private String participant, invitation, host, requestID;

    public Request(){}

    public Request(String participant, String invitation, String host, String reqID) {
        this.participant = participant;
        this.invitation = invitation;
        this.host = host;
        this.requestID = reqID;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getInvitation() {
        return invitation;
    }

    public void setInvitation(String invitation) {
        this.invitation = invitation;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
}
