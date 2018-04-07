package com.shsxt.xmjf.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lp on 2018/3/8.
 */
public class AccountDto  implements Serializable{
    private static final long serialVersionUID = -8825016330277873731L;
    private String name;
    private BigDecimal y;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }
}
