package com.example.nocomment.snapchat;

/**
 * Created by guomingsun on 31/08/2016.
 */
public class ChatMsg {

    private long msgID;
    private boolean isMe;
    private String msg;
    private String userID;
    private String time;

    public ChatMsg(boolean isMe, String msg, String time) {

        this.isMe = isMe;
        this.msg = msg;
        this.time = time;

    }


    public long getMsgID() {
        return msgID;
    }

    public void setMsgID(long msgID) {
        this.msgID = msgID;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
