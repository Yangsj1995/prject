package com.shsxt.xmjf.api.service;

import com.shsxt.xmjf.api.dto.PayInfo;
import com.shsxt.xmjf.api.query.BusAccountRechargeQuery;
import com.shsxt.xmjf.api.utils.PageList;

import java.math.BigDecimal;

/**
 * Created by lp on 2018/3/6.
 */
public interface IBusAccountRechargeService {

    /**
     * 添加充值记录并且构建充值请求参数
     * @param userId
     * @param amount
     * @param busiPassword
     */
    public PayInfo addBusAccountRechargeAndBuildPayParams(Integer userId, BigDecimal amount, String busiPassword);


    public  void updateBusAccountRechargeByOrderNo(String orderNo,BigDecimal totalFee,String sign,String tradeStatus,Integer userId);



    public PageList queryBusAccountRechargesByUserId(BusAccountRechargeQuery busAccountRechargeQuery);

}
