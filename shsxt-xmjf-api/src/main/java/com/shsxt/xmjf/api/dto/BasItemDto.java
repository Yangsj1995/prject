package com.shsxt.xmjf.api.dto;

import com.shsxt.xmjf.api.po.BasItem;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lp on 2018/3/3.
 */
public class BasItemDto extends BasItem implements Serializable{
    private static final long serialVersionUID = -7231731969359769844L;
    private Integer total;
    private Long syTime;

    private BigDecimal syAmount;

    public BigDecimal getSyAmount() {
        return syAmount;
    }

    public void setSyAmount(BigDecimal syAmount) {
        this.syAmount = syAmount;
    }

    public Long getSyTime() {
        return syTime;
    }

    public void setSyTime(Long syTime) {
        this.syTime = syTime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
