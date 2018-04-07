package com.shsxt.xmjf.api.query;

import java.io.Serializable;

/**
 * Created by lp on 2018/3/5.
 */
public class BusItemInvestQuery extends  BaseQuery implements Serializable {
    private static final long serialVersionUID = 5617708592871179622L;
    private Integer itemId;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
