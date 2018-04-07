package com.shsxt.xmjf.api.utils;

import com.shsxt.xmjf.api.exceptions.ParamsException;

/**
 * Created by lp on 2018/3/1.
 */
public class AssertUtil {

    public static   void isTrue(Boolean flag,String msg){
        if(flag){
            throw new ParamsException(msg);
        }
    }
}
