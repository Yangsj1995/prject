package com.shsxt.xmjf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.xmjf.api.constants.ItemStatus;
import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.dto.BasItemDto;
import com.shsxt.xmjf.api.po.*;
import com.shsxt.xmjf.api.query.BusItemInvestQuery;
import com.shsxt.xmjf.api.service.*;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.shsxt.xmjf.api.utils.PageList;
import com.shsxt.xmjf.db.dao.*;
import com.shsxt.xmjf.utils.Calculator;
import com.shsxt.xmjf.utils.MD5;
import com.shsxt.xmjf.utils.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.omg.CosNaming.IstringHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lp on 2018/3/5.
 */
@Service
public class BusItemInvestServiceImpl implements IBusItemInvestService {
    @Resource
    private BusItemInvestMapper busItemInvestMapper;

    @Resource
    private IBasUserService userService;

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private IBasItemService basItemService;

    @Resource
    private IBusAccountService busAccountService;

    @Resource
    private BusUserStatMapper busUserStatMapper;
    @Resource
    private BusAccountLogMapper busAccountLogMapper;

    @Resource
    private BusIncomeStatMapper busIncomeStatMapper;
    @Resource
    public  BusUserIntegralMapper busUserIntegralMapper;

    @Resource
    private BusIntegralLogMapper busIntegralLogMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public PageList queryBusItemInvestsByItemId(BusItemInvestQuery busItemInvestQuery) {
        PageHelper.startPage(busItemInvestQuery.getPageNum(),busItemInvestQuery.getPageSize());
        return new PageList(busItemInvestMapper.queryBusItemInvestsByItemId(busItemInvestQuery));
    }

    @Override
    public void addBusItemInvest(Integer itemId, BigDecimal amount, String busiPassword, Integer userId) {
        /**
         * 1.参数校验
         *     1.1 参数非空
         *     1.2 交易密码 必须正确
         *     1.3 投标记录是否存在校验
         *     1.4 投标金额 账户中金额满足投标金额
         *     1.5 投标项目
         *          单笔最小投资
         *          单笔最大投资
         *        项目剩余投资金额合法校验
         *     1.6 投标项目 (新手标)
         *         新手标只能进行一次投资
         *         定向标 特定用户投资
         *         app端pc 端不能进行投资
         *         是否开放校验
         * 2.记录更新操作
         *       bas_item	项目表
                 bus_item_invest	项目投资表
                 bus_user_stat	用户统计表
                 bus_account	用户账户表
                 bus_account_log	用户账户操作日志表
                 bus_income_stat	用户收益信息表
                 bus_user_integral	用户积分表
                 bus_integral_log	积分操作日志表
         3.结果返回(void)
         *
         */
        AssertUtil.isTrue(null==userId||null==userService.queryBasUserByUserId(userId),"用户未登录!");
        AssertUtil.isTrue(StringUtils.isBlank(busiPassword),"交易密码非空!");
        BasUserSecurity basUserSecurity=basUserSecurityService.queryBasUserSecurityByUserId(userId);
        AssertUtil.isTrue(!(basUserSecurity.getPaymentPassword().equals(MD5.toMD5(busiPassword))),"交易密码不正确!");
        AssertUtil.isTrue(null==amount||amount.compareTo(BigDecimal.ZERO)<0,"交易金额必须大于0!");
        AssertUtil.isTrue(null==itemId||itemId==0,"投资记录id不存在!");
        BasItem basItem=basItemService.queryBasItemByItemId(itemId);
        AssertUtil.isTrue(null==basItem,"待投资记录不存在!");
        AssertUtil.isTrue(basItem.getItemStatus()== ItemStatus.WAITOPEN,"项目处于未开放状态，暂时不能进行投资操作!");
        AssertUtil.isTrue(basItem.getItemStatus()==ItemStatus.FULL_COMPLETE,"项目已满标，不能再次进行投资操作!");
        AssertUtil.isTrue(basItem.getMoveVip().equals(1),"移动端项目，pc端不能进行投资操作!");
        AssertUtil.isTrue(StringUtils.isNoneBlank(basItem.getPassword()),"定向表只能特定用户进行投资!");
        System.out.println("新手标:"+basItem.getItemIsnew());
        System.out.println("新手标投资次数:"+busItemInvestMapper.countItemInvestIsNew(userId));
        Integer isNew=Integer.parseInt(basItem.getItemIsnew().toString());
        System.out.println("isTrue:"+(isNew.equals(1)&&busItemInvestMapper.countItemInvestIsNew(userId)>0));
        AssertUtil.isTrue(isNew.equals(1)&&busItemInvestMapper.countItemInvestIsNew(userId)>0,"新手标仅支持首投!");
        BusAccount busAccount=busAccountService.queryBusAccountInfoByUserId(userId);
        AssertUtil.isTrue(amount.compareTo(busAccount.getUsable())==1,"投资金额不能大于账户可用金额!");
        // 最低投标金额
        AssertUtil.isTrue(basItem.getItemSingleMinInvestment().compareTo(BigDecimal.ZERO)>0
                &&amount.compareTo(basItem.getItemSingleMinInvestment())==-1,"单笔投标金额必须大于单笔最低投资金额!");
        // 最高投标金额
        AssertUtil.isTrue(basItem.getItemSingleMaxInvestment().compareTo(BigDecimal.ZERO)>0
        &&amount.compareTo(basItem.getItemSingleMaxInvestment())==1,"单笔投标金额不能大于单笔最大投标金额");
        BigDecimal syAmount=basItem.getItemAccount().add(basItem.getItemOngoingAccount().negate());
        AssertUtil.isTrue(basItem.getItemSingleMinInvestment().compareTo(BigDecimal.ZERO)>0
        &&syAmount.compareTo(basItem.getItemSingleMinInvestment())==-1,"项目即将处于截标，不能进行投资操作!");
        // 表记录更新操作
        BusItemInvest busItemInvest=new BusItemInvest();
        // 实际已收总额=本金+利息(当前值=0)
        busItemInvest.setActualCollectAmount(BigDecimal.ZERO);
        busItemInvest.setActualCollectInterest(BigDecimal.ZERO);
        busItemInvest.setActualCollectPrincipal(BigDecimal.ZERO);
        // 实际未收总额=本金+利息
        BigDecimal lx= Calculator.getInterest(amount,basItem);
        System.out.println("利息:"+lx);
       busItemInvest.setActualUncollectAmount(amount.add(lx));
       busItemInvest.setActualUncollectInterest(lx);
       busItemInvest.setActualUncollectPrincipal(amount);
       busItemInvest.setAdditionalRateAmount(BigDecimal.ZERO);
       busItemInvest.setAddtime(new Date());
       busItemInvest.setCollectAmount(amount.add(lx));
       busItemInvest.setCollectInterest(lx);
       busItemInvest.setCollectPrincipal(amount);
       busItemInvest.setInvestAmount(amount);
       busItemInvest.setInvestCurrent(1);// 定期投资
        busItemInvest.setInvestDealAmount(amount);
        busItemInvest.setInvestOrder("SXT_TZ_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));// 投资订单号
        busItemInvest.setInvestStatus(0);
        busItemInvest.setInvestType(1);
        busItemInvest.setItemId(itemId);
        busItemInvest.setUpdatetime(new Date());
        busItemInvest.setUserId(userId);
        AssertUtil.isTrue(busItemInvestMapper.insert(busItemInvest)<1, P2pConstant.OPT_FAILED_MSG);

        // 更新用户统计表信息
        BusUserStat busUserStat= busUserStatMapper.queryBusUserStatByUserId(userId);
        busUserStat.setInvestAmount(busUserStat.getInvestAmount().add(amount));
        busUserStat.setInvestCount(busUserStat.getInvestCount()+1);
        AssertUtil.isTrue(busUserStatMapper.update(busUserStat)<1,P2pConstant.OPT_FAILED_MSG);

        busAccount.setUsable(busAccount.getUsable().add(amount.negate()));
        busAccount.setCash(busAccount.getCash().add(amount.negate()));
        busAccount.setTotal(busAccount.getTotal().add(lx));
        busAccount.setFrozen(busAccount.getFrozen().add(amount));
        busAccount.setWait(busAccount.getWait().add(amount));
        AssertUtil.isTrue(busAccountService.updateBusAccount(busAccount)<1,P2pConstant.OPT_FAILED_MSG);
        // 日志添加
        BusAccountLog busAccountLog=new BusAccountLog();
        busAccountLog.setUserId(userId);
        busAccountLog.setWait(busAccount.getWait());
        busAccountLog.setUsable(busAccount.getUsable());
        busAccountLog.setTotal(busAccount.getTotal());
        busAccountLog.setRepay(busAccount.getRepay());
        busAccountLog.setRemark("用户投标操作");
        busAccountLog.setOperType("user do invest");
        busAccountLog.setOperMoney(amount);
        busAccountLog.setFrozen(busAccount.getFrozen());
        busAccountLog.setCash(busAccount.getCash());
        busAccountLog.setBudgetType(2);// 添加
        busAccountLog.setAddtime(new Date());
        AssertUtil.isTrue(busAccountLogMapper.insert(busAccountLog)<1,P2pConstant.OPT_FAILED_MSG);
        //  收益信息更新
        BusIncomeStat busIncomeStat= busIncomeStatMapper.queryBusIncomeStatByUserId(userId);
        busIncomeStat.setTotalIncome(busIncomeStat.getTotalIncome().add(lx));
        busIncomeStat.setWaitIncome(busIncomeStat.getWaitIncome().add(lx));
        AssertUtil.isTrue(busIncomeStatMapper.update(busIncomeStat)<1,P2pConstant.OPT_FAILED_MSG);

        // 积分信息更新
        BusUserIntegral busUserIntegral=busUserIntegralMapper.queryBusUserIntegralByUserId(userId);
        busUserIntegral.setTotal(busUserIntegral.getTotal()+200);
        busUserIntegral.setUsable(busUserIntegral.getUsable()+200);
        AssertUtil.isTrue(busUserIntegralMapper.update(busUserIntegral)<1,P2pConstant.OPT_FAILED_MSG);

        // 积分日志添加
        BusIntegralLog busIntegralLog=new BusIntegralLog();
        busIntegralLog.setAddtime(new Date());
        busIntegralLog.setIntegral(200);
        busIntegralLog.setStatus(0);
        busIntegralLog.setUserId(userId);
        busIntegralLog.setWay("用户投标");
        AssertUtil.isTrue(busIntegralLogMapper.insert(busIntegralLog)<1,P2pConstant.OPT_FAILED_MSG);



        // 项目信息更新
        basItem.setItemOngoingAccount(basItem.getItemOngoingAccount().add(amount));
        if(basItem.getItemOngoingAccount().compareTo(basItem.getItemAccount())==0){
            basItem.setItemStatus(ItemStatus.FULL_COMPLETE);// 设置项目状态未满标状态
        }
        // 进度值计算  a.divide(b, 2, RoundingMode.HALF_UP)
        BigDecimal itemScale=basItem.getItemOngoingAccount().divide(basItem.getItemAccount(), 2, RoundingMode.HALF_UP);
        basItem.setItemScale(itemScale.multiply(BigDecimal.valueOf(100L)));
        basItem.setInvestTimes(basItem.getInvestTimes()+1);
        basItem.setUpdateTime(new Date());
        AssertUtil.isTrue(basItemService.updateBasItem((BasItemDto) basItem)<1,P2pConstant.OPT_FAILED_MSG);


        // 清缓存 清除包含当前投资记录的缓存数据
       /*Set<String> keys= redisTemplate.keys("basItemList*");
       redisTemplate.delete(keys);*/
       redisTemplate.delete(redisTemplate.keys("basItemList*"));
    }

    @Override
    public Map<String, Object> countLastFiveMonthsInvestInfoByUserId(Integer userId) {
       List<Map<String,Object>> list= busItemInvestMapper.countLastFiveMonthsInvestInfoByUserId(userId);
       Map<String,Object> result=new HashMap<String,Object>();
       //  月份数组  每个月投资总金额
       List<String> months=new ArrayList<String>();
       List<BigDecimal> totals=new ArrayList<BigDecimal>();
       for(Map<String,Object> temp:list){
            // month  total
            months.add(temp.get("month").toString());
            totals.add(BigDecimal.valueOf(Double.parseDouble(temp.get("total").toString())));
        }
        result.put("months",months);
        result.put("totals",totals);
       return result;
    }

   /* public static void main(String[] args) {
        Byte a=1;
        *//*Integer b=;
        System.out.println(b);*//*
        System.out.println(Integer.parseInt(a.toString())&&3>0);
    }*/
}
