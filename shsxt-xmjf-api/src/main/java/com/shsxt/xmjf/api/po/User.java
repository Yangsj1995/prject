package com.shsxt.xmjf.api.po;

import java.io.Serializable;

/**
 * Created by lp on 2018/3/1.
 */
public class User implements Serializable{
    private static final long serialVersionUID = 7532092919164349906L;
    private Integer id;
    private String userName;
    private String userPwd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                '}';
    }
}
