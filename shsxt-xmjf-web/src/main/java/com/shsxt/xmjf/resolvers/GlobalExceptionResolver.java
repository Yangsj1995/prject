package com.shsxt.xmjf.resolvers;

import com.alibaba.fastjson.JSON;
import com.shsxt.xmjf.api.constants.P2pConstant;
import com.shsxt.xmjf.api.exceptions.AuthException;
import com.shsxt.xmjf.api.exceptions.ParamsException;
import com.shsxt.xmjf.api.model.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Created by lp on 2018/3/2.
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        /***
         * 1.视图异常
         * 2.json 异常
         */
        if(ex instanceof AuthException){
            try {
                // 跳转到登录页面
                response.sendRedirect(request.getContextPath()+"/login");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        ModelAndView mv=getDefaultModelAndView(request,ex);
        if(handler instanceof HandlerMethod){
           HandlerMethod handlerMethod= (HandlerMethod) handler;
           Method method= handlerMethod.getMethod();
           ResponseBody responseBody= method.getAnnotation(ResponseBody.class);
           if(null!=responseBody){
               /**
                * json
                */
               ResultInfo resultInfo=new ResultInfo();
               resultInfo.setCode(P2pConstant.OPT_FAILED_CODE);
               resultInfo.setMsg(P2pConstant.OPT_FAILED_MSG);
               // 设置特定异常信息
               if(ex instanceof ParamsException){
                   ParamsException pe= (ParamsException) ex;
                   resultInfo.setMsg(pe.getMsg());
                   resultInfo.setCode(pe.getCode());
               }
               // 写出json数据到客户端(浏览器)
               response.setContentType("application/json;charset=utf-8");
               response.setCharacterEncoding("utf-8");
               PrintWriter pw=null;
               try {
                  pw= response.getWriter();
                  pw.write(JSON.toJSONString(resultInfo));
                  pw.flush();
               } catch (IOException e) {
                   e.printStackTrace();
               }finally {
                   if(null!=pw){
                       pw.close();
                   }
               }
            return null;

           }else{
               /**
                * view
                */
               if(ex instanceof  ParamsException){
                   ParamsException pe= (ParamsException) ex;
                   mv.addObject("errorMsg",pe.getMsg());
                   mv.addObject("errorCode",pe.getCode());
               }
               return mv;
           }
        }else{
            return mv;
        }

    }

    private ModelAndView getDefaultModelAndView(HttpServletRequest request,Exception ex) {
        ModelAndView mv=new ModelAndView();
        mv.setViewName("error");
        mv.addObject("errorCode",P2pConstant.OPT_FAILED_CODE);
        mv.addObject("errorMsg",ex.getMessage());
        return mv;
    }
}
