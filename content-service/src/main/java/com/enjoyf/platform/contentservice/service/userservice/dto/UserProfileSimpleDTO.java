package com.enjoyf.platform.contentservice.service.userservice.dto;

/**
 * Created by pengxu on 2017/6/26.
 */
public class UserProfileSimpleDTO {
    private long uid;
    private String nick;
    private String icon;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
