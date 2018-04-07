package com.shsxt.xmjf.db.dao;

import com.shsxt.xmjf.api.po.BusIncomeStat;
import com.shsxt.xmjf.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface BusIncomeStatMapper  extends BaseMapper<BusIncomeStat>{
    public  BusIncomeStat queryBusIncomeStatByUserId(@Param("userId") Integer userId);

}