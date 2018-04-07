package com.shsxt.xmjf.controller;

import com.shsxt.xmjf.api.model.ResultInfo;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lp on 2018/3/3.
 */
public class BaseController {


    @ModelAttribute
    public  void preMethod(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
    }

    public ResultInfo success(String msg){
        ResultInfo resultInfo=new ResultInfo();
        resultInfo.setMsg(msg);
        return resultInfo;
    }
}
