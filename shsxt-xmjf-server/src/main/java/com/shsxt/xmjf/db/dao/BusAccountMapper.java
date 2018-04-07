package com.shsxt.xmjf.db.dao;

import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

public interface BusAccountMapper extends BaseMapper<BusAccount>{

    public  BusAccount queryBusAccountInfoByUserId(@Param("userId")Integer userId);

    public Map<String,BigDecimal> queryAccountInfoByUserId(@Param("userId")Integer userId);

}