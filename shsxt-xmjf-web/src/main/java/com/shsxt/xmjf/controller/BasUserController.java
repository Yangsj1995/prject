package com.shsxt.xmjf.controller;

import com.shsxt.xmjf.annotations.IsLogin;
import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.exceptions.ParamsException;
import com.shsxt.xmjf.api.model.ResultInfo;
import com.shsxt.xmjf.api.model.UserModel;
import com.shsxt.xmjf.api.po.BasUserSecurity;
import com.shsxt.xmjf.api.service.IBasUserSecurityService;
import com.shsxt.xmjf.api.service.IBasUserService;
import com.shsxt.xmjf.api.utils.AssertUtil;
import com.sun.net.httpserver.Authenticator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by lp on 2018/3/1.
 */
@Controller
@RequestMapping("user")
public class BasUserController extends  BaseController{
    @Resource
    private IBasUserService userService;

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @RequestMapping("register")
    @ResponseBody
    public ResultInfo userRegister(String phone,String code,String password){
        ResultInfo resultInfo=new ResultInfo();
        userService.saveUser(phone,password,code);
        return resultInfo;
    }



    @RequestMapping("userLogin")
    @ResponseBody
    public  ResultInfo userLogin(String phone, String password, HttpSession session){
        ResultInfo resultInfo=new ResultInfo();
        UserModel userModel=userService.userLogin(phone,password);
        session.setAttribute(P2pConstant.SESSION_USER,userModel);
        return resultInfo;
    }

    @RequestMapping("quickLogin")
    @ResponseBody
    public  ResultInfo quickLogin(String phone, String code,HttpSession session){
        ResultInfo resultInfo=new ResultInfo();
        UserModel userModel= userService.quickLogin(phone,code);
        session.setAttribute(P2pConstant.SESSION_USER,userModel);
        return resultInfo;
    }

    @RequestMapping("exit")
    public  String exit(HttpSession session){
        session.removeAttribute(P2pConstant.SESSION_USER);
        return "redirect:/login";
    }


    @RequestMapping("checkUserIsAuth")
    @ResponseBody
    @IsLogin
    public  ResultInfo checkUserIsAuth(HttpSession session){
        UserModel userModel= (UserModel) session.getAttribute(P2pConstant.SESSION_USER);
        BasUserSecurity basUserSecurity= basUserSecurityService
                .queryBasUserSecurityByUserId(userModel.getId());
        AssertUtil.isTrue(!(basUserSecurity.getRealnameStatus().equals(1)),"用户信息未认证!");
        return success("用户已认证!");
    }

    @RequestMapping("auth")
    @IsLogin
    public  String toAuth(){
        return "auth";
    }


    @RequestMapping("doAuth")
    @ResponseBody
    @IsLogin
    public  ResultInfo doAuth(String realName,String idCard,String confirmPassword,String busiPassword,HttpSession session){
        UserModel userModel= (UserModel) session.getAttribute(P2pConstant.SESSION_USER);
        basUserSecurityService.updateBasUserSecurityByUserId(userModel.getId(),realName,idCard,busiPassword,confirmPassword);
        return success("认证成功!");
    }

}
