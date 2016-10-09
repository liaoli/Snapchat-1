package com.example.nocomment.snapchat;

/**
 * Created by guomingsun on 31/08/2016.
 */
public class ChatMsg {

    private long msgID;
    private int msgType;
    private String msg;
    private String userID;
    private String time;

    public static final int RIGHT_MSG = 0;
    public static final int LEFT_MSG = 1;
    public static final int RIGHT_IMG = 2;
    public static final int LEFT_IMG = 3;

    public ChatMsg(int msgType, String msg, String time) {

        this.msgType = msgType;
        this.msg = msg;
        this.time = time;

    }


    public long getMsgID() {
        return msgID;
    }

    public void setMsgID(long msgID) {
        this.msgID = msgID;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMe(int msgType) {
        this.msgType = msgType;
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
