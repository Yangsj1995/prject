package com.shsxt.xmjf.db.dao;

import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface BasUserSecurityMapper extends BaseMapper<BasUserSecurity> {

    public  BasUserSecurity queryBasUserSecurityByUserId(@Param("userId") Integer userId);

    public  BasUserSecurity queryBasUserSecurityByIdCard(@Param("idCard")String idCard);

}