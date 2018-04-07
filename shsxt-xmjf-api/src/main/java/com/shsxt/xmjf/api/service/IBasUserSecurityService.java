package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.po.BasUserSecurity;

/**
 * Created by lp on 2018/3/5.
 */
public interface IBasUserSecurityService {
    public BasUserSecurity queryBasUserSecurityByUserId(Integer userId);

    public  void updateBasUserSecurityByUserId(Integer userId,String realName,String idCard,String busiPassword,String confirmBusiPassword);




}
