package com.wishuok.dto;

public class EnDecodeModel {
    private String username;

    private String userkey;

    private String command;

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    public void setUserkey(String userkey){
        this.userkey = userkey;
    }
    public String getUserkey(){
        return this.userkey;
    }
    public void setCommand(String command){
        this.command = command;
    }
    public String getCommand(){
        return this.command;
    }
}
