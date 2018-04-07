package com.shsxt.xmjf.api.query;

import java.io.Serializable;

/**
 * Created by lp on 2018/3/3.
 */
public class BasItemQuery extends  BaseQuery implements Serializable{
    private static final long serialVersionUID = -4833175465958890143L;
    private Integer itemCycle;// 1(0-30 天)，2-（30-90天）,3(90天以上)
    private Integer itemType;// 项目类别
    private Integer isHistory;// 是否为历史项目  1-历史项目  0-可投标项目

    public Integer getItemCycle() {
        return itemCycle;
    }

    public void setItemCycle(Integer itemCycle) {
        this.itemCycle = itemCycle;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Integer isHistory) {
        this.isHistory = isHistory;
    }
}
