package com.shsxt.xmjf.api.model;

import com.shsxt.xmjf.api.constants.P2pConstant;

import java.io.Serializable;

/**
 * Created by lp on 2018/3/1.
 */
public class ResultInfo  implements Serializable{
    private static final long serialVersionUID = -1418031281886709162L;
    private Integer code= P2pConstant.OPT_SUCCESS_CODE;
    private String msg=P2pConstant.OPT_SUCCESS_MSG;
    private Object result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
