package com.shsxt.xmjf.controller;

import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.exceptions.ParamsException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.User;
import com.shsxt.xmjf.api.service.IBasUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by lp on 2018/3/1.
 */
@Controller
@RequestMapping("user02")
public class BasUserController02 {
    @Resource
    private IBasUserService userService;




    @RequestMapping("register")
    @ResponseBody
    public ResultInfo userRegister(String phone,String code,String password){
        ResultInfo resultInfo=new ResultInfo();
        try{
            userService.saveUser(phone,password,code);
        }catch (ParamsException pe){
            resultInfo.setCode(pe.getCode());
            resultInfo.setMsg(pe.getMsg());
        }catch (Exception e){
            resultInfo.setCode(P2pConstant.OPT_FAILED_CODE);
            resultInfo.setMsg(P2pConstant.OPT_FAILED_MSG);
        }
        return resultInfo;
    }



    @RequestMapping("userLogin")
    @ResponseBody
    public  ResultInfo userLogin(String phone, String password, HttpSession session){
        ResultInfo resultInfo=new ResultInfo();
        try{
           UserModel userModel= userService.userLogin(phone,password);
           session.setAttribute(P2pConstant.SESSION_USER,userModel);
        }catch (ParamsException pe){
            resultInfo.setCode(pe.getCode());
            resultInfo.setMsg(pe.getMsg());
        }catch (Exception e){
            resultInfo.setCode(P2pConstant.OPT_FAILED_CODE);
            resultInfo.setMsg(P2pConstant.OPT_FAILED_MSG);
        }
        return resultInfo;
    }

    @RequestMapping("quickLogin")
    @ResponseBody
    public  ResultInfo quickLogin(String phone, String code,HttpSession session){
        ResultInfo resultInfo=new ResultInfo();
        try{
            UserModel userModel= userService.quickLogin(phone,code);
            session.setAttribute(P2pConstant.SESSION_USER,userModel);
        }catch (ParamsException pe){
            resultInfo.setCode(pe.getCode());
            resultInfo.setMsg(pe.getMsg());
        }catch (Exception e){
            resultInfo.setCode(P2pConstant.OPT_FAILED_CODE);
            resultInfo.setMsg(P2pConstant.OPT_FAILED_MSG);
        }
        return resultInfo;
    }

    @RequestMapping("exit")
    public  String exit(HttpSession session){
        session.removeAttribute(P2pConstant.SESSION_USER);
        return "redirect:/login";
    }

}
