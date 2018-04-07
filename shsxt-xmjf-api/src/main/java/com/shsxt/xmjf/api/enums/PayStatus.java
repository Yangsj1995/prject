package com.shsxt.xmjf.api.enums;

/**
 * Created by lp on 2018/3/6.
 */
public enum PayStatus {
    PAY_SUCCESS(1),// 支付成功
    PAY_FAILED(0),//  支付失败
    PAY_CHECK(2);// 审核中
    private Integer state;

    PayStatus(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
