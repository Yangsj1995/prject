package com.shsxt.xmjf.api.service;

/**
 * Created by lp on 2018/3/1.
 * 短信服务接口
 */
public interface ISmsService {
    /**
     * 发送短信
     * @param phone
     * @param type
     * @param code
     */
    public  void sendPhoneSms(String phone,String type,String code);
}
