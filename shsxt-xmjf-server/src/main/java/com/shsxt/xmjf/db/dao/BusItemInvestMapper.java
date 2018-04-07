package com.shsxt.xmjf.db.dao;

import com.shsxt.xmjf.api.po.BusItemInvest;
import com.shsxt.xmjf.api.query.BusItemInvestQuery;
import com.shsxt.xmjf.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BusItemInvestMapper  extends BaseMapper<BusItemInvest>{

    public List<Map<String,Object>> queryBusItemInvestsByItemId(BusItemInvestQuery busItemInvestQuery);


    public  Integer countItemInvestIsNew(@Param("userId") Integer userId);

    public  List<Map<String,Object>> countLastFiveMonthsInvestInfoByUserId(@Param("userId")Integer userId);


}