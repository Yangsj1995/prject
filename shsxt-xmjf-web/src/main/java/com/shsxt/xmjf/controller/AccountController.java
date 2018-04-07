package com.shsxt.xmjf.controller;

import com.shsxt.xmjf.annotations.IsLogin;
import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.dto.AccountDto;
import com.shsxt.xmjf.api.dto.PayInfo;
import com.shsxt.xmjf.api.exceptions.ParamsException;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.query.BusAccountRechargeQuery;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.IBusAccountRechargeService;
import com.shsxt.xmjf.api.service.IBusAccountService;
import com.shsxt.xmjf.api.utils.PageList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lp on 2018/3/2.
 */
@Controller
@RequestMapping("account")
public class AccountController  extends  BaseController{

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private IBusAccountRechargeService busAccountRechargeService;

    @Resource
    private IBusAccountService busAccountService;

    @IsLogin
    @RequestMapping("index")
    public  String index(HttpSession session, Model model){
        UserModel userModel= (UserModel) session.getAttribute(P2pConstant.SESSION_USER);
        BasUserSecurity basUserSecurity= basUserSecurityService.queryBasUserSecurityByUserId(userModel.getId());
        model.addAttribute("security",basUserSecurity);
        return "setting";
    }

    @RequestMapping("toRecharge")
    @IsLogin
    public  String toRecharge(){
        return "recharge";
    }



    @RequestMapping("doRecharge")
    public String doRecharge(BigDecimal amount,String code,String busiPassword,HttpSession session,Model model){
        String sessionCode= (String) session.getAttribute(P2pConstant.PICTURE_VERIFY_KEY);
        if(StringUtils.isBlank(code)){
            model.addAttribute("msg","验证码已失效!");
            return "recharge";
        }
        if(!(code.equals(sessionCode))){
            model.addAttribute("msg","验证码输入错误!");
            return "recharge";
        }

        UserModel userModel= (UserModel) session.getAttribute(P2pConstant.SESSION_USER);
       PayInfo payInfo= busAccountRechargeService.addBusAccountRechargeAndBuildPayParams(userModel.getId(),amount,busiPassword);
       model.addAttribute("pay",payInfo);
       return "pay";
    }


    @RequestMapping("callBack")
    @IsLogin
    public  String  callBack(@RequestParam(name = "total_fee") BigDecimal totalFee,
                          @RequestParam(name = "out_order_no")String orderNo,
                          String sign,
                          @RequestParam(name = "trade_status") String tradeStatus,HttpSession
                           session,RedirectAttributes redirectAttributes){
        try {
            UserModel userModel= (UserModel) session.getAttribute(P2pConstant.SESSION_USER);
            redirectAttributes.addAttribute("msg","支付成功!");
        }catch (ParamsException pe){
            pe.printStackTrace();
            redirectAttributes.addAttribute("msg",pe.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addAttribute("msg","支付异常!");
        }

        return "redirect:result";
    }


    @RequestMapping("result")
        public  String result(String msg,Model model){
        System.out.println("支付结果:"+msg);
            model.addAttribute("msg",msg);
            return  "result";
    }


    @RequestMapping("rechargeRecode")
    @IsLogin
    public  String toRechargeRecodePage(){
        return "recharge_record";
    }

    @RequestMapping("queryBusAccountRechargesByUserId")
    @ResponseBody
    @IsLogin
    public PageList queryBusAccountRechargesByUserId(BusAccountRechargeQuery busAccountRechargeQuery,HttpSession session){
         UserModel userModel= (UserModel) session.getAttribute(P2pConstant.SESSION_USER);
         busAccountRechargeQuery.setUserId(userModel.getId());
        return busAccountRechargeService.queryBusAccountRechargesByUserId(busAccountRechargeQuery);
    }


    @RequestMapping("accountInfo")
    @IsLogin
    public  String busAccountInfo(){
        return "account_info";
    }

    @RequestMapping("queryAccountInfoByUserId")
    @ResponseBody
    @IsLogin
    public List<AccountDto> queryAccountInfoByUserId(HttpSession session){
        UserModel userModel= (UserModel) session.getAttribute(P2pConstant.SESSION_USER);
        return busAccountService.queryAccountInfoByUserId(userModel.getId());
    }




}
