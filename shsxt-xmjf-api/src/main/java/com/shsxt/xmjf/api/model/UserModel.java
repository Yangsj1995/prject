package com.shsxt.xmjf.api.model;

import java.io.Serializable;

/**
 * Created by lp on 2018/3/2.
 */
public class UserModel implements Serializable {
    private static final long serialVersionUID = -2504052881976706097L;
    private Integer id;
    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
