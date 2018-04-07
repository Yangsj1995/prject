package com.shsxt.xmjf.controller;

import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.exceptions.ParamsException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.service.ISmsService;
import com.shsxt.xmjf.api.utils.RandomCodesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by lp on 2018/3/1.
 */

@Controller
@RequestMapping("sms")
public class SmsController {
    @Resource
    private ISmsService smsService;



    @RequestMapping("sendPhoneSms")
    @ResponseBody
    public ResultInfo sendPhoneSms(String phone, String type, String picCode, HttpSession session){
        ResultInfo resultInfo=new ResultInfo();
        /**
         * 图形验证码校验
         *   验证码不能过期  有效
         *   比对验证码值是否正确
         */
       String sessionPicCode= (String) session.getAttribute(P2pConstant.PICTURE_VERIFY_KEY);
       if(StringUtils.isBlank(sessionPicCode)){
            resultInfo.setCode(300);
            resultInfo.setMsg("图形验证码已失效，请刷新页面!");
            return resultInfo;
       }
       if(!(picCode.equals(sessionPicCode))){
            resultInfo.setCode(300);
            resultInfo.setMsg("验证码输入不正确!");
            return resultInfo;
       }
       // 移除图形验证码
       session.removeAttribute(P2pConstant.PICTURE_VERIFY_KEY);
       try {
           String code= RandomCodesUtils.createRandom(true,4);
           smsService.sendPhoneSms(phone,type,code);
       }catch (ParamsException pe){
            resultInfo.setCode(pe.getCode());
            resultInfo.setMsg(pe.getMsg());
       }catch (Exception e){
           resultInfo.setCode(P2pConstant.OPT_FAILED_CODE);
           resultInfo.setMsg(P2pConstant.OPT_FAILED_MSG);
       }
        return resultInfo;
    }

}
