package com.shsxt.xmjf.api.query;

import java.io.Serializable;

/**
 * Created by lp on 2018/3/6.
 */
public class BusAccountRechargeQuery extends  BaseQuery  implements Serializable{

    private static final long serialVersionUID = -6201412761078682644L;

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
