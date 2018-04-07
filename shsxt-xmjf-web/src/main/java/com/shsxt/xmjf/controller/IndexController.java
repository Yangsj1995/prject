package com.shsxt.xmjf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lp on 2018/3/1.
 */
@Controller
public class IndexController {

    @RequestMapping("index")
    public  String index(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "index";
    }




    @RequestMapping("login")
    public  String loginPage(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "login";
    }


    @RequestMapping("quickLogin")
    public  String quickLogin(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "quick_login";
    }

    @RequestMapping("register")
    public  String register(HttpServletRequest request){
        request.setAttribute("ctx",request.getContextPath());
        return "register";
    }
}
