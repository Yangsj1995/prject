package com.shsxt.xmjf.api.query;

import java.io.Serializable;

/**
 * Created by lp on 2018/3/1.
 */
public class BaseQuery  implements Serializable {
    private static final long serialVersionUID = 9168363729900761385L;
    private Integer pageNum=1;
    private Integer pageSize=10;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
