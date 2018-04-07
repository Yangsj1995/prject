package com.shsxt.xmjf.controller;

import com.shsxt.xmjf.annotations.IsLogin;
import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.query.BusItemInvestQuery;
import com.shsxt.xmjf.api.service.IBusItemInvestService;
import com.shsxt.xmjf.api.utils.PageList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.management.relation.RelationSupport;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by lp on 2018/3/5.
 */
@Controller
@RequestMapping("busItemInvest")
public class BusItemInvestController extends  BaseController{
    @Resource
    private IBusItemInvestService busItemInvestService;


    @RequestMapping("queryBusItemInvestsByItemId")
    @ResponseBody
    public PageList queryBusItemInvestsByItemId(BusItemInvestQuery busItemInvestQuery){
        return busItemInvestService.queryBusItemInvestsByItemId(busItemInvestQuery);
    }


    @RequestMapping("doInvest")
    @ResponseBody
    @IsLogin
    public ResultInfo doInvest(BigDecimal amount, Integer itemId, String busiPassword, HttpSession session){
        UserModel userModel= (UserModel) session.getAttribute(P2pConstant.SESSION_USER);
        busItemInvestService.addBusItemInvest(itemId,amount,busiPassword,userModel.getId());
        return  success("投标成功!");
    }

    @RequestMapping("countLastFiveMonthsInvestInfoByUserId")
    @ResponseBody
    @IsLogin
    public  Map<String,Object> countLastFiveMonthsInvestInfoByUserId(HttpSession session){
        UserModel userModel= (UserModel) session.getAttribute(P2pConstant.SESSION_USER);
        return busItemInvestService.countLastFiveMonthsInvestInfoByUserId(userModel.getId());
    }

}
