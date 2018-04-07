package com.shsxt.xmjf.service.impl;

import com.github.pagehelper.PageHelper;
import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.dto.PayInfo;
import com.shsxt.xmjf.api.enums.PayStatus;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.po.BusAccount;
import com.shsxt.xmjf.api.po.BusAccountLog;
import com.shsxt.xmjf.api.po.BusAccountRecharge;
import com.shsxt.xmjf.api.query.BusAccountRechargeQuery;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.api.utils.PageList;
import com.shsxt.xmjf.db.dao.BusAccountLogMapper;
import com.shsxt.xmjf.db.dao.BusAccountMapper;
import com.shsxt.xmjf.db.dao.BusAccountRechargeMapper;
import com.shsxt.xmjf.utils.MD5;
import com.shsxt.xmjf.utils.Md5Util;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lp on 2018/3/6.
 */
@Service
public class BusAccountRechargeServiceImpl implements IBusAccountRechargeService {
    @Resource
    private BusAccountRechargeMapper busAccountRechargeMapper;
    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private BusAccountMapper busAccountMapper;

    @Resource
    private BusAccountLogMapper busAccountLogMapper;
    @Override
    public PayInfo addBusAccountRechargeAndBuildPayParams(Integer userId, BigDecimal amount, String busiPassword) {
        /**
         * 1.参数基本校验
         *   userId  必须存在 认证状态必须认证
         *   amount   金额非空 >0
         * busiPassword   非空  等于认证密码
         * 2.添加充值记录
         * 3.构建充值请求参数
         */
        checkParams(userId,amount,busiPassword);
        BusAccountRecharge busAccountRecharge=new BusAccountRecharge();
        busAccountRecharge.setAddtime(new Date());
        busAccountRecharge.setFeeAmount(BigDecimal.ZERO);
        busAccountRecharge.setFeeRate(BigDecimal.ZERO);
        String orderNo="SXT_P2P_"+System.currentTimeMillis();
        busAccountRecharge.setOrderNo(orderNo);
        busAccountRecharge.setRechargeAmount(amount);
        busAccountRecharge.setRemark("PC端用户充值操作,充值金额:"+amount);
        busAccountRecharge.setResource("PC端用户充值操作");
        busAccountRecharge.setStatus(PayStatus.PAY_CHECK.getState());
        busAccountRecharge.setType(P2pConstant.RECHARGE_PC);
        busAccountRecharge.setUserId(userId);
        AssertUtil.isTrue(busAccountRechargeMapper.insert(busAccountRecharge)<1,P2pConstant.OPT_FAILED_MSG);
        return buildPayInfo(amount,orderNo,"PC端用户充值操作","PC端用户充值操作,充值金额:"+amount);
    }



    private PayInfo buildPayInfo(BigDecimal amount, String orderNo, String subject, String body) {
        PayInfo payInfo=new PayInfo();
        payInfo.setBody(body);
        payInfo.setOutOrderNo(orderNo);
        payInfo.setSubject(subject);
        payInfo.setTotalFee(amount);
        String sign= buildSignStr(payInfo);
        payInfo.setSign(new Md5Util().encode(sign+payInfo.getKey(),null));
        return payInfo;
    }

    private String buildSignStr(PayInfo payInfo) {
        StringBuffer arg = new StringBuffer();
        if(!StringUtils.isBlank(payInfo.getBody())){
            arg.append("body="+payInfo.getBody()+"&");
        }
        arg.append("notify_url="+payInfo.getNotifyUrl()+"&");
        arg.append("out_order_no="+payInfo.getOutOrderNo()+"&");
        arg.append("partner="+payInfo.getPid()+"&");
        arg.append("return_url="+payInfo.getReturnUrl()+"&");
        arg.append("subject="+payInfo.getSubject()+"&");
        arg.append("total_fee="+payInfo.getTotalFee()+"&");
        arg.append("user_seller="+payInfo.getUserSeller());
        // 如果存在转义字符，那么去掉转义
        return StringEscapeUtils.unescapeJava(arg.toString());
    }

    private void checkParams(Integer userId, BigDecimal amount, String busiPassword) {
        AssertUtil.isTrue(null==userId,"用户未登录!");
        BasUserSecurity basUserSecurity=basUserSecurityService.queryBasUserSecurityByUserId(userId);
        AssertUtil.isTrue(null==basUserSecurity,"该用户不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(busiPassword),"交易密码不能为空!");
        AssertUtil.isTrue(!(basUserSecurity.getPaymentPassword().equals(MD5.toMD5(busiPassword))),"交易密码不正确!");
        AssertUtil.isTrue(null==amount||amount.compareTo(BigDecimal.ZERO)<=0,"交易金额非法!");
    }



    @Override
    public void updateBusAccountRechargeByOrderNo(String orderNo, BigDecimal totalFee, String sign, String tradeStatus,Integer userId) {
        AssertUtil.isTrue(
                StringUtils.isBlank(orderNo)
                        || null==totalFee||StringUtils.isBlank(sign)
                        || StringUtils.isBlank(tradeStatus),"参数不能空,请联系客服!");
        Md5Util md5Util = new Md5Util();
        String signMd5 = md5Util.encode(orderNo+totalFee+tradeStatus+P2pConstant.PAY_PID+P2pConstant.PAY_KEY, null);
        AssertUtil.isTrue(!(sign.equals(signMd5)),"支付异常，请联系客服!");
        BusAccountRecharge busAccountRecharge=busAccountRechargeMapper.queryBusAccountRechargeByOrderNo(orderNo);
        AssertUtil.isTrue(null==busAccountRecharge,"订单异常,该订单记录不存在!");
        AssertUtil.isTrue((busAccountRecharge.getRechargeAmount().compareTo(totalFee))!=0,"订单异常,请联系管理员!");
        AssertUtil.isTrue(busAccountRecharge.getStatus().equals(1),"该订单已支付!");
        AssertUtil.isTrue(!tradeStatus.equals(P2pConstant.PAY_SUCCESS),"订单异常，请联系客服!");
        busAccountRecharge.setStatus(PayStatus.PAY_SUCCESS.getState());
        busAccountRecharge.setActualAmount(totalFee);
        busAccountRecharge.setAuditTime(new Date());
        AssertUtil.isTrue(busAccountRechargeMapper.update(busAccountRecharge)<1,P2pConstant.OPT_FAILED_MSG);
        //  更新账户金额
        BusAccount busAccount=busAccountMapper.queryBusAccountInfoByUserId(userId);
        busAccount.setTotal(busAccount.getTotal().add(totalFee));
        busAccount.setCash(busAccount.getCash().add(totalFee));
        busAccount.setUsable(busAccount.getUsable().add(totalFee));
        AssertUtil.isTrue(busAccountMapper.update(busAccount)<1,P2pConstant.OPT_FAILED_MSG);
        // 添加日志记录
        BusAccountLog busAccountLog=new BusAccountLog();
        busAccountLog.setAddtime(new Date());
        busAccountLog.setBudgetType(1);// 收入
        busAccountLog.setCash(busAccount.getCash());
        busAccountLog.setFrozen(busAccount.getFrozen());
        busAccountLog.setOperMoney(totalFee);
        busAccountLog.setOperType("user_recharge_success");
        busAccountLog.setRemark("用户充值");
        busAccountLog.setRepay(busAccount.getRepay());
        busAccountLog.setTotal(busAccount.getTotal());
        busAccountLog.setUsable(busAccount.getUsable());
        busAccountLog.setWait(busAccount.getWait());
        busAccountLog.setUserId(userId);
        AssertUtil.isTrue(busAccountLogMapper.insert(busAccountLog)<1,P2pConstant.OPT_FAILED_MSG);
    }

    @Override
    public PageList queryBusAccountRechargesByUserId(BusAccountRechargeQuery busAccountRechargeQuery) {
        PageHelper.startPage(busAccountRechargeQuery.getPageNum(),busAccountRechargeQuery.getPageSize());
        List<Map<String,Object>> list= busAccountRechargeMapper.queryBusAccountRechargesByUserId(busAccountRechargeQuery);
        return new PageList(list);
    }
}
