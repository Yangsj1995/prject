package com.shsxt.xmjf.api.service;

import com.github.pagehelper.Page;
import com.shsxt.xmjf.api.query.BusItemInvestQuery;
import com.shsxt.xmjf.api.utils.PageList;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by lp on 2018/3/5.
 */
public interface IBusItemInvestService {
    public PageList queryBusItemInvestsByItemId(BusItemInvestQuery busItemInvestQuery);

    public  void addBusItemInvest(Integer itemId, BigDecimal amount,String busiPassword,Integer userId);


    public Map<String,Object> countLastFiveMonthsInvestInfoByUserId(Integer userId);
}
