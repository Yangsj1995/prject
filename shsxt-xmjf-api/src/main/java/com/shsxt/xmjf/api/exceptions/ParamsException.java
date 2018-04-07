package com.shsxt.xmjf.api.exceptions;

import com.shsxt.xmjf.api.constants.P2pConstant;

/**
 * Created by lp on 2018/3/1.
 */
public class ParamsException extends  RuntimeException {
    private String msg= P2pConstant.OPT_FAILED_MSG;
    private Integer code=P2pConstant.OPT_FAILED_CODE;

    public ParamsException() {
        super(P2pConstant.OPT_FAILED_MSG);
    }

    public ParamsException(String message) {
        super(message);
        this.msg=message;
    }

    public ParamsException(String msg,Integer code) {
        super(msg);
        this.msg=msg;
        this.code=code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
