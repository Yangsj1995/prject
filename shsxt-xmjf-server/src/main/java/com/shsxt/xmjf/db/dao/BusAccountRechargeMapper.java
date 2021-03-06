package com.shsxt.xmjf.db.dao;

import com.shsxt.xmjf.api.po.BusAccountRecharge;
import com.shsxt.xmjf.api.query.BusAccountRechargeQuery;
import com.shsxt.xmjf.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BusAccountRechargeMapper  extends BaseMapper<BusAccountRecharge>{

    public  BusAccountRecharge queryBusAccountRechargeByOrderNo(@Param("orderNo")String orderNo);

    public List<Map<String,Object>> queryBusAccountRechargesByUserId(BusAccountRechargeQuery busAccountRechargeQuery);



}